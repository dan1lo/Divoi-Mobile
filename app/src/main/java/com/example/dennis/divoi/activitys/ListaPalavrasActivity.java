package com.example.dennis.divoi.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.adapters.PalavrasAdapter;
import com.example.dennis.divoi.fragments.LinguasFragment;
import com.example.dennis.divoi.fragments.PalavrasFragment;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.util.FileUtil;
import com.example.dennis.divoi.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 20/04/17.
 */

public class ListaPalavrasActivity extends AppCompatActivity{

    private PalavrasFragment palavrasFragment;
    private ImageButton bttnCadastro;
    private long idLingua;
    private boolean logado;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listapalavras);

        Intent intent = getIntent();
        idLingua = intent.getLongExtra("idLingua",0L);
        logado = intent.getBooleanExtra("logado",false);
        tipoUsuario = intent.getStringExtra("tipoUsuario");
        intent.putExtra("logado", logado);
        intent.putExtra("tipoUsuario",tipoUsuario);

        if(savedInstanceState == null){
            palavrasFragment = new PalavrasFragment();

            palavrasFragment.setArguments(intent.getExtras());

            getSupportFragmentManager().beginTransaction().replace(R.id.containerPalavra,palavrasFragment).commit();
        }
        bttnCadastro = (ImageButton) findViewById(R.id.fabutton_cadastrarpalavra);

        if(!logado || tipoUsuario.equals("Pesquisador"))
            bttnCadastro.setVisibility(View.GONE);

        bttnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaPalavrasActivity.this, CadastroVocabuloActivity.class);
                intent.putExtra("idLingua", idLingua);
                startActivity(intent);
            }
        });

    }
}
