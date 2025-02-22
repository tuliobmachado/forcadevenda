package br.com.informsistemas.forcadevenda.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;

public class MovimentoAdapter extends RecyclerView.Adapter<MovimentoAdapter.MyViewHolder> {

    private List<Movimento> fList;
    private List<Integer> selectedIds = new ArrayList<>();
    private LayoutInflater fLayoutInflater;
    private Context context;

    public MovimentoAdapter(Context context, List<Movimento> fList) {
        this.fList = fList;
        this.context = context;
        this.fLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_movimento, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Boolean selecionado = false;
        Parceiro p = ParceiroDAO.getInstance(context).findByIdAuxiliar("codigoparceiro", fList.get(position).codigoparceiro);

        if (fList.get(position).sincronizado.equals("T")) {
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        } else if (fList.get(position).sincronizado.equals("P")) {
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
        } else {
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
        }

        if (p == null){
            myViewHolder.txtCodigoParceiro.setText(fList.get(position).codigoparceiro);
            myViewHolder.txtDescricao.setText(fList.get(position).descricaoparceiro);
        }else {
            myViewHolder.txtCodigoParceiro.setText(p.codigoparceiro);
            myViewHolder.txtDescricao.setText(p.descricao);
        }

        myViewHolder.txtTotalLiquido.setText(Misc.formatMoeda(fList.get(position).totalliquido.floatValue()));

        for (int i : selectedIds) {
            if (fList.get(position).id == i) {
                selecionado = true;
            }
        }

        if (selecionado) {
            myViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.cardSelecionado)));
        } else {
            myViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        if (fList != null) {
            return fList.size();
        } else
            return 0;
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCodigoParceiro;
        public TextView txtDescricao;
        public TextView txtTotalLiquido;
        public CardView cardView;
        public FrameLayout frmStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodigoParceiro = itemView.findViewById(R.id.txt_codigo_parceiro);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtTotalLiquido = itemView.findViewById(R.id.txt_total_liquido);
            cardView = itemView.findViewById(R.id.card_movimento);
            frmStatus = itemView.findViewById(R.id.frm_status);
        }
    }
}
