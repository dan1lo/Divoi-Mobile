package com.example.dennis.divoi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dennis.divoi.R;
import com.example.dennis.divoi.model.Palavra;

import java.util.List;

/**
 * Created by dennis on 18/04/17.
 */

public class PalavrasAdapter extends RecyclerView.Adapter<PalavrasAdapter.PalavrasViewHolder> {


    private Context context;
    private List<Palavra> palavras;
    private final PalavraOnClickListener onClickListener;

    public PalavrasAdapter(Context context, List<Palavra> palavras, PalavraOnClickListener onClickListener){
        this.context = context;
        this.palavras = palavras;
        this.onClickListener = onClickListener;

    }


    @Override
    public PalavrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_palavras, parent, false);
        PalavrasViewHolder viewHolder = new PalavrasViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PalavrasViewHolder holder, final int position) {
        Palavra palavra = palavras.get(position);
        holder.textViewPalavra.setText(palavra.getPalavra());
        if(onClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    onClickListener.onClickPalavra(holder.view,  position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {return this.palavras != null ? this.palavras.size() : 0;  }

    public static class PalavrasViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewPalavra;
        private View view;

        public PalavrasViewHolder(View itemView) {
            super(itemView);

            textViewPalavra = (TextView) itemView.findViewById(R.id.text_palavra);
        }
    }


    public interface PalavraOnClickListener{
        public void onClickPalavra(View view, int index);
    }


}
