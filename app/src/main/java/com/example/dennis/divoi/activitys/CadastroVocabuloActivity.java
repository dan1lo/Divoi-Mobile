package com.example.dennis.divoi.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.util.Constants;
import com.example.dennis.divoi.util.HttpHelper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 11/04/17.
 */

public class CadastroVocabuloActivity extends AppCompatActivity {

    private EditText vocabulo;
    private EditText traducao;
    private EditText aplicacaoFrase;
    private EditText traducaoFrase;
    private Palavra palavra;
    private Button bttnCadastrarPalavra;
    private ProgressDialog progressDialog;
    private HttpHelper httpHelper = new HttpHelper(Constants.IP_SERVIDOR);
    private long idLingua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrovocabulo);

        Intent intent = getIntent();
        idLingua = intent.getLongExtra("idLingua",0L);


        bttnCadastrarPalavra = (Button) findViewById(R.id.cadastrarPalavra);
        bttnCadastrarPalavra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CadastroVocabuloActivity.this);
                new PalavraAsyncTask().execute(recuperarObjeto());

            }
        });
    }
    public Palavra recuperarObjeto(){
        palavra = new Palavra();
        Lingua lingua = new Lingua();

        vocabulo = (EditText) findViewById(R.id.editText_palavra);
        traducao = (EditText) findViewById(R.id.editText_traducao);
        aplicacaoFrase = (EditText) findViewById(R.id.editText_aplicacaoFrase);
        traducaoFrase= (EditText) findViewById(R.id.editText_traducaoFrase);

        lingua.setId(idLingua);

        palavra.setLingua(lingua);
        palavra.setPalavra(vocabulo.getText().toString());
        palavra.setTraducao(traducao.getText().toString());
        palavra.setAplicacaoFrase(aplicacaoFrase.getText().toString());
        palavra.setTraducaoFrase(traducaoFrase.getText().toString());

        return palavra;
    }

    class PalavraAsyncTask extends AsyncTask<Palavra, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setTitle("Enviando dados...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Palavra... palavraArray){
            Palavra palara = palavraArray[0];
            Gson gson = new Gson();
            String body = gson.toJson(palavra);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            String response = httpHelper.doPOST("RecebePalavra",  headers, body);
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            super.onPostExecute(response);
            progressDialog.dismiss();
            if(response!=null){
                Toast.makeText(CadastroVocabuloActivity.this, "Dados enviado com sucesso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CadastroVocabuloActivity.this, ListaPalavrasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idLingua",idLingua);
                startActivity(intent);
            }else{
                Toast.makeText(CadastroVocabuloActivity.this, "Erro ao enviar dados", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
