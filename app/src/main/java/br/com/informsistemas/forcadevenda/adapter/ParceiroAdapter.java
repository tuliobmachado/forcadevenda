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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroVencimentoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
import br.com.informsistemas.forcadevenda.model.utils.CPFCNPJMask;
import br.com.informsistemas.forcadevenda.viewholder.ParceiroViewHolder;

public class ParceiroAdapter extends RecyclerView.Adapter<ParceiroViewHolder> {

    private List<Parceiro> fList;
    private List<Integer> selectedIds = new ArrayList<>();
    private LayoutInflater fLayoutInflater;
    private Context context;
    private ParceiroViewHolder.onParceiroListener vOnParceiroListener;

    public ParceiroAdapter(Context context, List<Parceiro> fList, ParceiroViewHolder.onParceiroListener onParceiroListener) {
        this.fList = fList;
        this.context = context;
        this.vOnParceiroListener = onParceiroListener;
        this.fLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ParceiroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_parceiro, viewGroup, false);
        return new ParceiroViewHolder(v, vOnParceiroListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ParceiroViewHolder parceiroViewHolder, int position) {
        Boolean selecionado = false;
        List<ParceiroVencimento> parceiroVencimentoList = ParceiroVencimentoDAO.getInstance(context).findAllVencimentoByCodigoParceiro(fList.get(position).codigoparceiro);

        if (parceiroVencimentoList.size() == 0){
            parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        }else{
            if (parceiroVencimentoList.get(0).status.equals("Vencido")){
                parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
            }else{
                parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
            }

            fList.get(position).statusvencimento = parceiroVencimentoList.get(0).status;
        }

        parceiroViewHolder.txtCodigo.setText(fList.get(position).codigoparceiro);
        parceiroViewHolder.txtDescricao.setText(fList.get(position).descricao);
        parceiroViewHolder.txtNomeFantasia.setText(fList.get(position).nomefantasia);
        parceiroViewHolder.txtCPFCGC.setText(CPFCNPJMask.getMask(fList.get(position).cpfcgc));

        if (fList.get(position).longitude != 0 && fList.get(position).latitude != 0){
            parceiroViewHolder.imgLocalizacao.setVisibility(View.VISIBLE);
        }else{
            parceiroViewHolder.imgLocalizacao.setVisibility(View.GONE);
        }

        for (int i : selectedIds){
            if (fList.get(position).id == i){
                selecionado = true;
            }
        }

        if (selecionado){
            parceiroViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.cardSelecionado)));
        }else{
            parceiroViewHolder.cardView.setForeground(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
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
}
