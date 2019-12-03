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
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroVencimentoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
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
        List<ParceiroVencimento> parceiroVencimentoList = ParceiroVencimentoDAO.getInstance(context).findAllVencimentoByCodigoParceiro(fList.get(position).codigoparceiro);

        if (parceiroVencimentoList.size() == 0){
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        }else{
            if (parceiroVencimentoList.get(0).status.equals("Vencido")){
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
            }else{
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
            }

            fList.get(position).statusvencimento = parceiroVencimentoList.get(0).status;
        }

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
        public FrameLayout frmStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodigo = itemView.findViewById(R.id.txt_codigo);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtCPFCGC = itemView.findViewById(R.id.txt_cpfcgc);
            txtNomeFantasia = itemView.findViewById(R.id.txt_nome_fantasia);
            cardView = itemView.findViewById(R.id.card_parceiro);
            frmStatus = itemView.findViewById(R.id.frm_status);
        }
    }
}
