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

import com.example.dennis.divoi.activitys.DetalheLinguaActivity;
import com.example.dennis.divoi.activitys.ListaPalavrasActivity;
import com.example.dennis.divoi.adapters.LinguasAdapter;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.R;
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
 * Created by dennis on 10/04/17.
 */

public class LinguasFragment extends Fragment {

    public static final String TAG = ".fragment.LinguasFragment";
    private RecyclerView recyclerView;
    private LinguasAdapter linguasAdapter;
    private List<Lingua> linguas = new ArrayList<>();
    private ProgressDialog progressDialog;
    private boolean logado = false;
    private String tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linguas, container, false);

        if(getArguments().getBoolean("logado") == true){
        logado = getArguments().getBoolean("logado");
        tipoUsuario = getArguments().getString("tipoUsuario");}

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando Dados");
        progressDialog.setMessage("Aguarde um momento...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_linguas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        linguasAdapter = new LinguasAdapter(getContext(), linguas, onClickLingua(),onLongClickListener());
        recyclerView.setAdapter(linguasAdapter);

        carregar();

        return view;
    }

//    public void buscar(String s) {
//        if(s == null || s.trim().equals("")){
//            limparBusca();
//            return;
//        }
//        List<Lingua> linguasEncontradas = linguas;
//        for(int i = linguasEncontradas.size()-1; i >= 0; i--){
//            Lingua lingua = linguasEncontradas.get(i);
//            if(! lingua.getNome().toUpperCase().contains(s.toUpperCase())){
//                linguasEncontradas.remove(lingua);
//            }
//        }
//        linguasAdapter = new LinguasAdapter(getContext(), linguasEncontradas, onClickLingua());
//        recyclerView.setAdapter(linguasAdapter);
//    }
//    public void limparBusca(){
//        carregar();
//    }

    public void carregar() {
        if (HttpUtil.hasConnection(getContext())) {
            new LinguasAsyncTask().execute();
        } else {
            Toast.makeText(getContext(), "Verifique a conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void carregarLinguas(){
        try{
            HttpURLConnection connection = HttpUtil.openConnection(Constants.IP_SERVIDOR+"ListaLinguasGson", "GET", true, false);
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                JSONArray jsonArray = new JSONArray(FileUtil.bytesToString(inputStream));
                for(int i = 0; i< jsonArray.length(); i++){
                    Lingua lingua = new Lingua();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    lingua.setId(Long.parseLong(jsonObject.getString("id")));
                    lingua.setNome(jsonObject.getString("nome"));
                    lingua.setPovo(jsonObject.getString("povo"));
                    lingua.setLocalizacao(jsonObject.getString("localizacao"));
                    lingua.setDescricao(jsonObject.getString("descricao"));

                    linguas.add(lingua);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private LinguasAdapter.LinguaOnClickListener onClickLingua(){
        return new LinguasAdapter.LinguaOnClickListener(){
            @Override
            public void onClickLingua(View view, int index){
                Lingua lingua = linguas.get(index);
//                Intent intent = new Intent(getContext(), DetalheLinguaActivity.class);
                Intent intent = new Intent(getContext(), ListaPalavrasActivity.class);
                intent.putExtra("idLingua", lingua.getId());
                Log.d("ID - LINGUAS", String.valueOf(lingua.getId()));
                intent.putExtra("nome", lingua.getNome());
                intent.putExtra("povo", lingua.getPovo());
                intent.putExtra("localizacao", lingua.getLocalizacao());
                intent.putExtra("descricao", lingua.getDescricao());
                intent.putExtra("logado",logado);
                intent.putExtra("tipoUsuario", tipoUsuario);
                startActivity(intent);
            }
        };
    }

    private LinguasAdapter.LinguaOnLongClickListener onLongClickListener(){
        return new LinguasAdapter.LinguaOnLongClickListener(){

            @Override
            public void onLongClickLingua(View view, int index) {
                Lingua lingua = linguas.get(index);
                Intent intent = new Intent(getContext(), DetalheLinguaActivity.class);
               // Intent intent = new Intent(getContext(), ListaPalavrasActivity.class);
                intent.putExtra("idLingua", lingua.getId());
                Log.d("ID - LINGUAS", String.valueOf(lingua.getId()));
                intent.putExtra("nome", lingua.getNome());
                intent.putExtra("povo", lingua.getPovo());
                intent.putExtra("localizacao", lingua.getLocalizacao());
                intent.putExtra("descricao", lingua.getDescricao());
                startActivity(intent);
            }
        };
    }




    class LinguasAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... strings){
            carregarLinguas();
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            linguasAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }


}
