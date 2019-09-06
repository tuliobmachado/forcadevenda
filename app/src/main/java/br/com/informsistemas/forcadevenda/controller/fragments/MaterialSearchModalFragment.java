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

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.utils.MoneyTextWatcher;

public class MaterialSearchModalFragment extends DialogFragment implements View.OnFocusChangeListener{

    private TextView txtDescricao;
    private TextView txtSaldo;
    private TextView txtPreco;
    private TextView txtAcrescimoValor;
    private TextView txtAcrescimoPorcentagem;
    private TextView txtDescontoValor;
    private TextView txtDescontoPorcentagem;
    private EditText edtQuantidade;
    private EditText edtAcrescimoValor;
    private EditText edtAcrescimoPorcentagem;
    private EditText edtDescontoValor;
    private EditText edtDescontoPorcentagem;
    private EditText edtCusto;
    private EditText edtPrecoFinal;
    private LinearLayout layoutAcrescimo;
    private LinearLayout layoutDesconto;
    private LinearLayout layoutCusto;
    private Material material;

    private boolean possuiQuantidade;
    private int position;

    public static MaterialSearchModalFragment newInstance() {
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
        position = getArguments().getInt("position");
        material = (Material) getArguments().getSerializable("material");

        txtDescricao = view.findViewById(R.id.txt_descricao);
        txtSaldo = view.findViewById(R.id.txt_saldo);
        txtPreco = view.findViewById(R.id.txt_preco_venda);
        txtAcrescimoValor = view.findViewById(R.id.txt_acrescimo_valor);
        txtAcrescimoPorcentagem = view.findViewById(R.id.txt_acrescimo_porcentagem);
        txtDescontoValor = view.findViewById(R.id.txt_desconto_valor);
        txtDescontoPorcentagem = view.findViewById(R.id.txt_desconto_porcentagem);
        edtQuantidade = view.findViewById(R.id.edt_quantidade);
        edtQuantidade.addTextChangedListener(new MoneyTextWatcher(edtQuantidade, Constants.DTO.registro.casasquantidade, false));
        edtAcrescimoValor = view.findViewById(R.id.edt_acrescimo_valor);
        edtAcrescimoValor.addTextChangedListener(new MoneyTextWatcher(edtAcrescimoValor, Constants.DTO.registro.casasvalor, false));
        edtAcrescimoPorcentagem = view.findViewById(R.id.edt_acrescimo_porcentagem);
        edtAcrescimoPorcentagem.addTextChangedListener(new MoneyTextWatcher(edtAcrescimoPorcentagem, Constants.DTO.registro.casaspercentual, true));
        edtDescontoValor = view.findViewById(R.id.edt_desconto_valor);
        edtDescontoValor.addTextChangedListener(new MoneyTextWatcher(edtDescontoValor, Constants.DTO.registro.casasvalor, false));
        edtDescontoPorcentagem = view.findViewById(R.id.edt_desconto_porcentagem);
        edtDescontoPorcentagem.addTextChangedListener(new MoneyTextWatcher(edtDescontoPorcentagem, Constants.DTO.registro.casaspercentual, true));
        edtCusto = view.findViewById(R.id.edt_custo);
        edtCusto.addTextChangedListener(new MoneyTextWatcher(edtCusto, Constants.DTO.registro.casaspreco, false));
        edtPrecoFinal = view.findViewById(R.id.edt_preco_final);
        edtPrecoFinal.addTextChangedListener(new MoneyTextWatcher(edtPrecoFinal, Constants.DTO.registro.casasvalor, false));
        layoutAcrescimo = view.findViewById(R.id.layout_acrescimo);
        layoutDesconto = view.findViewById(R.id.layout_desconto);
        layoutCusto = view.findViewById(R.id.layout_custo);

        txtSaldo.setText(String.format("%.2f", material.saldomaterial) + " | " + material.unidadesaida);
        txtPreco.setText("R$ " + Misc.formatMoeda(material.totalliquido.floatValue()));
        edtAcrescimoValor.setText(Misc.parseFloatToWatcher(material.valoracrescimo, Constants.DTO.registro.casasvalor));
        edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(material.percacrescimo, Constants.DTO.registro.casaspercentual));
        edtDescontoValor.setText(Misc.parseFloatToWatcher(material.valordesconto, Constants.DTO.registro.casasvalor));
        edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(material.percdesconto, Constants.DTO.registro.casaspercentual));
        edtCusto.setText(Misc.parseFloatToWatcher(material.custo, Constants.DTO.registro.casaspreco));
        edtPrecoFinal.setText(Misc.parseFloatToWatcher(material.totalliquido, Constants.DTO.registro.casasvalor));

        edtCusto.setOnFocusChangeListener(this);
        edtPrecoFinal.setOnFocusChangeListener(this);
        edtDescontoPorcentagem.setOnFocusChangeListener(this);
        edtDescontoValor.setOnFocusChangeListener(this);
        edtAcrescimoPorcentagem.setOnFocusChangeListener(this);
        edtAcrescimoValor.setOnFocusChangeListener(this);
        edtQuantidade.setOnFocusChangeListener(this);

        onExibeAcrescimo();
        onExibeDesconto();
        onExibeCusto();

        txtDescricao.setText(material.descricao);
        if (material.quantidade.floatValue() == 0) {
            possuiQuantidade = false;
            edtQuantidade.setText(Misc.parseFloatToWatcher(new BigDecimal("1"), Constants.DTO.registro.casasquantidade));
        } else {
            possuiQuantidade = true;
            edtQuantidade.setText(Misc.parseFloatToWatcher(material.quantidade, Constants.DTO.registro.casasquantidade));
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
                if (edtQuantidade.getText().toString().equals("")) {
                    edtQuantidade.setText("0");
                }

                onSetFocusEditText();

                if ((!possuiQuantidade && Misc.parseStringToFloat(edtQuantidade.getText().toString()) == 0) ||
                        (material.quantidade.floatValue() == Misc.parseStringToFloat(edtQuantidade.getText().toString()) &&
                                material.valoracrescimo.floatValue() == Misc.parseStringToFloat(edtAcrescimoValor.getText().toString()) &&
                                material.valordesconto.floatValue() == Misc.parseStringToFloat(edtDescontoValor.getText().toString()) &&
                                material.percdesconto.floatValue() == Misc.parseStringToFloat(edtDescontoPorcentagem.getText().toString()) &&
                                material.percacrescimo.floatValue() == Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString()) &&
                                material.custo.floatValue() == Misc.parseStringToFloat(edtCusto.getText().toString()))) {
                    dialog.dismiss();
                } else {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getQuantidade());
                }
            }
        });

        return builder.create();
    }

    private Intent getQuantidade() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putFloat("quantidade", Misc.parseStringToFloat(edtQuantidade.getText().toString()));
        bundle.putFloat("valoracrescimo", Misc.parseStringToFloat(edtAcrescimoValor.getText().toString()));
        bundle.putFloat("percacrescimo", Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString()));
        bundle.putFloat("valordesconto", Misc.parseStringToFloat(edtDescontoValor.getText().toString()));
        bundle.putFloat("percdesconto", Misc.parseStringToFloat(edtDescontoPorcentagem.getText().toString()));
        bundle.putFloat("custo", Misc.parseStringToFloat(edtCusto.getText().toString()));
        intent.putExtras(bundle);

        return intent;
    }

    private void onExibeAcrescimo() {
        if (Constants.DTO.registro.editaacrescimo) {
            layoutAcrescimo.setVisibility(View.VISIBLE);

            if (Constants.DTO.registro.valoracrescimo.equals("V")) {
                txtAcrescimoValor.setVisibility(View.VISIBLE);
                edtAcrescimoValor.setVisibility(View.VISIBLE);
                txtAcrescimoPorcentagem.setVisibility(View.GONE);
                edtAcrescimoPorcentagem.setVisibility(View.GONE);
            } else if (Constants.DTO.registro.valoracrescimo.equals("P")) {
                txtAcrescimoPorcentagem.setVisibility(View.VISIBLE);
                edtAcrescimoPorcentagem.setVisibility(View.VISIBLE);
                txtAcrescimoValor.setVisibility(View.GONE);
                edtAcrescimoValor.setVisibility(View.GONE);
            }

        } else {
            layoutAcrescimo.setVisibility(View.GONE);
        }
    }

    private void onExibeDesconto() {
        if (Constants.DTO.registro.editadesconto) {
            layoutDesconto.setVisibility(View.VISIBLE);

            if (Constants.DTO.registro.valordesconto.equals("V")) {
                txtDescontoValor.setVisibility(View.VISIBLE);
                edtDescontoValor.setVisibility(View.VISIBLE);
                txtDescontoPorcentagem.setVisibility(View.GONE);
                edtDescontoPorcentagem.setVisibility(View.GONE);
            } else if (Constants.DTO.registro.valordesconto.equals("P")) {
                txtDescontoPorcentagem.setVisibility(View.VISIBLE);
                edtDescontoPorcentagem.setVisibility(View.VISIBLE);
                txtDescontoValor.setVisibility(View.GONE);
                edtDescontoValor.setVisibility(View.GONE);
            }

        } else {
            layoutDesconto.setVisibility(View.GONE);
        }
    }

    private void onExibeCusto() {
        if (Constants.DTO.registro.alterapreco) {
            layoutCusto.setVisibility(View.VISIBLE);
        } else {
            layoutCusto.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            BigDecimal custo = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtCusto.getText().toString()))) ;
            BigDecimal desconto = new BigDecimal("0");
            BigDecimal acrescimo = new BigDecimal("0");
            BigDecimal percdesconto = new BigDecimal("0");
            BigDecimal percacrescimo = new BigDecimal("0");

            switch (v.getId()) {
                case R.id.edt_custo:
                    if (Constants.DTO.registro.alteracusto) {
                        if (custo.floatValue() < material.custooriginal.floatValue()) {
                            desconto = material.custooriginal.subtract(custo).multiply(new BigDecimal("1"));
                            desconto = new BigDecimal(String.valueOf(Misc.fRound(false, desconto, Constants.DTO.registro.casasvalor)));
                            edtDescontoValor.setText(Misc.parseFloatToWatcher(desconto, Constants.DTO.registro.casasvalor));
                            percdesconto = desconto.multiply(new BigDecimal("100")).divide(material.custooriginal, 8, BigDecimal.ROUND_HALF_EVEN);
                            percdesconto = Misc.fRound(false, percdesconto, Constants.DTO.registro.casaspercentual);
                            edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(percdesconto, Constants.DTO.registro.casaspercentual));

                            edtAcrescimoValor.setText(String.valueOf(0));
                            edtAcrescimoPorcentagem.setText(String.valueOf(0));
                        } else if (custo.floatValue() > material.custooriginal.floatValue()) {
                            acrescimo = custo.subtract(material.custooriginal).multiply(new BigDecimal("1"));
                            acrescimo = Misc.fRound(false, acrescimo, Constants.DTO.registro.casasvalor);
                            edtAcrescimoValor.setText(Misc.parseFloatToWatcher(acrescimo, Constants.DTO.registro.casasvalor));
                            percacrescimo = acrescimo.multiply(new BigDecimal("100")).divide(material.custooriginal, 8, BigDecimal.ROUND_HALF_EVEN);
                            percacrescimo = Misc.fRound(false, percacrescimo, Constants.DTO.registro.casaspercentual);
                            edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(percacrescimo, Constants.DTO.registro.casaspercentual));

                            edtDescontoValor.setText(String.valueOf(0));
                            edtDescontoPorcentagem.setText(String.valueOf(0));
                        }else{
                            edtAcrescimoValor.setText(String.valueOf(0));
                            edtAcrescimoPorcentagem.setText(String.valueOf(0));
                            edtDescontoValor.setText(String.valueOf(0));
                            edtDescontoPorcentagem.setText(String.valueOf(0));
                        }

                        edtCusto.setText(Misc.parseFloatToWatcher(material.custooriginal, Constants.DTO.registro.casaspreco));
                    }
                    break;
                case R.id.edt_desconto_valor:

                    desconto = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtDescontoValor.getText().toString())));

                    percdesconto = desconto.multiply(new BigDecimal("100")).divide(custo, 8, BigDecimal.ROUND_HALF_EVEN);
                    percdesconto = Misc.fRound(false, percdesconto, Constants.DTO.registro.casaspercentual);

                    edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(percdesconto, Constants.DTO.registro.casaspercentual));
                    break;
                case R.id.edt_desconto_porcentagem:

                    percdesconto = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtDescontoPorcentagem.getText().toString())));

                    desconto = custo.multiply(percdesconto).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN);
                    desconto = Misc.fRound(false, desconto, Constants.DTO.registro.casasvalor);

                    edtDescontoValor.setText(Misc.parseFloatToWatcher(desconto, Constants.DTO.registro.casasvalor));
                    break;
                case R.id.edt_acrescimo_valor:

                    acrescimo = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtAcrescimoValor.getText().toString())));

                    percacrescimo = acrescimo.multiply(new BigDecimal("100")).divide(custo, 8, BigDecimal.ROUND_HALF_EVEN);
                    percacrescimo = Misc.fRound(false, percacrescimo, Constants.DTO.registro.casaspercentual);

                    edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(percacrescimo, Constants.DTO.registro.casaspercentual));
                    break;
                case R.id.edt_acrescimo_porcentagem:

                    percacrescimo = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString())));

                    acrescimo = custo.multiply(percacrescimo).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN);
                    acrescimo = Misc.fRound(false, acrescimo, Constants.DTO.registro.casasvalor);

                    edtAcrescimoValor.setText(Misc.parseFloatToWatcher(acrescimo, Constants.DTO.registro.casasvalor));
                    break;
                case R.id.edt_preco_final:
                    BigDecimal custo_final = onGetCustoFinal();
                    edtCusto.setText(Misc.parseFloatToWatcher(custo_final, Constants.DTO.registro.casaspreco));
                    onFocusChange(edtCusto, false);
                    break;
                case R.id.edt_quantidade:
                    if (Constants.DTO.registro.alteracusto) {
                        onFocusChange(edtPrecoFinal, false);
                    }
                    break;

            }
        }
    }

    private void onSetFocusEditText(){
        if (edtCusto.hasFocus()){
            edtCusto.setFocusable(false);
        }

        if (edtAcrescimoValor.hasFocus()){
            edtAcrescimoValor.setFocusable(false);
        }

        if (edtAcrescimoPorcentagem.hasFocus()){
            edtAcrescimoPorcentagem.setFocusable(false);
        }

        if (edtDescontoValor.hasFocus()){
            edtDescontoValor.setFocusable(false);
        }

        if (edtDescontoPorcentagem.hasFocus()){
            edtDescontoPorcentagem.setFocusable(false);
        }

        if (edtPrecoFinal.hasFocus()){
            edtPrecoFinal.setFocusable(false);
        }
    }

    private BigDecimal onGetCustoFinal(){
        BigDecimal precoFinal = new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtPrecoFinal.getText().toString())));

        if (precoFinal.floatValue() == 0){
            edtPrecoFinal.setText(Misc.parseFloatToWatcher(material.totalliquidooriginal, Constants.DTO.registro.casasvalor));;
            return material.custooriginal;
        }

        if (precoFinal.floatValue() != material.totalliquidooriginal.floatValue()) {

            CalculoClass calculoClass = new CalculoClass(getActivity(), material);
            return calculoClass.getPrecoFinal(precoFinal,
                    new BigDecimal(String.valueOf(Misc.parseStringToFloat(edtQuantidade.getText().toString()))));
        }
        return material.custooriginal;
    }
}
