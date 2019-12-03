package br.com.informsistemas.forcadevenda.controller;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.fragments.MovimentoItemFragment;
import br.com.informsistemas.forcadevenda.controller.fragments.MaterialSearchFragment;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoItemDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.utils.IOnBackPressed;

public class MovimentoItemActivity extends AppCompatActivity {

    private MovimentoItemFragment movimentoItemFragment;
    private MaterialSearchFragment materialSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimento_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);
        tabLayout.addTab(tabLayout.newTab().setText("Produtos"));
        tabLayout.addTab(tabLayout.newTab().setText("Categorias"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Materiais");

        Button btn = findViewById(R.id.btn_selecionar_pagamento);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.MOVIMENTO.movimento.id != null) {
                    if (MovimentoItemDAO.getInstance(MovimentoItemActivity.this).findByMovimentoId(Constants.MOVIMENTO.movimento.id).size() > 0) {
                        Intent intent = new Intent(MovimentoItemActivity.this, MovimentoParcelaActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MovimentoItemActivity.this, "Necessário informar um material", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MovimentoItemActivity.this, "Necessário informar um material", Toast.LENGTH_LONG).show();
                }
            }
        });

        onShowMovimentoItem();

        if (Constants.MOVIMENTO.movimento.id == null){
            onShowSearchMaterial();
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
                onShowSearchMaterial();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onShowMovimentoItem(){
        movimentoItemFragment = (MovimentoItemFragment) getSupportFragmentManager().findFragmentByTag("movimentoItemFragment");

        if (movimentoItemFragment == null){
            movimentoItemFragment = new MovimentoItemFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, movimentoItemFragment, "movimentoItemFragment");
            ft.commit();
        }
    }

    private void onShowSearchMaterial(){
        materialSearchFragment = (MaterialSearchFragment) getSupportFragmentManager().findFragmentByTag("materialSearchFragment");

        if (materialSearchFragment == null){
            materialSearchFragment = new MaterialSearchFragment();

            materialSearchFragment.setTargetFragment(movimentoItemFragment, 0);
            materialSearchFragment.setArguments(getListMovimentoItem());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, materialSearchFragment, "materialSearchFragment");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("materialSearchFragment");

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            if ((fragment instanceof IOnBackPressed)){
                ((IOnBackPressed) fragment).onBackPressed();
            }

            getSupportFragmentManager().popBackStack();

        }
    }

    private Bundle getListMovimentoItem(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("listMaterialSelecionados", (Serializable) movimentoItemFragment.getListMaterialSelecionados());

        return bundle;
    }
}
