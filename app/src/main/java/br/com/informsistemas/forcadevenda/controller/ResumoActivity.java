package br.com.informsistemas.forcadevenda.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.model.dao.FormaPagamentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MaterialDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoItemDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoParcelaDAO;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.utils.CPFCNPJMask;

public class ResumoActivity extends AppCompatActivity {

    private LinearLayout linearLayoutParceiro;
    private LinearLayout linearLayoutMaterial;
    private LinearLayout linearLayoutPagamento;
    private LinearLayout linearLayoutBottomEnviar;
    private EditText edtTxtObservacao;
    private Button btnSalvarPedido;
    private Button btnEnviarPedido;
    private BigDecimal total_ipi;
    private BigDecimal total_icmssubst;
    private BigDecimal total_fecoepst;
    private BigDecimal total_material;
    private BigDecimal total_acrescimo;
    private BigDecimal total_desconto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayoutParceiro = findViewById(R.id.layout_resumo_parceiro);
        linearLayoutMaterial = findViewById(R.id.layout_resumo_material);
        linearLayoutPagamento = findViewById(R.id.layout_resumo_pagamento);
        linearLayoutBottomEnviar = findViewById(R.id.layout_bottom_enviar);
        btnEnviarPedido = findViewById(R.id.btn_enviar_pedido);
        btnSalvarPedido = findViewById(R.id.btn_salvar_pedido);
        edtTxtObservacao = findViewById(R.id.edtObservacao);

        total_ipi = new BigDecimal("0");
        total_icmssubst = new BigDecimal("0");
        total_fecoepst = new BigDecimal("0");
        total_material = new BigDecimal("0");
        total_acrescimo = new BigDecimal("0");
        total_desconto = new BigDecimal("0");

        if (!Constants.MOVIMENTO.movimento.observacao.equals("")){
            edtTxtObservacao.setText(Constants.MOVIMENTO.movimento.observacao);
        }

        if (Constants.MOVIMENTO.movimento.sincronizado.equals("T") || Constants.MOVIMENTO.movimento.sincronizado.equals("P")){
            if (Constants.MOVIMENTO.movimento.sincronizado.equals("T")){
                linearLayoutBottomEnviar.setVisibility(View.GONE);
            }else {
                linearLayoutBottomEnviar.setVisibility(View.VISIBLE);
                btnSalvarPedido.setVisibility(View.GONE);
            }
            edtTxtObservacao.setEnabled(false);
        }

        setTitle("Resumo Pedido");

