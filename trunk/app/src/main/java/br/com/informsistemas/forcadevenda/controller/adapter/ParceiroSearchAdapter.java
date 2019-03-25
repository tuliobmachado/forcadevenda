package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroVencimentoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
import br.com.informsistemas.forcadevenda.model.utils.CPFCNPJMask;
import br.com.informsistemas.forcadevenda.model.utils.ItemClickListener;

public class ParceiroSearchAdapter extends RecyclerView.Adapter<ParceiroSearchAdapter.MyViewHolder>  {

    private List<Parceiro> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;
    private ItemClickListener itemClickListener;

    public ParceiroSearchAdapter(Context c, List<Parceiro> list){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        List<ParceiroVencimento> parceiroVencimentoList = ParceiroVencimentoDAO.getInstance(context).findAllVencimentoByCodigoParceiro(fList.get(position).codigoparceiro);

        if (parceiroVencimentoList.size() == 0){
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        }else{
            if (parceiroVencimentoList.get(0).status.equals("Vencido")){
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
            }else{
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
            }
        }

        myViewHolder.txtCodigo.setText(fList.get(position).codigoparceiro);
        myViewHolder.txtDescricao.setText(fList.get(position).descricao);
        myViewHolder.txtNomeFantasia.setText(fList.get(position).nomefantasia);
        myViewHolder.txtCPFCGC.setText(CPFCNPJMask.getMask(fList.get(position).cpfcgc));
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

        public TextView txtCodigo;
        public TextView txtDescricao;
        public TextView txtNomeFantasia;
        public TextView txtCPFCGC;
        public FrameLayout frmStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodigo = itemView.findViewById(R.id.txt_codigo);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtNomeFantasia = itemView.findViewById(R.id.txt_nome_fantasia);
            txtCPFCGC = itemView.findViewById(R.id.txt_cpfcgc);
            frmStatus = itemView.findViewById(R.id.frm_status);

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
