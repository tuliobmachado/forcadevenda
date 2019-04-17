package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.utils.CPFCNPJMask;

public class ParceiroAdapter extends RecyclerView.Adapter<ParceiroAdapter.MyViewHolder> {

    private List<Parceiro> fList;
    private List<Integer> selectedIds = new ArrayList<>();
    private LayoutInflater fLayoutInflater;
    private Context context;

    public ParceiroAdapter(Context context, List<Parceiro> fList) {
        this.fList = fList;
        this.context = context;
        this.fLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_parceiro, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Boolean selecionado = false;

        myViewHolder.txtCodigo.setText(fList.get(position).codigoparceiro);
        myViewHolder.txtDescricao.setText(fList.get(position).descricao);
        myViewHolder.txtNomeFantasia.setText(fList.get(position).nomefantasia);
        myViewHolder.txtCPFCGC.setText(CPFCNPJMask.getMask(fList.get(position).cpfcgc));

        for (int i : selectedIds){
            if (fList.get(position).id == i){
                selecionado = true;
            }
        }

        if (selecionado){
            myViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.cardSelecionado)));
        }else{
            myViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        if (fList != null) {
            return fList.size();
        }else
            return 0;
    }

    public void setSelectedIds(List<Integer> selectedIds){
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCodigo;
        public TextView txtDescricao;
        public TextView txtCPFCGC;
        public TextView txtNomeFantasia;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodigo = itemView.findViewById(R.id.txt_codigo);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtCPFCGC = itemView.findViewById(R.id.txt_cpfcgc);
            txtNomeFantasia = itemView.findViewById(R.id.txt_nome_fantasia);
            cardView = itemView.findViewById(R.id.card_parceiro);
        }
    }
}
