package br.com.informsistemas.forcadevenda.controller;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private float total_ipi;
    private float total_icmssubst;
    private float total_fecoepst;
    private float total_material;

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
        edtTxtObservacao = findViewById(R.id.edtObservacao);

        if (!Constants.MOVIMENTO.movimento.observacao.equals("")){
            edtTxtObservacao.setText(Constants.MOVIMENTO.movimento.observacao);
        }

        if (Constants.MOVIMENTO.movimento.sincronizado.equals("T") || Constants.MOVIMENTO.movimento.sincronizado.equals("P")){
            linearLayoutBottomEnviar.setVisibility(View.GONE);
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
        TextView txtDescricao = new TextView(this);
        TextView txtCPFCGC = new TextView(this);

        txtDescricao.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtDescricao.setText(p.descricao);
        txtDescricao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        txtDescricao.setTypeface(null, Typeface.BOLD);

        txtCPFCGC.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtCPFCGC.setText(CPFCNPJMask.getMask(p.cpfcgc));
        txtCPFCGC.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        linearLayoutParceiro.addView(txtDescricao);
        linearLayoutParceiro.addView(txtCPFCGC);
    }

    private void getDadosItens(){
        Constants.PEDIDO.movimentoItems = MovimentoItemDAO.getInstance(this).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

        TextView txtTitleDescricao = findViewById(R.id.txt_resumo_title_material_descricao);
        TextView txtTitleTotal = findViewById(R.id.txt_resumo_title_material_total);

        txtTitleDescricao.setText(Constants.PEDIDO.movimentoItems.size()+"x PRODUTOS");
        txtTitleTotal.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido));

        for (int i = 0; i < Constants.PEDIDO.movimentoItems.size(); i++) {
            Material m = MaterialDAO.getInstance(this).findByIdAuxiliar("codigomaterial", Constants.PEDIDO.movimentoItems.get(i).codigomaterial);

            linearLayoutMaterial.addView(newTextView(Constants.PEDIDO.movimentoItems.get(i).quantidade+"x"+m.descricao));

            total_fecoepst = total_fecoepst + Constants.PEDIDO.movimentoItems.get(i).valoricmsfecoepst;
            total_ipi = total_ipi + Constants.PEDIDO.movimentoItems.get(i).valoripi;
            total_icmssubst = total_icmssubst + Constants.PEDIDO.movimentoItems.get(i).valoricmssubst;

            total_material = total_material + (Constants.PEDIDO.movimentoItems.get(i).custo * Constants.PEDIDO.movimentoItems.get(i).quantidade);
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

        txtTitleDescricao.setText("Total Material");
        txtTitleTotal.setText("R$ " + Misc.formatMoeda(total_material));

        txttitleTotalIPI.setText("R$ " + Misc.formatMoeda(total_ipi));
        txtTitleTotalICMSSubst.setText("R$ " + Misc.formatMoeda(total_icmssubst));
        txtTitleTotalFecoepST.setText("R$ " + Misc.formatMoeda(total_fecoepst));
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
