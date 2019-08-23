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
    private EditText edtQuantidade;
    private EditText edtAcrescimoValor;
    private EditText edtAcrescimoPorcentagem;
    private LinearLayout layoutAcrescimo;
    private Material material;

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
        position = getArguments().getInt("position");

        material = (Material) getArguments().getSerializable("material");

        txtDescricao = view.findViewById(R.id.txt_descricao);
        txtSaldo = view.findViewById(R.id.txt_saldo);
        txtPreco = view.findViewById(R.id.txt_preco_venda);
        txtAcrescimoValor = view.findViewById(R.id.txt_acrescimo_valor);
        txtAcrescimoPorcentagem = view.findViewById(R.id.txt_acrescimo_porcentagem);
        edtQuantidade = view.findViewById(R.id.edt_quantidade);
        edtAcrescimoValor = view.findViewById(R.id.edt_acrescimo_valor);
        edtAcrescimoValor.addTextChangedListener(textWatcherAcrescimo());
        edtAcrescimoPorcentagem = view.findViewById(R.id.edt_acrescimo_porcentagem);
        edtAcrescimoPorcentagem.addTextChangedListener(textWatcherPorcentagem());
        layoutAcrescimo = view.findViewById(R.id.layout_acrescimo);

        onExibeAcrescimo();

        txtDescricao.setText(material.descricao);
        if (material.quantidade == 0) {
            possuiQuantidade = false;
            edtQuantidade.setText("1");
        }else{
            possuiQuantidade = true;
            edtQuantidade.setText(Float.toString(material.quantidade));
        }
        txtSaldo.setText(String.format("%.2f", material.saldomaterial) + " | " + material.unidadesaida);
        txtPreco.setText("R$ "+ Misc.formatMoeda(material.totalliquido));

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

    private float getQuantidadeEdit(){
        if (edtQuantidade.getText().toString().equals("")){
            return 1;
        }else{
            return Float.parseFloat(edtQuantidade.getText().toString());
        }
    }

    private Intent getQuantidade(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putFloat("quantidade", Float.valueOf(edtQuantidade.getText().toString()));
        bundle.putFloat("valoracrescimo", Misc.parseStringToFloat(edtAcrescimoValor.getText().toString()));
        bundle.putFloat("percacrescimo", Misc.parseStringToFloat(edtAcrescimoPorcentagem.getText().toString()));
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

    private TextWatcher textWatcherAcrescimo(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Locale locale = new Locale("pt", "BR");

                float totalProduto = material.totalliquido * getQuantidadeEdit();

                edtAcrescimoValor.removeTextChangedListener(this);
                BigDecimal parsed = Misc.parseToBigDecimalCurrency(s.toString(), locale);
                float percentual = Misc.fRound (false, (parsed.floatValue() * 100) / (totalProduto), 3);
                String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);
                formatted = formatted.replace("R$", "");
                edtAcrescimoValor.setText(formatted);
                edtAcrescimoValor.setSelection(formatted.length());
                edtAcrescimoValor.addTextChangedListener(this);
//
//                edtAcrescimoPorcentagem.removeTextChangedListener(textWatcherPorcentagem());
//                BigDecimal parsedPercent = Misc.paseToBigDecimalPercent(String.valueOf(percentual), locale);
//                String formattedPercent = NumberFormat.getPercentInstance(locale).format(parsedPercent);
//                formattedPercent = formattedPercent.replace("%", "");
//                String percent2 = percent2(0, 0);
//                edtAcrescimoPorcentagem.setText(formattedPercent);
//                edtAcrescimoPorcentagem.setSelection(formattedPercent.length());
//                edtAcrescimoPorcentagem.addTextChangedListener(textWatcherPorcentagem());
            }
        };
    }

    private TextWatcher textWatcherPorcentagem(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Locale locale = new Locale("pt", "BR");

                edtAcrescimoPorcentagem.removeTextChangedListener(this);
                if (edtAcrescimoPorcentagem == null) return;
                edtAcrescimoPorcentagem.removeTextChangedListener(this);

                NumberFormat format = NumberFormat.getPercentInstance(locale);
                format.setMaximumFractionDigits(3);
                String percentNumber = format.format(convertToDouble(edtAcrescimoPorcentagem.getText().toString())/100);
                percentNumber = percentNumber.replace("%", "");
                edtAcrescimoPorcentagem.setText(percentNumber);
                edtAcrescimoPorcentagem.setSelection(percentNumber.length());
                edtAcrescimoPorcentagem.addTextChangedListener(this);

            }
        };
    }

    private double convertToDouble(String value) {
        double convertedNumber = 0;
        NumberFormat nf = new DecimalFormat("##,###");
        try {
            convertedNumber = nf.parse(value).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedNumber;
    }

    public String percent2(double p1, double p2){
        String str;
        double p3 = (0.01 * 100) / 39.65;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(4);
        str = nf.format(p3);
        return str;
    }
}
