package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;

public class ParceiroVencimentoAdapter extends RecyclerView.Adapter<ParceiroVencimentoAdapter.MyViewHolder>  {

    private List<ParceiroVencimento> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;

    public ParceiroVencimentoAdapter(Context c, List<ParceiroVencimento> list){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ParceiroVencimentoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_parceiro_vencimento, viewGroup, false);
        ParceiroVencimentoAdapter.MyViewHolder mvh = new ParceiroVencimentoAdapter.MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParceiroVencimentoAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.txtValorNumeroLancamento.setText("Nº "+fList.get(position).numerolancamento);
        myViewHolder.txtValorDataEmissao.setText(Misc.formatDate(fList.get(position).dataemissao, "dd/MM/yyyy"));
        myViewHolder.txtValorDataVencimento.setText(Misc.formatDate(fList.get(position).datavencimento, "dd/MM/yyyy"));
        myViewHolder.txtValorDocumento.setText("R$ "+Misc.formatMoeda(fList.get(position).valordocumento));
        myViewHolder.txtValorEmAberto.setText("R$ "+Misc.formatMoeda(fList.get(position).valoremaberto));

        if (fList.get(position).status.equals("Vencido")){
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
        }else{
                myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
        }
    }

    @Override
    public int getItemCount() {
        if (fList != null){
            return fList.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtValorNumeroLancamento;
        public TextView txtValorDataVencimento;
        public TextView txtValorDataEmissao;
        public TextView txtValorDocumento;
        public TextView txtValorEmAberto;
        public FrameLayout frmStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtValorNumeroLancamento = itemView.findViewById(R.id.numero_lancamento_valor);
            txtValorDataEmissao = itemView.findViewById(R.id.data_emissao_valor);
            txtValorDataVencimento = itemView.findViewById(R.id.data_vencimento_valor);
            txtValorDocumento = itemView.findViewById(R.id.valor_documento_valor);
            txtValorEmAberto = itemView.findViewById(R.id.valor_documento_aberto_valor);
            frmStatus = itemView.findViewById(R.id.frm_status);
        }
    }
}
