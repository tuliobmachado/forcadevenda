package br.com.informsistemas.forcadevenda.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroVencimentoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
import br.com.informsistemas.forcadevenda.model.utils.CPFCNPJMask;
import br.com.informsistemas.forcadevenda.interfaces.ItemClickListener;
import br.com.informsistemas.forcadevenda.viewholder.ParceiroViewHolder;

public class ParceiroConsultaAdapter extends RecyclerView.Adapter<ParceiroViewHolder>  {

    private List<Parceiro> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;
    private ParceiroViewHolder.onParceiroListener vOnParceiroListener;

    public ParceiroConsultaAdapter(Context c, List<Parceiro> list, ParceiroViewHolder.onParceiroListener vOnParceiroListener){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vOnParceiroListener= vOnParceiroListener;
    }

    @NonNull
    @Override
    public ParceiroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_parceiro, viewGroup, false);
        return new ParceiroViewHolder(v, vOnParceiroListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ParceiroViewHolder parceiroViewHolder, int position) {
        List<ParceiroVencimento> parceiroVencimentoList = ParceiroVencimentoDAO.getInstance(context).findAllVencimentoByCodigoParceiro(fList.get(position).codigoparceiro);

        if (parceiroVencimentoList.size() == 0){
            parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movSincronizado));
        }else{
            if (parceiroVencimentoList.get(0).status.equals("Vencido")){
                parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.movNaoSincronizado));
            }else{
                parceiroViewHolder.frmStatus.setBackgroundColor(context.getResources().getColor(R.color.parceiroAVencer));
            }
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
    }

    @Override
    public int getItemCount() {
        if (fList != null){
            return fList.size();
        }else{
            return 0;
        }
    }
}
