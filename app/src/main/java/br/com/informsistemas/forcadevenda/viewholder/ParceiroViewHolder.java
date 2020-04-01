package br.com.informsistemas.forcadevenda.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import br.com.informsistemas.forcadevenda.R;

public class ParceiroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCodigo;
    public TextView txtDescricao;
    public TextView txtNomeFantasia;
    public TextView txtCPFCGC;
    public ImageView imgLocalizacao;
    public CardView cardView;
    public FrameLayout frmStatus;
    private onParceiroListener vOnParceiroListener;

    public ParceiroViewHolder(View itemView, onParceiroListener onParceiroListener){
        super(itemView);

        txtCodigo = itemView.findViewById(R.id.txt_codigo_parceiro);
        txtDescricao = itemView.findViewById(R.id.txt_descricao);
        txtNomeFantasia = itemView.findViewById(R.id.txt_nome_fantasia);
        txtCPFCGC = itemView.findViewById(R.id.txt_cpfcgc);
        imgLocalizacao = itemView.findViewById(R.id.img_localizacao);
        frmStatus = itemView.findViewById(R.id.frm_status);
        cardView = itemView.findViewById(R.id.card_parceiro);
        vOnParceiroListener = onParceiroListener;

        itemView.setOnClickListener(this);
        imgLocalizacao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_localizacao:
                vOnParceiroListener.onLocalizacaoClick(getAdapterPosition());
                break;
            default:
                vOnParceiroListener.onItemClick(getAdapterPosition());
                break;
        }
    }

    public interface onParceiroListener{
        void onItemClick(int position);
        void onLocalizacaoClick(int position);
    }
}
