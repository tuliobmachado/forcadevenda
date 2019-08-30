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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.utils.MoneyTextWatcher;
import br.com.informsistemas.forcadevenda.model.utils.PercentTextWatcher;

public class MaterialSearchModalFragment extends DialogFragment {

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
        edtAcrescimoValor = view.findViewById(R.id.edt_acrescimo_valor);
        edtAcrescimoValor.addTextChangedListener(new MoneyTextWatcher(edtAcrescimoValor));
        edtAcrescimoPorcentagem = view.findViewById(R.id.edt_acrescimo_porcentagem);
        edtAcrescimoPorcentagem.addTextChangedListener(new PercentTextWatcher(edtAcrescimoPorcentagem));
        edtDescontoValor = view.findViewById(R.id.edt_desconto_valor);
        edtDescontoValor.addTextChangedListener(new MoneyTextWatcher(edtDescontoValor));
        edtDescontoPorcentagem = view.findViewById(R.id.edt_desconto_porcentagem);
        edtDescontoPorcentagem.addTextChangedListener(new PercentTextWatcher(edtDescontoPorcentagem));
        edtCusto = view.findViewById(R.id.edt_custo);
        edtCusto.addTextChangedListener(new MoneyTextWatcher(edtCusto));
        layoutAcrescimo = view.findViewById(R.id.layout_acrescimo);
        layoutDesconto = view.findViewById(R.id.layout_desconto);
        layoutCusto = view.findViewById(R.id.layout_custo);

        edtCusto.setOnFocusChangeListener(onExitEditText());
        edtDescontoPorcentagem.setOnFocusChangeListener(onExitEditText());
        edtDescontoValor.setOnFocusChangeListener(onExitEditText());
        edtAcrescimoPorcentagem.setOnFocusChangeListener(onExitEditText());
        edtAcrescimoValor.setOnFocusChangeListener(onExitEditText());

        onExibeAcrescimo();
        onExibeDesconto();
        onExibeCusto();

        txtDescricao.setText(material.descricao);
        if (material.quantidade == 0) {
            possuiQuantidade = false;
            edtQuantidade.setText("1");
        } else {
            possuiQuantidade = true;
            edtQuantidade.setText(Float.toString(material.quantidade));
        }
        txtSaldo.setText(String.format("%.2f", material.saldomaterial) + " | " + material.unidadesaida);
        txtPreco.setText("R$ " + Misc.formatMoeda(material.totalliquido));
        edtAcrescimoValor.setText(Misc.parseFloatToWatcher(material.valoracrescimo));
        edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(material.percacrescimo));
        edtDescontoValor.setText(Misc.parseFloatToWatcher(material.valordesconto));
        edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(material.percdesconto));

        if (material.custo == 0) {
            edtCusto.setText(Misc.parseFloatToWatcher(material.totalliquido));
        } else {
            edtCusto.setText(Misc.parseFloatToWatcher(material.custo));
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

                if ((!possuiQuantidade && Float.parseFloat(edtQuantidade.getText().toString()) == 0) ||
                        (material.quantidade == Float.parseFloat(edtQuantidade.getText().toString()) &&
                                material.valoracrescimo == Misc.parseStringToFloat(edtAcrescimoValor.getText().toString()) &&
                                material.valordesconto == Misc.parseStringToFloat(edtDescontoValor.getText().toString()) &&
                                material.percdesconto == Misc.parseStringToFloat(edtDescontoPorcentagem.getText().toString()) &&
                                material.percacrescimo == Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString()) &&
                                material.custo == Misc.parseStringToFloat(edtCusto.getText().toString()))) {
//                     material.totalliquido == Misc.parseStringToFloat(edtCusto.getText().toString())){
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
        bundle.putFloat("quantidade", Float.valueOf(edtQuantidade.getText().toString()));
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

    private View.OnFocusChangeListener onExitEditText() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    float custo = Misc.parseStringToFloat(edtCusto.getText().toString());
                    float desconto = 0;
                    float acrescimo = 0;
                    float percdesconto = 0;
                    float percacrescimo = 0;

                    switch (v.getId()) {
                        case R.id.edt_custo:
                            if (Constants.DTO.registro.alteracusto) {
                                if (custo < material.custooriginal) {
                                    desconto = (material.custooriginal - custo) * 1;
                                    desconto = Misc.fRound(false, desconto, 2);
                                    edtDescontoValor.setText(Misc.parseFloatToWatcher(desconto));
                                    percdesconto = (desconto * 100) / material.custooriginal;
                                    percdesconto = Misc.fRound(false, percdesconto, 2);
                                    edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(percdesconto));

                                    edtAcrescimoValor.setText(String.valueOf(0));
                                    edtAcrescimoPorcentagem.setText(String.valueOf(0));
                                } else if (custo > material.custooriginal) {
                                    acrescimo = (custo - material.custooriginal) * 1;
                                    acrescimo = Misc.fRound(false, acrescimo, 2);
                                    edtAcrescimoValor.setText(Misc.parseFloatToWatcher(acrescimo));
                                    percacrescimo = (acrescimo * 100) / material.custooriginal;
                                    percacrescimo = Misc.fRound(false, percacrescimo, 2);
                                    edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(percacrescimo));

                                    edtDescontoValor.setText(String.valueOf(0));
                                    edtDescontoPorcentagem.setText(String.valueOf(0));
                                }

                                edtCusto.setText(Misc.parseFloatToWatcher(material.custooriginal));
                            }
                            break;
                        case R.id.edt_desconto_valor:

                            desconto = Misc.parseStringToFloat(edtDescontoValor.getText().toString());

                            percdesconto = (desconto * 100) / custo;
                            percdesconto = Misc.fRound(false, percdesconto, 2);

                            edtDescontoPorcentagem.setText(Misc.parseFloatToWatcher(percdesconto));
                            break;
                        case R.id.edt_desconto_porcentagem:

                            percdesconto = Misc.parseStringToFloat(edtDescontoPorcentagem.getText().toString());

                            desconto = (custo * percdesconto) / 100;
                            desconto = Misc.fRound(false, desconto, 2);

                            edtDescontoValor.setText(Misc.parseFloatToWatcher(desconto));
                            break;
                        case R.id.edt_acrescimo_valor:

                            acrescimo = Misc.parseStringToFloat(edtAcrescimoValor.getText().toString());

                            percacrescimo = (acrescimo * 100) / custo;
                            percacrescimo = Misc.fRound(false, percacrescimo, 2);

                            edtAcrescimoPorcentagem.setText(Misc.parseFloatToWatcher(percacrescimo));
                            break;
                        case R.id.edt_acrescimo_porcentagem:

                            percacrescimo = Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString());

                            acrescimo = (custo * percacrescimo) / 100;
                            acrescimo = Misc.fRound(false, acrescimo, 2);

                            edtAcrescimoValor.setText(Misc.parseFloatToWatcher(acrescimo));
                            break;
                    }
                }
            }
        };
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
    }
}
