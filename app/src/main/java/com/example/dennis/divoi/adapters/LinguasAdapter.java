package com.example.dennis.divoi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennis.divoi.activitys.ListaPalavrasActivity;
import com.example.dennis.divoi.model.Lingua;
import com.example.dennis.divoi.R;

import java.util.List;

/**
 * Created by dennis on 10/04/17.
 */

public class LinguasAdapter extends RecyclerView.Adapter<LinguasAdapter.LinguasViewHolder> {
    private Context context;
    private List<Lingua> linguas;
    private final LinguaOnClickListener onClickListener;
    private final LinguaOnLongClickListener onLongClickListener;

    public LinguasAdapter(Context context, List<Lingua> linguas, LinguaOnClickListener onClickListener, LinguaOnLongClickListener onLongClickListener) {
        this.context = context;
        this.linguas = linguas;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public LinguasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_linguas, parent, false);
        LinguasViewHolder viewHolder = new LinguasViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LinguasViewHolder holder, final int position) {
        Lingua lingua = linguas.get(position);
        holder.textViewNome.setText(lingua.getNome());
        holder.textViewLocalizacao.setText(lingua.getLocalizacao());
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickLingua(holder.view, position);
                }
            });
        }
        if (onLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onLongClickListener.onLongClickLingua(holder.view, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.linguas != null ? this.linguas.size() : 0;
    }


    public static class LinguasViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNome;
        public TextView textViewLocalizacao;
        private View view;

        public LinguasViewHolder(View itemView) {
            super(itemView);

            textViewNome = (TextView) itemView.findViewById(R.id.text_nome);
            textViewLocalizacao = (TextView) itemView.findViewById(R.id.text_localizacao);

        }
    }

    public interface LinguaOnClickListener {
        public void onClickLingua(View view, int index);

    }

    public interface LinguaOnLongClickListener {
        public void onLongClickLingua(View view, int index);
    }
}
