package br.com.informsistemas.forcadevenda.model.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeakReference;
    private final Locale locale;
    private int casasDecimais;
    private Double divisao;
    private boolean percentual;

    public MoneyTextWatcher(EditText editText, Locale locale, int casasDecimais, boolean percentual) {
        this.editTextWeakReference = new WeakReference<EditText>(editText);
        this.locale = locale != null ? locale : Locale.getDefault();
        this.casasDecimais = casasDecimais;
        this.divisao = getValorDivisao(casasDecimais);
        this.percentual = percentual;
    }

    public MoneyTextWatcher(EditText editText, int casasDecimais, boolean percentual) {
        this.editTextWeakReference = new WeakReference<EditText>(editText);
        this.locale = Locale.getDefault();
        this.casasDecimais = casasDecimais;
        this.divisao = getValorDivisao(casasDecimais);
        this.percentual = percentual;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        editText.removeTextChangedListener(this);

        BigDecimal parsed = parseToBigDecimal(editable.toString(), locale);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setMinimumFractionDigits(casasDecimais);
        String formatted = formatter.format(parsed);

        if (!percentual) {
            formatted = formatted.replace("R$", "");
        }else{
            formatted = formatted.replaceAll("[%s.R$\\s]", "");
        }

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    private Double getValorDivisao(int casasDecimais){
        String value = "1";
        for (int i = 0; i < casasDecimais; i++) {
            value = value + "0";
        }
        return Double.parseDouble(value);
    }

    private BigDecimal parseToBigDecimal(String value, Locale locale) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String cleanString = value.replaceAll(replaceable, "");
        return new BigDecimal(cleanString).divide(BigDecimal.valueOf(divisao), 8, BigDecimal.ROUND_HALF_EVEN);
    }
}
