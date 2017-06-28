package com.example.dennis.divoi.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Lingua;

/**
 * Created by dennis on 17/04/17.
 */

public class DetalheLinguaActivity extends AppCompatActivity {

    private Lingua lingua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhelingua);
        recuperarObjeto();
        setarDados();

    }
    public void recuperarObjeto(){

        Intent intent = getIntent();
        lingua = new Lingua();
        lingua.setId(intent.getLongExtra("idLingua",0L));
        lingua.setNome(intent.getStringExtra("nome"));
        lingua.setPovo(intent.getStringExtra("povo"));
        lingua.setLocalizacao(intent.getStringExtra("localizacao"));
        lingua.setDescricao(intent.getStringExtra("descricao"));

    }

    public void setarDados(){
        TextView textViewNome = (TextView) findViewById(R.id.textViewNomeLingua);
        TextView textViewPovo = (TextView) findViewById(R.id.textViewPovo);
        TextView textViewLocalizacao = (TextView) findViewById(R.id.textViewLocalizacaoLingua);
        TextView textViewDescricao = (TextView) findViewById(R.id.textViewDescricaoLingua);

        textViewNome.setText(lingua.getNome());
        textViewPovo.setText(lingua.getPovo());
        textViewLocalizacao.setText(lingua.getLocalizacao());
        textViewDescricao.setText(lingua.getDescricao());
    }
}
