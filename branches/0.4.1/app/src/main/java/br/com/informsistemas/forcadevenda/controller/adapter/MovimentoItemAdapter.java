package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.MaterialDAO;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;

public class MovimentoItemAdapter extends RecyclerView.Adapter<MovimentoItemAdapter.MyViewHolder> {

    private List<MovimentoItem> fList;
    private List<Integer> selectedIds = new ArrayList<>();
    private LayoutInflater fLayoutInflater;
    private Context context;

    public MovimentoItemAdapter(Context context, List<MovimentoItem> fList) {
        this.fList = fList;
        this.context = context;
        this.fLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_movimento_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Boolean selecionado = false;
        BigDecimal acrescimo;
        BigDecimal desconto;
        BigDecimal totalDesconto;
        BigDecimal totalLiquido;

        desconto = fList.get(position).valordescontoitem;
        acrescimo = fList.get(position).valoricmsfecoepst.add(fList.get(position).valoripi).add(fList.get(position).valoricmssubst).add(fList.get(position).valoracrescimoitem);

        if (desconto.floatValue() > 0){
            totalDesconto = fList.get(position).totalitem.subtract(desconto);
            myViewHolder.txtValorDesconto.setText(Misc.formatMoeda(desconto.floatValue()));
            myViewHolder.txtTotalDesconto.setText(Misc.formatMoeda(totalDesconto.floatValue()));
        }else{
            myViewHolder.layoutDesconto.setVisibility(View.GONE);
        }

        if (acrescimo.floatValue() > 0){
            totalLiquido = fList.get(position).totalitem.add(acrescimo).subtract(desconto);
            myViewHolder.txtTotalAcrescimo.setText(Misc.formatMoeda(acrescimo.floatValue()));
            myViewHolder.txtTotalLiquido.setText(Misc.formatMoeda(totalLiquido.floatValue()));
        }else{
            myViewHolder.layoutAcrescimo.setVisibility(View.GONE);
        }

        Material m = MaterialDAO.getInstance(context).findByIdAuxiliar("codigomaterial", fList.get(position).codigomaterial);

        myViewHolder.txtDescricao.setText(m.descricao);
        myViewHolder.txtUnidade.setText(m.unidadesaida);
        myViewHolder.txtQuantidadeCusto.setText(String.format("%.2f", fList.get(position).quantidade) + " X " + Misc.formatMoeda((fList.get(position).custo.floatValue())));
        myViewHolder.txtTotalItem.setText(Misc.formatMoeda((fList.get(position).totalitem.floatValue())));

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

        public TextView txtDescricao;
        public TextView txtUnidade;
        public TextView txtQuantidadeCusto;
        public TextView txtTotalItem;
        public TextView txtTotalAcrescimo;
        public TextView txtTotalLiquido;
        public TextView txtTotalDesconto;
        public TextView txtValorDesconto;
        public CardView cardView;
        public LinearLayout layoutAcrescimo;
        public LinearLayout layoutDesconto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtUnidade = itemView.findViewById(R.id.txt_unidade);
            txtQuantidadeCusto = itemView.findViewById(R.id.txt_quantidade_custo);
            txtTotalItem = itemView.findViewById(R.id.txt_total_item);
            txtTotalAcrescimo = itemView.findViewById(R.id.txt_total_acrescimo);
            txtTotalLiquido = itemView.findViewById(R.id.txt_total_liquido);
            txtTotalDesconto = itemView.findViewById(R.id.txt_total_desconto);
            txtValorDesconto = itemView.findViewById(R.id.txt_valor_desconto);
            cardView = itemView.findViewById(R.id.card_movimento_item);
            layoutAcrescimo = itemView.findViewById(R.id.layout_acrescimo);
            layoutDesconto = itemView.findViewById(R.id.layout_desconto);
        }
    }
}
