package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.MaterialEstadoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MaterialSaldoDAO;
import br.com.informsistemas.forcadevenda.model.dao.TabelaPrecoItemDAO;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.TabelaPrecoItem;
import br.com.informsistemas.forcadevenda.model.utils.ItemClickListener;
import br.com.informsistemas.forcadevenda.model.utils.RecyclerItemClickListener;

public class MaterialSearchAdapter extends RecyclerView.Adapter<MaterialSearchAdapter.MyViewHolder>  {

    private List<Material> fList;
    private LayoutInflater fLayoutInflater;
    private Context context;
    private OnMaterialListener fOnMaterialListener;
    private boolean excluindo = false;

    public MaterialSearchAdapter(Context c, List<Material> list, OnMaterialListener onMaterialListener){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fOnMaterialListener = onMaterialListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_material, viewGroup, false);
        return new MyViewHolder(v, fOnMaterialListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        myViewHolder.txtDescricao.setText(fList.get(position).descricao);
        myViewHolder.txtCusto.setText("R$ "+Misc.formatMoeda(fList.get(position).totalliquido));
        myViewHolder.txtSaldo.setText("Saldo: "+ String.format("%.2f", fList.get(position).saldomaterial) + " | " + fList.get(position).unidadesaida);

        if (fList.get(position).quantidade >= 1){
            myViewHolder.imgExcluir.setImageResource(R.drawable.ic_remove_red_24dp);
            myViewHolder.imgExcluir.setVisibility(View.VISIBLE);

            if (fList.get(position).quantidade == 1){
                myViewHolder.imgExcluir.setVisibility(View.VISIBLE);
            }

            if (fList.get(position).quantidade >= 2){
                myViewHolder.txtQuantidade.setText(String.format("%.0f", fList.get(position).quantidade));
            }else{
                myViewHolder.txtQuantidade.setText("");
            }

            myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_adicionado_24dp);
        }else{
            myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
            myViewHolder.txtQuantidade.setText("");
            myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_gray_24dp);
        }

        myViewHolder.imgSelecionado.startAnimation(getRotateAnimation(excluindo));
    }

    @Override
    public int getItemCount() {
        if (fList != null){
            return fList.size();
        }else{
            return 0;
        }
    }

    private RotateAnimation getRotateAnimation(boolean excluindo){
        int fromDregress = 0;
        int toDegress = 0;

        if (excluindo){
            fromDregress = 180;
            toDegress = 0;
        }else {
            fromDregress = 0;
            toDegress = 180;
        }

        RotateAnimation animation = new RotateAnimation(fromDregress, toDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);

        return animation;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView txtDescricao;
        public TextView txtCusto;
        public TextView txtQuantidade;
        public TextView txtAcao;
        public TextView txtSaldo;
        public ImageView imgSelecionado;
        public ImageView imgExcluir;
        public OnMaterialListener fOnMaterialListener;

        public MyViewHolder(@NonNull View itemView, OnMaterialListener onMaterialListener) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtCusto = itemView.findViewById(R.id.txt_custo);
            txtQuantidade = itemView.findViewById(R.id.txt_quantidade);
            txtAcao = itemView.findViewById(R.id.txt_acao);
            txtSaldo = itemView.findViewById(R.id.txt_saldo);
            imgSelecionado = itemView.findViewById(R.id.img_adicionar);
            imgExcluir = itemView.findViewById(R.id.img_excluir);
            fOnMaterialListener = onMaterialListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imgExcluir.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_excluir:
                    excluindo = true;
                    fOnMaterialListener.onBotaoExcluirClick(getAdapterPosition());
                    break;
                default:
                    excluindo = false;
                    fOnMaterialListener.onMaterialClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            fOnMaterialListener.onMaterialLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnMaterialListener{
        void onBotaoExcluirClick(int position);
        void onMaterialClick(int position);
        void onMaterialLongClick(int position);
    }
}
