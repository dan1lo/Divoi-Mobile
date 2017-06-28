package com.example.dennis.divoi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.activitys.DetalheUsuarioActivity;
import com.example.dennis.divoi.adapters.UsuariosAdapter;
import com.example.dennis.divoi.model.Usuario;
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
 * Created by dennis on 22/06/17.
 */

public class UsuariosFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuariosAdapter usuariosAdapter;
    private List<Usuario> usuarios = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando Dados");
        progressDialog.setMessage("Aguarde um momento...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_usuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        usuariosAdapter = new UsuariosAdapter(getContext(), usuarios, onClickUsuario());
        recyclerView.setAdapter(usuariosAdapter);

        carregar();

        return view;
    }

    public void carregar() {
        if (HttpUtil.hasConnection(getContext())) {
            new UsuariosFragment.UsuariosAsyncTask().execute();
        } else {
            Toast.makeText(getContext(), "Verifique a conexão com a internet", Toast.LENGTH_SHORT).show();
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
            usuariosAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
            super.onPostExecute(v);

        }
    }

    private UsuariosAdapter.UsuarioOnClickListener onClickUsuario() {
        return new UsuariosAdapter.UsuarioOnClickListener() {
            @Override
            public void onClickUsuario(View view, int index) {
                Usuario usuario = usuarios.get(index);
                Intent intent = new Intent(getContext(), DetalheUsuarioActivity.class);
                intent.putExtra("id", usuario.getId());
                intent.putExtra("nome", usuario.getNome());
                intent.putExtra("email", usuario.getEmail());
                intent.putExtra("senha", usuario.getSenha());
                intent.putExtra("tipo", usuario.getTipo());
                startActivity(intent);
            }
        };
    }

}
