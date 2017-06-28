package com.example.dennis.divoi.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Usuario;

/**
 * Created by dennis on 22/06/17.
 */

public class DetalheUsuarioActivity extends AppCompatActivity {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalheusuario);
        recuperarObjeto();
        setarDados();

    }

    public void recuperarObjeto(){

        Intent intent = getIntent();
        usuario = new Usuario();
        usuario.setId(intent.getLongExtra("id",0L));
        usuario.setNome(intent.getStringExtra("nome"));
        usuario.setEmail(intent.getStringExtra("email"));
        usuario.setSenha(intent.getStringExtra("senha"));
        usuario.setTipo(intent.getStringExtra("tipo"));

    }
    public void setarDados(){
        TextView textViewNome = (TextView) findViewById(R.id.textViewNomeUsuario);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        TextView textViewTipo = (TextView) findViewById(R.id.textViewTipo);

        textViewNome.setText(usuario.getNome());
        textViewEmail.setText(usuario.getEmail());
        textViewTipo.setText(usuario.getTipo());
    }
}
