package br.com.informsistemas.forcadevenda.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.fragments.ParceiroFragment;
import br.com.informsistemas.forcadevenda.fragments.ParceiroSearchFragment;
import br.com.informsistemas.forcadevenda.fragments.ParceiroVencimentoFragment;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.tasks.MontagemPrecoTask;
import br.com.informsistemas.forcadevenda.model.utils.IOnBackPressed;

public class ParceiroActivity extends AppCompatActivity {

    private ParceiroFragment parceiroFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parceiro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Parceiros");

        Button btnSelecionarProduto = findViewById(R.id.btn_selecionar_produto);
        btnSelecionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.MOVIMENTO.movimento.codigoparceiro != null) {
                    MontagemPrecoTask montagemPrecoTask = new MontagemPrecoTask(ParceiroActivity.this);
                    montagemPrecoTask.execute();
                }else{
                    Toast.makeText(ParceiroActivity.this, "Necessário informar um parceiro", Toast.LENGTH_LONG).show();
                }
            }
        });

        onShowParceiro();

        if (Constants.MOVIMENTO.movimento.codigoparceiro == null){
            onShowSearchParceiro();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parceiro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.action_search_list:
                onShowSearchParceiro();
                break;
            case R.id.action_titulos:
                onShowTitulosParceiro();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onShowParceiro(){
        parceiroFragment = (ParceiroFragment) getSupportFragmentManager().findFragmentByTag("parceiroFragment");

        if (parceiroFragment == null){
            parceiroFragment = new ParceiroFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, parceiroFragment, "parceiroFragment");
            ft.commit();
        }
    }

    private void onShowSearchParceiro(){
        ParceiroSearchFragment parceiroSearchFragment = (ParceiroSearchFragment) getSupportFragmentManager().findFragmentByTag("parceiroSearchFragment");

        if (parceiroSearchFragment == null){
            parceiroSearchFragment = new ParceiroSearchFragment();

            parceiroSearchFragment.setTargetFragment(parceiroFragment, 0);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, parceiroSearchFragment, "parceiroSearchFragment");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void onShowTitulosParceiro(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("Parceiro", parceiroFragment.onGetParceiroSelecionado());
        ParceiroVencimentoFragment parceiroVencimentoFragment = (ParceiroVencimentoFragment) getSupportFragmentManager().findFragmentByTag("parceiroVencimentoFragment");

        if (parceiroVencimentoFragment == null){
            parceiroVencimentoFragment = new ParceiroVencimentoFragment();

            parceiroVencimentoFragment.setArguments(bundle);
            parceiroFragment.btnSelecionarProduto.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, parceiroVencimentoFragment, "parceiroVencimentoFragment");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("parceiroFragment");

        if (count == 0) {
            super.onBackPressed();
            setResult(Activity.RESULT_OK, new Intent());
            //additional code
        } else {
            if ((fragment instanceof IOnBackPressed)){
                ((IOnBackPressed) fragment).onBackPressed();
            }

            getSupportFragmentManager().popBackStack();

        }
    }

    public void onExibeMovimentoItem(){
        Intent intent = new Intent(ParceiroActivity.this, MovimentoItemActivity.class);
        startActivity(intent);
    }
}
