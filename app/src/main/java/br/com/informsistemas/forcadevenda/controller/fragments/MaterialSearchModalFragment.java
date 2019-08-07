package br.com.informsistemas.forcadevenda.controller.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;

public class MaterialSearchModalFragment extends DialogFragment {

    private TextView txtDescricao;
    private TextView txtSaldo;
    private TextView txtPreco;
    private TextView txtAcrescimoValor;
    private TextView txtAcrescimoPorcentagem;
    private EditText edtQuantidade;
    private EditText edtAcrescimoValor;
    private EditText edtAcrescimoPorcentagem;
    private LinearLayout layoutAcrescimo;

    private boolean possuiQuantidade;
    private int position;

    public static MaterialSearchModalFragment newInstance(){
        MaterialSearchModalFragment frag = new MaterialSearchModalFragment();
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogDefault);
        builder.setTitle("Material");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_modal_search_material, null);

        txtDescricao = view.findViewById(R.id.txt_descricao);
        txtSaldo = view.findViewById(R.id.txt_saldo);
        txtPreco = view.findViewById(R.id.txt_preco_venda);
        txtAcrescimoValor = view.findViewById(R.id.txt_acrescimo_valor);
        txtAcrescimoPorcentagem = view.findViewById(R.id.txt_acrescimo_porcentagem);
        edtQuantidade = view.findViewById(R.id.edt_quantidade);
        edtAcrescimoValor = view.findViewById(R.id.edt_acrescimo_valor);
        edtAcrescimoPorcentagem = view.findViewById(R.id.edt_acrescimo_porcentagem);
        layoutAcrescimo = view.findViewById(R.id.layout_acrescimo);

        onExibeAcrescimo();

        position = getArguments().getInt("position");
        final Material material = (Material) getArguments().getSerializable("material");

        txtDescricao.setText(material.descricao);
        txtSaldo.setText(String.format("%.2f", material.saldomaterial) + " | " + material.unidadesaida);
        txtPreco.setText("R$ "+ Misc.formatMoeda(material.totalliquido));

        if (material.quantidade == 0) {
            possuiQuantidade = false;
            edtQuantidade.setText("");
        }else{
            possuiQuantidade = true;
            edtQuantidade.setText(Float.toString(material.quantidade));
        }

        builder.setView(view);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtQuantidade.getText().toString().equals("")){
                    edtQuantidade.setText("0");
                }

                if ((!possuiQuantidade && Float.parseFloat(edtQuantidade.getText().toString()) == 0) ||
                    (material.quantidade == Float.parseFloat(edtQuantidade.getText().toString()))){
                    dialog.dismiss();
                }else {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getQuantidade());
                }
            }
        });

        return builder.create();
    }

    private Intent getQuantidade(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putFloat("quantidade", Float.valueOf(edtQuantidade.getText().toString()));
        intent.putExtras(bundle);

        return intent;
    }

    private void onExibeAcrescimo(){
        if (Constants.DTO.registro.editaacrescimo){
            layoutAcrescimo.setVisibility(View.VISIBLE);

            if (Constants.DTO.registro.valoracrescimo.equals("V")){
                txtAcrescimoValor.setVisibility(View.VISIBLE);
                edtAcrescimoValor.setVisibility(View.VISIBLE);
                txtAcrescimoPorcentagem.setVisibility(View.GONE);
                edtAcrescimoPorcentagem.setVisibility(View.GONE);
            }else if (Constants.DTO.registro.valoracrescimo.equals("P")){
                txtAcrescimoPorcentagem.setVisibility(View.VISIBLE);
                edtAcrescimoPorcentagem.setVisibility(View.VISIBLE);
                txtAcrescimoValor.setVisibility(View.GONE);
                edtAcrescimoValor.setVisibility(View.GONE);
            }

        }else{
            layoutAcrescimo.setVisibility(View.GONE);
        }
    }
}
