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
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;

public class RelatorioPedidoAdapter extends RecyclerView.Adapter<RelatorioPedidoAdapter.MyViewHolder> {

    private List<Movimento> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;

    public RelatorioPedidoAdapter(Context c, List<Movimento> list) {
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RelatorioPedidoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_relatorio_pedido, viewGroup, false);
        RelatorioPedidoAdapter.MyViewHolder mvh = new RelatorioPedidoAdapter.MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioPedidoAdapter.MyViewHolder myViewHolder, int position) {

        Parceiro p = ParceiroDAO.getInstance(context).findByIdAuxiliar("codigoparceiro", fList.get(position).codigoparceiro);

        if (fList.get(position).sincronizado.equals("T")) {
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        } else {
            myViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
        }

        myViewHolder.txtCodigoParceiro.setText(p.codigoparceiro);
        myViewHolder.txtData.setText(Misc.formatDate(fList.get(position).data, "dd/MM/yyyy"));
        myViewHolder.txtDescricao.setText(p.descricao);
        myViewHolder.txtHoraInicio.setText(Misc.formatDate(fList.get(position).datainicio, "HH:mm:ss"));
        if (fList.get(position).datafim != null) {
            myViewHolder.txtHoraFim.setText(Misc.formatDate(fList.get(position).datafim, "HH:mm:ss"));
        }
        myViewHolder.txtTotal.setText("R$ "+Misc.formatMoeda(fList.get(position).totalliquido));
    }

    @Override
    public int getItemCount() {
        if (fList != null) {
            return fList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCodigoParceiro;
        public TextView txtDescricao;
        public TextView txtData;
        public TextView txtHoraInicio;
        public TextView txtHoraFim;
        public TextView txtTotal;
        public FrameLayout frmStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCodigoParceiro = itemView.findViewById(R.id.txt_relatorio_pedido_codigo);
            txtDescricao = itemView.findViewById(R.id.txt_relatorio_pedido_descricao);
            txtData = itemView.findViewById(R.id.txt_relatorio_pedido_data);
            txtHoraInicio = itemView.findViewById(R.id.txt_relatorio_pedido_inicio);
            txtHoraFim = itemView.findViewById(R.id.txt_relatorio_pedido_fim);
            txtTotal = itemView.findViewById(R.id.txt_relatorio_pedido_total);
            frmStatus = itemView.findViewById(R.id.frm_status);
        }
    }
}
