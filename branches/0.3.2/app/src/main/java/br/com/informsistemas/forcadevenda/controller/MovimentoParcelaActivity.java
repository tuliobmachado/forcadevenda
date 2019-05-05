package br.com.informsistemas.forcadevenda.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.fragments.MovimentoParcelaFragment;
import br.com.informsistemas.forcadevenda.controller.fragments.FormaPagamentoSearchFragment;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoParcelaDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;

public class MovimentoParcelaActivity extends AppCompatActivity {

    private MovimentoParcelaFragment movimentoParcelaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Pagamentos");

        Button btn = findViewById(R.id.btn_resumo_pedido);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MovimentoParcelaDAO.getInstance(MovimentoParcelaActivity.this).findByMovimentoId(Constants.MOVIMENTO.movimento.id).size() > 0) {
                    Intent intent = new Intent(MovimentoParcelaActivity.this, ResumoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MovimentoParcelaActivity.this, "Necessário informar uma forma de pagamento", Toast.LENGTH_LONG).show();
                }
            }
        });

        onShowFormaPagamento();
        if (Constants.MOVIMENTO.codigoformapagamento.equals("")) {
            if (MovimentoParcelaDAO.getInstance(this).findByMovimentoId(Constants.MOVIMENTO.movimento.id).size() == 0) {
                onShowSearchFormaPagamento();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.action_search_list:
                onShowSearchFormaPagamento();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onShowFormaPagamento(){
        movimentoParcelaFragment = (MovimentoParcelaFragment) getSupportFragmentManager().findFragmentByTag("pagamentoFragment");

        if (movimentoParcelaFragment == null){
            movimentoParcelaFragment = new MovimentoParcelaFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, movimentoParcelaFragment, "pagamentoFragment");
            ft.commit();
        }
    }

    private void onShowSearchFormaPagamento(){
        FormaPagamentoSearchFragment pagamentoSearchFragment = (FormaPagamentoSearchFragment) getSupportFragmentManager().findFragmentByTag("pagamentoSearchFragment");

        if (pagamentoSearchFragment == null){
            pagamentoSearchFragment = new FormaPagamentoSearchFragment();

            pagamentoSearchFragment.setTargetFragment(movimentoParcelaFragment, 0);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, pagamentoSearchFragment, "pagamentoSearchFragment");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

}
