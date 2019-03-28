package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.utils.ItemClickListener;

public class PagamentoSearchAdapter extends RecyclerView.Adapter<PagamentoSearchAdapter.MyViewHolder>  {

    private List<FormaPagamento> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public PagamentoSearchAdapter(Context c, List<FormaPagamento> list){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_pagamento, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.txtDescricao.setText(fList.get(position).descricao);
    }

    @Override
    public int getItemCount() {
        if (fList != null){
            return fList.size();
        }else{
            return 0;
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtDescricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txt_descricao);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
