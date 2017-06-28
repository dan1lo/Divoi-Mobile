package com.example.dennis.divoi.activitys;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.divoi.BuildConfig;
import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Arquivo;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.util.Constants;
import com.example.dennis.divoi.util.HttpHelper;
import com.example.dennis.divoi.util.PermissionUtil;
import com.example.dennis.divoi.util.SDCardUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 25/05/17.
 */

public class DetalhePalavraActivity extends AppCompatActivity {

    private Palavra palavra;
    private ImageButton imagemButton;
    private ImageButton audioButton;
    private ImageView imagePalavra;
    private ProgressDialog progressDialog;
    private File photoFile;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private boolean logado = false;
    private String tipoUsuario;

    private HttpHelper httpHelper = new HttpHelper(Constants.IP_SERVIDOR);


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhepalavra);
        recuperarObjeto();

        imagemButton = (ImageButton) findViewById(R.id.imageButton_imagem);
        audioButton = (ImageButton) findViewById(R.id.imageButton_audio);
        if (palavra.getImagemUrl() == null) {
            imagemButton.setVisibility(View.GONE);
        }
        if (palavra.getAudioUrl() == null) {
            audioButton.setVisibility(View.GONE);
        }
        imagemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(DetalhePalavraActivity.this);
                progressDialog.setTitle("Carregando Imagem");
                progressDialog.setMessage("Aguarde um momento...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final Dialog dialog = new Dialog(DetalhePalavraActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_image, null));

                final LayoutInflater factory = dialog.getLayoutInflater();
                final View view = factory.inflate(R.layout.dialog_image, null);

                imagePalavra = (ImageView) view.findViewById(R.id.imageViewPalavra);

                Picasso.with(DetalhePalavraActivity.this).load(palavra.getImagemUrl()).into(imagePalavra, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        dialog.setContentView(view);
                        progressDialog.dismiss();
                        dialog.show();
                    }

                    @Override
                    public void onError() {
                        progressDialog.dismiss();
                        Toast.makeText(DetalhePalavraActivity.this, "Verifique sua conexão com a internet e tente novamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(DetalhePalavraActivity.this);
                progressDialog.setTitle("Carregando o áudio");
                progressDialog.setMessage("Aguarde um momento...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                try {

                    final MediaPlayer player = new MediaPlayer();
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(palavra.getAudioUrl());

                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            progressDialog.dismiss();
                            player.start();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        setarDados();

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
        logado = intent.getBooleanExtra("logado", false);
        tipoUsuario = intent.getStringExtra("tipoUsuario");

    }

    public void setarDados() {
        TextView textViewPalavra = (TextView) findViewById(R.id.textViewPalavra);
        TextView textViewTraducao = (TextView) findViewById(R.id.textViewTraducaoPalavra);
        TextView textViewAplicacaoFrase = (TextView) findViewById(R.id.textViewAplicacaoFrase);
        TextView textViewTraducaoAplicacaoFrase = (TextView) findViewById(R.id.textViewTraducaoFrase);

        textViewPalavra.setText(palavra.getPalavra());
        textViewTraducao.setText(palavra.getTraducao());
        textViewAplicacaoFrase.setText(palavra.getAplicacaoFrase());
        textViewTraducaoAplicacaoFrase.setText(palavra.getTraducaoFrase());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(logado && !tipoUsuario.equals("Pesquisador"))
            getMenuInflater().inflate(R.menu.menu_toolbar_detalhe, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Intent intent;
        switch (item.getItemId()) {
            case R.id.action_tirarFoto:

                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                if (PermissionUtil.validate(DetalhePalavraActivity.this, 0, permissions)) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;

                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(this,
                                    "com.example.dennis.divoi.provider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        }
                    }
                }
                break;
            case R.id.action_gravarAudio:

                Intent intent = new Intent(DetalhePalavraActivity.this,AudioRecordTest.class);
                intent.putExtra("idPalavra", palavra.getId());
                intent.putExtra("palavra", palavra.getPalavra());
                intent.putExtra("traducao", palavra.getTraducao());
                intent.putExtra("frase", palavra.getAplicacaoFrase());
                intent.putExtra("traducaoFrase", palavra.getTraducaoFrase());
                intent.putExtra("imagemUrl",palavra.getImagemUrl());
                intent.putExtra("audioUrl",palavra.getAudioUrl());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            StorageReference firebaseRef = storage.getReference("Imagens/"+palavra.getId());

            progressDialog = new ProgressDialog(DetalhePalavraActivity.this);
            progressDialog.setTitle("Enviando Imagem");
            progressDialog.setMessage("Aguarde um momento...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();

            UploadTask uploadTask = firebaseRef.putFile(contentUri);

            uploadTask.addOnProgressListener(DetalhePalavraActivity.this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    Log.d("PROGRESS", String.valueOf(progress));
                    progressDialog.setProgress(progress);

                }
            });
            uploadTask.addOnSuccessListener(DetalhePalavraActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    palavra.setImagemUrl(taskSnapshot.getDownloadUrl().toString());

                    // --------------------- mostrar na tela -------------------
                    Toast.makeText(DetalhePalavraActivity.this, "Imagem enviada com sucesso!", Toast.LENGTH_LONG).show();
                    Arquivo arquivo = new Arquivo();
                    arquivo.setIdDialeto(palavra.getId());
                    arquivo.setImagem(taskSnapshot.getDownloadUrl().toString());
                    new ArquivoAsyncTask().execute(arquivo);
                }
            });
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    class ArquivoAsyncTask extends AsyncTask<Arquivo, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setTitle("Enviando dados...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Arquivo... arquivoArray){
            Arquivo arquivo = arquivoArray[0];
            Gson gson = new Gson();
            String body = gson.toJson(arquivo);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");
            String response = httpHelper.doPOST("RecebeImagem",  headers, body);
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            super.onPostExecute(response);
            progressDialog.dismiss();
            if(response!=null){
                Toast.makeText(DetalhePalavraActivity.this, "Dados enviado com sucesso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetalhePalavraActivity.this, DetalhePalavraActivity.class);
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
                Toast.makeText(DetalhePalavraActivity.this, "Erro ao enviar dados", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