        Button btnEnviarPedido = findViewById(R.id.btn_enviar_pedido);
        btnEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSalvarPedido(true);
            }
        });

        Button btnSalvarPedido = findViewById(R.id.btn_salvar_pedido);
        btnSalvarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSalvarPedido(false);
            }
        });

        getDadosParceiro();
        getDadosItens();
        getDadosFormaPagamentos();
        getDadosTotais();

    }

    private void enviarSalvarPedido(Boolean enviar){
        if (!edtTxtObservacao.getText().toString().equals("")){
            Constants.MOVIMENTO.movimento.observacao = edtTxtObservacao.getText().toString();
        }

        if (Constants.MOVIMENTO.movimento.datafim == null){
            Constants.MOVIMENTO.movimento.datafim = new Date();
        }else{
            Constants.MOVIMENTO.movimento.dataalteracao = new Date();
        }

        MovimentoDAO.getInstance(ResumoActivity.this).createOrUpdate(Constants.MOVIMENTO.movimento);

        Constants.MOVIMENTO.enviarPedido = enviar;
        if (enviar) {
            Constants.PEDIDO.movimento = MovimentoDAO.getInstance(ResumoActivity.this).findById(Constants.MOVIMENTO.movimento.id);
            Constants.PEDIDO.PEDIDOATUAL = 2;
            Constants.PEDIDO.listPedidos = null;
            Constants.PEDIDO.listPedidos = new ArrayList<>();
            Constants.PEDIDO.listPedidos.add(Constants.PEDIDO.movimento.id);
        }
        Intent intent = new Intent(ResumoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void getDadosParceiro() {
        Parceiro p = ParceiroDAO.getInstance(this).findByIdAuxiliar("codigoparceiro", Constants.MOVIMENTO.movimento.codigoparceiro);
        TextView txtCodigoParceiro = new TextView(this);
        TextView txtDescricao = new TextView(this);
        TextView txtCPFCGC = new TextView(this);

        txtCodigoParceiro.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (p != null){
            txtCodigoParceiro.setText(p.codigoparceiro);
        }else{
            txtCodigoParceiro.setText(Constants.MOVIMENTO.movimento.codigoparceiro);
        }
        txtCodigoParceiro.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtCodigoParceiro.setTypeface(null, Typeface.BOLD);

        txtDescricao.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (p != null) {
            txtDescricao.setText(p.descricao);
        }else{
            txtDescricao.setText(Constants.MOVIMENTO.movimento.descricaoparceiro);
        }
        txtDescricao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtDescricao.setTypeface(null, Typeface.BOLD);

        txtCPFCGC.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        if (p != null) {
            txtCPFCGC.setText(CPFCNPJMask.getMask(p.cpfcgc));
        }else{
            txtCPFCGC.setText(CPFCNPJMask.getMask("00000000000000"));
        }
        txtCPFCGC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        linearLayoutParceiro.addView(txtCodigoParceiro);
        linearLayoutParceiro.addView(txtDescricao);
        linearLayoutParceiro.addView(txtCPFCGC);
    }

    private void getDadosItens(){
        Constants.PEDIDO.movimentoItems = MovimentoItemDAO.getInstance(this).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

        TextView txtTitleDescricao = findViewById(R.id.txt_resumo_title_material_descricao);
        TextView txtTitleTotal = findViewById(R.id.txt_resumo_title_material_total);

        txtTitleDescricao.setText(Constants.PEDIDO.movimentoItems.size()+"x PRODUTOS");
        txtTitleTotal.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido.floatValue()));

        for (int i = 0; i < Constants.PEDIDO.movimentoItems.size(); i++) {
            Material m = MaterialDAO.getInstance(this).findByIdAuxiliar("codigomaterial", Constants.PEDIDO.movimentoItems.get(i).codigomaterial);

            linearLayoutMaterial.addView(newTextView(Constants.PEDIDO.movimentoItems.get(i).quantidade+"x"+m.descricao));

            total_fecoepst = total_fecoepst.add(Constants.PEDIDO.movimentoItems.get(i).valoricmsfecoepst);
            total_ipi = total_ipi.add(Constants.PEDIDO.movimentoItems.get(i).valoripi);
            total_icmssubst = total_icmssubst.add(Constants.PEDIDO.movimentoItems.get(i).valoricmssubst);
            total_acrescimo = total_acrescimo.add(Constants.PEDIDO.movimentoItems.get(i).valoracrescimoitem);
            total_desconto = total_desconto.add(Constants.PEDIDO.movimentoItems.get(i).valordescontoitem);

            total_material = total_material.add(Constants.PEDIDO.movimentoItems.get(i).custo.multiply(Constants.PEDIDO.movimentoItems.get(i).quantidade));
        }
    }

    private void getDadosFormaPagamentos(){
        Constants.PEDIDO.movimentoParcelas = MovimentoParcelaDAO.getInstance(this).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

        FormaPagamento p = FormaPagamentoDAO.getInstance(this).findByIdAuxiliar("codigoforma", Constants.PEDIDO.movimentoParcelas.get(0).codigoforma);
        linearLayoutPagamento.addView(newTextView(p.descricao));
    }

    private void getDadosTotais(){
        TextView txtTitleDescricao = findViewById(R.id.txt_resumo_title_total);
        TextView txtTitleTotal = findViewById(R.id.txt_resumo_title_valor_material);
        TextView txttitleTotalIPI = findViewById(R.id.txt_resumo_title_total_ipi);
        TextView txtTitleTotalICMSSubst = findViewById(R.id.txt_resumo_title_total_icmssubst);
        TextView txtTitleTotalFecoepST = findViewById(R.id.txt_resumo_title_total_fecoepst);
        TextView txtTitleTotalDesconto = findViewById(R.id.txt_resumo_title_total_desconto);
        TextView txtTitleTotalAcrescimo = findViewById(R.id.txt_resumo_title_total_acrescimo);

        txtTitleDescricao.setText("Total Material");
        txtTitleTotal.setText("R$ " + Misc.formatMoeda(total_material.floatValue()));
        txtTitleTotalDesconto.setText("R$ " + Misc.formatMoeda(total_desconto.floatValue()));
        txtTitleTotalAcrescimo.setText("R$ " + Misc.formatMoeda(total_acrescimo.floatValue()));
        txttitleTotalIPI.setText("R$ " + Misc.formatMoeda(total_ipi.floatValue()));
        txtTitleTotalICMSSubst.setText("R$ " + Misc.formatMoeda(total_icmssubst.floatValue()));
        txtTitleTotalFecoepST.setText("R$ " + Misc.formatMoeda(total_fecoepst.floatValue()));
    }

    private TextView newTextView(String descricao){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(descricao);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        return textView;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            Constants.PEDIDO.movimentoParcelas = null;
            Constants.PEDIDO.movimentoItems = null;
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
