package com.example.dennis.divoi.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Arquivo;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.util.Constants;
import com.example.dennis.divoi.util.HttpHelper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 10/04/17.
 */

public class CadastroLinguaActivity extends AppCompatActivity {

    private EditText nomeLingua;
    private EditText povo;
    private EditText localizacao;
    private EditText descricao;
    private Lingua lingua;
    private Button bttnCadastrar;
    private ProgressDialog progressDialog;
    private HttpHelper httpHelper = new HttpHelper(Constants.IP_SERVIDOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrolingua);

        bttnCadastrar = (Button) findViewById(R.id.bttn_cadastrarLingua);

        bttnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CadastroLinguaActivity.this);
                new LinguaAsyncTask().execute(recuperarObjeto());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lingua, menu);
        return true;
    }

    public Lingua recuperarObjeto(){

        lingua = new Lingua();

        nomeLingua = (EditText) findViewById(R.id.editText_nomeLingua);
        povo = (EditText) findViewById(R.id.editText_povo);
        localizacao = (EditText) findViewById(R.id.editText_localizacao);
        descricao = (EditText) findViewById(R.id.editText_descricao);

        lingua.setNome(nomeLingua.getText().toString());
        lingua.setPovo(povo.getText().toString());
        lingua.setLocalizacao(localizacao.getText().toString());
        lingua.setDescricao(descricao.getText().toString());

        return lingua;
    }

    class LinguaAsyncTask extends AsyncTask<Lingua, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setTitle("Enviando dados...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Lingua... linguaArray){
            Lingua lingua = linguaArray[0];
            Gson gson = new Gson();
            String body = gson.toJson(lingua);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            String response = httpHelper.doPOST("RecebeLingua",  headers, body);
            return response;
        }

        @Override
        protected void onPostExecute(String response){
            super.onPostExecute(response);
            progressDialog.dismiss();
            if(response!=null){
                Toast.makeText(CadastroLinguaActivity.this, "Dados enviado com sucesso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CadastroLinguaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                Toast.makeText(CadastroLinguaActivity.this, "Erro ao enviar dados", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
