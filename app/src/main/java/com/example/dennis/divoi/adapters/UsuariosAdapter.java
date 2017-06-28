package com.example.dennis.divoi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Palavra;
import com.example.dennis.divoi.model.Usuario;

import java.util.List;

/**
 * Created by dennis on 22/06/17.
 */

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosViewHolder> {

    private Context context;
    private List<Usuario> usuarios;
    private final UsuarioOnClickListener onClickListener;


    public UsuariosAdapter(Context context, List<Usuario> usuarios, UsuarioOnClickListener onClickListener){
        this.context = context;
        this.usuarios = usuarios;
        this.onClickListener = onClickListener;

    }

    @Override
    public UsuariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_usuarios, parent, false);
        UsuariosViewHolder viewHolder = new UsuariosViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final UsuariosViewHolder holder, final int position) {
        Usuario usuario = usuarios.get(position);
        holder.textViewUsuario.setText(usuario.getNome());
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickUsuario(holder.view, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {return this.usuarios != null ? this.usuarios.size() : 0;  }

    public static class UsuariosViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewUsuario;
        private View view;

        public UsuariosViewHolder(View itemView) {
            super(itemView);

            textViewUsuario = (TextView) itemView.findViewById(R.id.text_nome);
        }
    }

    public interface UsuarioOnClickListener{
        public void onClickUsuario(View view, int index);
    }
}
