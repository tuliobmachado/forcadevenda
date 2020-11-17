package br.com.informsistemas.forcadevenda.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;

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

        String vCasasQuantidade = String.valueOf(Constants.DTO.registro.casasquantidade);
//                Misc.parseFloatToWatcher(fList.get(position).quantidade, Constants.DTO.registro.casasquantidade);

        if ((Constants.DTO.registro.exibematerialsemsaldo) || ((!Constants.DTO.registro.exibematerialsemsaldo) && (fList.get(position).saldomaterial.floatValue() > 0))) {
            myViewHolder.itemView.setVisibility(View.VISIBLE);
            myViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            myViewHolder.txtDescricao.setText(fList.get(position).descricao);
            myViewHolder.txtCusto.setText("R$ " + Misc.formatMoeda(fList.get(position).totalliquido.floatValue()));
            myViewHolder.txtSaldo.setText("Saldo: " + String.format("%.2f", fList.get(position).saldomaterial) + " | " + fList.get(position).unidadesaida);

            if (fList.get(position).quantidade.floatValue() >= 1) {
                myViewHolder.imgExcluir.setImageResource(R.drawable.ic_remove_red_24dp);
                myViewHolder.imgExcluir.setVisibility(View.VISIBLE);

                if (fList.get(position).quantidade.floatValue() == 1) {
                    myViewHolder.imgExcluir.setVisibility(View.VISIBLE);
                }

                if ((fList.get(position).quantidade.floatValue() >= 2) || !(fList.get(position).quantidade.floatValue() % 1.0f == 0.0f)){
                    myViewHolder.txtQuantidade.setText(String.format("%."+vCasasQuantidade+"f", fList.get(position).quantidade));
                } else {
                    myViewHolder.txtQuantidade.setText("");
                }

                myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_adicionado_24dp);
            } else {
                myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
                myViewHolder.txtQuantidade.setText("");
                myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_gray_24dp);
            }

            myViewHolder.imgSelecionado.startAnimation(getRotateAnimation(excluindo));
        }else{
            myViewHolder.itemView.setVisibility(View.GONE);
            myViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
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
        public RelativeLayout viewItemLista;
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
            viewItemLista = itemView.findViewById(R.id.lyt_recycler_item_lista);
            fOnMaterialListener = onMaterialListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imgExcluir.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BigDecimal quantidade = new BigDecimal(1);
            switch (v.getId()) {
                case R.id.img_excluir:
                    excluindo = true;
                    fOnMaterialListener.onBotaoExcluirClick(getAdapterPosition(), quantidade);
                    break;
                default:
                    excluindo = false;
                    fOnMaterialListener.onMaterialClick(getAdapterPosition(), quantidade);
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
        void onBotaoExcluirClick(int position, BigDecimal quantidade);
        void onMaterialClick(int position, BigDecimal quantidade);
        void onMaterialLongClick(int position);
    }
}
