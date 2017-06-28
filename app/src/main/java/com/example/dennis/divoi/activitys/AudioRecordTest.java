package com.example.dennis.divoi.activitys;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Arquivo;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.util.Constants;
import com.example.dennis.divoi.util.HttpHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 28/05/17.
 */

public class AudioRecordTest extends AppCompatActivity {
    private ImageButton mRecordBtn;
    private TextView mRecordLabel;

    private MediaRecorder mRecorder;

    private String mFileName = null;

    private static final String LOG_TAG = "Record_log";

    private FirebaseStorage storage;

    private Palavra palavra;

    private ProgressDialog mProgress;

    private HttpHelper httpHelper = new HttpHelper(Constants.IP_SERVIDOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravaraudio);

        recuperarObjeto();

        storage = FirebaseStorage.getInstance();

        mRecordLabel = (TextView) findViewById(R.id.recordLabel);
        mRecordBtn = (ImageButton) findViewById(R.id.recordButton);

        mProgress = new ProgressDialog(this);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/"+palavra.getId()+".mp3";

        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    startRecording();
                    mRecordLabel.setText("Gravando...");

                } else if (event.getAction() == MotionEvent.ACTION_UP){

                    stopRecording();
                    mRecordLabel.setText("Gravação finalizada");

                }

                return false;
            }
        });
    }

    public void recuperarObjeto() {

        Intent intent = getIntent();
        palavra = new Palavra();
        palavra.setId(intent.getLongExtra("idPalavra", 0L));
        palavra.setPalavra(intent.getStringExtra("palavra"));
        palavra.setTraducao(intent.getStringExtra("traducao"));
        palavra.setAplicacaoFrase(intent.getStringExtra("frase"));
        palavra.setTraducaoFrase(intent.getStringExtra("traducaoFrase"));
        if (intent.getStringExtra("imagemUrl") != null) {
            palavra.setImagemUrl(intent.getStringExtra("imagemUrl"));
        }
        if (intent.getStringExtra("audioUrl") != null) {
            palavra.setAudioUrl(intent.getStringExtra("audioUrl"));
        }

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }


    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }

    private void uploadAudio() {

        mProgress.setMessage("Enviando áudio");
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();

        StorageReference filePath = storage.getReference("Audios/"+palavra.getId());

        Uri uri = Uri.fromFile(new File(mFileName));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                palavra.setAudioUrl(taskSnapshot.getDownloadUrl().toString());
                Arquivo arquivo = new Arquivo();
                arquivo.setIdDialeto(palavra.getId());
                arquivo.setAudio(taskSnapshot.getDownloadUrl().toString());
                new ArquivoAsyncTask().execute(arquivo);
            }
        });
    }

    class ArquivoAsyncTask extends AsyncTask<Arquivo, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgress.setTitle("Enviando dados...");
            mProgress.setIndeterminate(true);
            mProgress.setCancelable(false);
            mProgress.show();
        }

        @Override
        protected String doInBackground(Arquivo... arquivoArray){
            Arquivo arquivo = arquivoArray[0];
            Gson gson = new Gson();
            String body = gson.toJson(arquivo);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            String response = httpHelper.doPOST("RecebeArquivo",  headers, body);
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            super.onPostExecute(response);
            mRecordLabel.setText("Áudio enviado com sucesso!");
            mProgress.dismiss();
            if(response!=null){
                Toast.makeText(AudioRecordTest.this, "Dados enviado com sucesso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AudioRecordTest.this, DetalhePalavraActivity.class);
                intent.putExtra("idPalavra", palavra.getId());
                intent.putExtra("palavra", palavra.getPalavra());
                intent.putExtra("traducao", palavra.getTraducao());
                intent.putExtra("frase", palavra.getAplicacaoFrase());
                intent.putExtra("traducaoFrase", palavra.getTraducaoFrase());
                intent.putExtra("imagemUrl",palavra.getImagemUrl());
                intent.putExtra("audioUrl",palavra.getAudioUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                Toast.makeText(AudioRecordTest.this, "Erro ao enviar dados", Toast.LENGTH_SHORT).show();
            }
        }

    }


}


