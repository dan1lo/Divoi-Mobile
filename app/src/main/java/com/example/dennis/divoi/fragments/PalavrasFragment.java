package com.example.dennis.divoi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.activitys.DetalhePalavraActivity;
import com.example.dennis.divoi.activitys.ListaPalavrasActivity;
import com.example.dennis.divoi.adapters.PalavrasAdapter;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.util.Constants;
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
 * Created by dennis on 18/04/17.
 */

public class PalavrasFragment extends Fragment {

    public static final String TAG = ".fragment.PalavrasFragment";
    private RecyclerView recyclerView;
    private PalavrasAdapter palavrasAdapter;
    private List<Palavra> palavras = new ArrayList<>();
    private ProgressDialog progressDialog;
    private long idLingua;
    private boolean logado = false;
    private String tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_palavras, container, false);

        idLingua = getArguments().getLong("idLingua");

        logado = getArguments().getBoolean("logado");
        tipoUsuario = getArguments().getString("tipoUsuario");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando Dados");
        progressDialog.setMessage("Aguarde um momento...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_palavras);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        palavrasAdapter = new PalavrasAdapter(getContext(), palavras, onClickPalavra());
        recyclerView.setAdapter(palavrasAdapter);

        carregar();

        return view;
    }


    public void carregar() {
        if (HttpUtil.hasConnection(getContext())) {
            new PalavrasFragment.PalavrasAsyncTask().execute();
        } else {
            Toast.makeText(getContext(), "Verifique a conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void carregarPalavras() {
        try {
            HttpURLConnection connection = HttpUtil.openConnection(Constants.IP_SERVIDOR+"ListaDialetosGson?idLingua="+idLingua+"", "GET", true, false);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                JSONArray jsonArray = new JSONArray(FileUtil.bytesToString(inputStream));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Palavra palavra = new Palavra();

                    palavra.setId(jsonObject.getLong("id"));
                    palavra.setPalavra(jsonObject.getString("palavra"));
                    palavra.setTraducao(jsonObject.getString("traducao"));
                    palavra.setAplicacaoFrase(jsonObject.getString("aplicacaoFrase"));
                    palavra.setTraducaoFrase(jsonObject.getString("traducaoFrase"));
                    if(!jsonObject.getString("imagemUrl").equals("none")){
                        palavra.setImagemUrl(jsonObject.getString("imagemUrl"));
                    }
                    if(!jsonObject.getString("audioUrl").equals("none")){
                        palavra.setAudioUrl(jsonObject.getString("audioUrl"));
                    }

                    palavras.add(palavra);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PalavrasAdapter.PalavraOnClickListener onClickPalavra() {
        return new PalavrasAdapter.PalavraOnClickListener() {
            @Override
            public void onClickPalavra(View view, int index) {
                Palavra palavra = palavras.get(index);
                Intent intent = new Intent(getContext(), DetalhePalavraActivity.class);
                intent.putExtra("idPalavra", palavra.getId());
                intent.putExtra("palavra", palavra.getPalavra());
                intent.putExtra("traducao", palavra.getTraducao());
                intent.putExtra("frase", palavra.getAplicacaoFrase());
                intent.putExtra("traducaoFrase", palavra.getTraducaoFrase());
                intent.putExtra("imagemUrl",palavra.getImagemUrl());
                intent.putExtra("audioUrl",palavra.getAudioUrl());
                intent.putExtra("logado", logado);
                intent.putExtra("tipoUsuario",tipoUsuario);
                startActivity(intent);
            }
        };
    }

    class PalavrasAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... strings){
            carregarPalavras();
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            palavrasAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }
}

