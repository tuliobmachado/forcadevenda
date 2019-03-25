package br.com.informsistemas.forcadevenda.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class MaterialSearchAdapter extends RecyclerView.Adapter<MaterialSearchAdapter.MyViewHolder>  {

    private List<MovimentoItem> fListSelecionado;
    private List<Material> fList;
    private List<Material> fListSearch;
    private LayoutInflater fLayoutInflater;
    private Context context;
    private ItemClickListener itemClickListener;
    private boolean excluindo = false;
    TranslateAnimation moveLefttoRight;

    public MaterialSearchAdapter(Context c, List<Material> list){
        this.fList = list;
        this.context = c;
        this.fLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = fLayoutInflater.inflate(R.layout.recycler_item_material, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
        int fromDregress = 0;
        int toDegress = 0;
        float QuantidadeSelecionada = 0;
        boolean zerarList = false;
        myViewHolder.txtAcao.setVisibility(View.GONE);
        moveLefttoRight = new TranslateAnimation(150, -40, 0, 0);
        moveLefttoRight.setDuration(1000);
        moveLefttoRight.setFillAfter(true);
        zeraValores(position);

        QuantidadeSelecionada = fList.get(position).quantidade;

        MaterialSaldo materialSaldo = MaterialSaldoDAO.getInstance(context).findByIdAuxiliar("codigomaterial", fList.get(position).codigomaterial);

        fList.get(position).precovenda1 = CalculoClass.getPrecoVenda(context, fList.get(position));
        fList.get(position).quantidade = 1;
        CalculoClass.getTributos(context, fList.get(position));
        fList.get(position).quantidade = QuantidadeSelecionada;

        myViewHolder.txtDescricao.setText(fList.get(position).descricao);
        myViewHolder.txtCusto.setText("R$ "+Misc.formatMoeda(fList.get(position).totalliquido));
        myViewHolder.txtSaldo.setText("Saldo: "+ String.format("%.2f", materialSaldo.saldo / fList.get(position).fator)+ " | " + fList.get(position).unidadesaida);

        if (fListSelecionado != null) {
            for (int i = 0; i < fListSelecionado.size(); i++) {
                if (fListSelecionado.get(i).codigomaterial.equals(fList.get(position).codigomaterial)) {
                    fList.get(position).quantidade = fListSelecionado.get(i).quantidade;
                    fListSelecionado.remove(i);
                }
            }

            if (fListSelecionado.size() == 0){
                zerarList = true;
            }
        }

        if (fListSearch != null) {
            for (int i = 0; i < fListSearch.size(); i++) {
                if (fListSearch.get(i).codigomaterial.equals(fList.get(position).codigomaterial)) {
                    fList.get(position).quantidade = fListSearch.get(i).quantidade;
                    fListSearch.remove(i);
                }
            }

            if (fListSearch.size() == 0){
                zerarList = true;
            }
        }

        if (fList.get(position).quantidade >= 1){

            if ((fList.get(position).quantidade == 1) && (fListSelecionado == null)) {
                if (!excluindo){
//                    myViewHolder.txtAcao.setTextColor(context.getResources().getColor(R.color.color_adicionado));
//                    myViewHolder.txtAcao.setText("Adicionado");
//
//                    myViewHolder.txtAcao.startAnimation(moveLefttoRight);
                    moveLefttoRight.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
//                            myViewHolder.txtAcao.setVisibility(View.VISIBLE);
                            myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
//                            myViewHolder.txtAcao.clearAnimation();
//                            myViewHolder.txtAcao.setVisibility(View.INVISIBLE);
//                            myViewHolder.txtAcao.setText("");
                            myViewHolder.imgExcluir.setVisibility(View.VISIBLE);
                            myViewHolder.imgExcluir.setImageResource(R.drawable.ic_remove_red_24dp);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }

            if (fList.get(position).quantidade >= 2){
                myViewHolder.txtQuantidade.setText(String.format("%.0f", fList.get(position).quantidade));
                myViewHolder.imgExcluir.setImageResource(R.drawable.ic_remove_red_24dp);
                myViewHolder.imgExcluir.setVisibility(View.VISIBLE);
//                myViewHolder.txtAcao.clearAnimation();
//                myViewHolder.txtAcao.setVisibility(View.INVISIBLE);
//                myViewHolder.txtAcao.setText("");
            }else{
                myViewHolder.txtQuantidade.setText("");
                myViewHolder.imgExcluir.setImageResource(R.drawable.ic_clear_red_24dp);
                myViewHolder.imgExcluir.setVisibility(View.VISIBLE);
            }

            myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_adicionado_24dp);
        }else{

            if (excluindo){
//                myViewHolder.txtAcao.setTextColor(context.getResources().getColor(R.color.color_excluido));
//                myViewHolder.txtAcao.setText("Excluído");

                myViewHolder.txtAcao.startAnimation(moveLefttoRight);
                moveLefttoRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
//                        myViewHolder.txtAcao.setVisibility(View.VISIBLE);
                        myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        myViewHolder.txtAcao.clearAnimation();
//                        myViewHolder.txtAcao.setText("");
//                        myViewHolder.txtAcao.setVisibility(View.INVISIBLE);
                        myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            myViewHolder.imgExcluir.setVisibility(View.INVISIBLE);
            myViewHolder.txtQuantidade.setText("");
            myViewHolder.imgSelecionado.setImageResource(R.drawable.ic_add_circle_gray_24dp);
        }

        if (excluindo){
            fromDregress = 180;
            toDegress = 0;
        }else {
            fromDregress = 0;
            toDegress = 180;
        }

        RotateAnimation animation = new RotateAnimation(fromDregress, toDegress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        myViewHolder.imgSelecionado.startAnimation(animation);

        if (zerarList){
            fListSelecionado = null;
            fListSearch = null;
        }
    }

    private void zeraValores(int position){
        fList.get(position).baseicms = 0;
        fList.get(position).icms = 0;
        fList.get(position).valoricms = 0;
        fList.get(position).baseicmssubst = 0;
        fList.get(position).icmssubst = 0;
        fList.get(position).valoricmssubst = 0;
        fList.get(position).ipi = 0;
        fList.get(position).valoripi = 0;
        fList.get(position).margemsubstituicao = 0;
        fList.get(position).icmsfecoep = 0;
        fList.get(position).valoricmsfecoep = 0;
        fList.get(position).icmsfecoepst = 0;
        fList.get(position).valoricmsfecoepst = 0;
        fList.get(position).totalliquido = 0;
        fList.get(position).pautafiscal = 0;
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

    public void setfListSelecionado(List<MovimentoItem> fListSelecionado) {
        this.fListSelecionado = fListSelecionado;
    }

    public void setfListSearch(List<Material> fListSearch){
        this.fListSearch = fListSearch;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtDescricao;
        public TextView txtCusto;
        public TextView txtQuantidade;
        public TextView txtAcao;
        public TextView txtSaldo;
        public ImageView imgSelecionado;
        public ImageView imgExcluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtCusto = itemView.findViewById(R.id.txt_custo);
            txtQuantidade = itemView.findViewById(R.id.txt_quantidade);
            txtAcao = itemView.findViewById(R.id.txt_acao);
            txtSaldo = itemView.findViewById(R.id.txt_saldo);
            imgSelecionado = itemView.findViewById(R.id.img_adicionar);
            imgExcluir = itemView.findViewById(R.id.img_excluir);

            itemView.setOnClickListener(this);
            imgExcluir.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {

                switch (v.getId()) {
                    case R.id.img_excluir:
                        excluindo = true;
                        break;
                    default:
                        excluindo = false;
                        break;
                }

                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
