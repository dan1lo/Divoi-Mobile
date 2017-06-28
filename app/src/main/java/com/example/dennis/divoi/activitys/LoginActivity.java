package com.example.dennis.divoi.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Usuario;
import com.example.dennis.divoi.util.Constants;
import com.example.dennis.divoi.util.FileUtil;
import com.example.dennis.divoi.util.HttpUtil;
import com.example.dennis.divoi.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 10/04/17.
 */

public class LoginActivity extends AppCompatActivity {

    private TextView textViewCadastro;
    private List<Usuario> usuarios;
    private ProgressDialog progressDialog;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textViewCadastro = (TextView) findViewById(R.id.textViewCadastro);
        usuarios = new ArrayList<>();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextSenha = (EditText) findViewById(R.id.editText_senha);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Carregando Dados");
                progressDialog.setMessage("Aguarde um momento...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                Log.d("TESTE", "TO NO FOR");
                carregar();
                Log.d("TESTE", "TO NO FOR");

                String email = editTextEmail.getText().toString();
                String senha = null;
                String confere = "NP";
                try {
                    senha = StringUtil.SHA1(editTextSenha.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                for(Usuario usuario : usuarios){
                    Log.d("TESTE", "TO NO FOR");
                    if(usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)){
                        Log.d("TESTE", "TO NO IF");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("tipoUsuario", usuario.getTipo());
                        intent.putExtra("logado", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        confere = "E";
                        startActivity(intent);
                        break;
                    }else{
                        confere = "NE";
                    }
                }
                if(confere.equals("NE")){

                    Toast.makeText(LoginActivity.this, "Usuario não encontrado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void chamarTelaCadastro(View view) {

        Intent intent = new Intent(this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    public void carregar() {
        if (HttpUtil.hasConnection(LoginActivity.this)) {
            new UsuariosAsyncTask().execute();
        } else {
            Toast.makeText(LoginActivity.this, "Verifique a conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void carregarUsuarios(){
        try{
            HttpURLConnection connection = HttpUtil.openConnection(Constants.IP_SERVIDOR+"ListaUsuarioGson", "GET", true, false);
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                JSONArray jsonArray = new JSONArray(FileUtil.bytesToString(inputStream));
                for(int i = 0; i< jsonArray.length(); i++){
                    Usuario usuario = new Usuario();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    usuario.setId(Long.parseLong(jsonObject.getString("id")));
                    usuario.setNome(jsonObject.getString("nome"));
                    usuario.setEmail(jsonObject.getString("email"));
                    usuario.setSenha(jsonObject.getString("senha"));
                    usuario.setTipo(jsonObject.getString("tipo"));

                    usuarios.add(usuario);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    class UsuariosAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... strings){
            carregarUsuarios();
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            progressDialog.dismiss();
            super.onPostExecute(v);

        }
    }
}
