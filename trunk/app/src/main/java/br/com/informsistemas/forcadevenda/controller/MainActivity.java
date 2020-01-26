package br.com.informsistemas.forcadevenda.controller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.fragments.MaterialSaldoFragment;
import br.com.informsistemas.forcadevenda.controller.fragments.MovimentoFragment;
import br.com.informsistemas.forcadevenda.controller.fragments.ParceiroConsultaFragment;
import br.com.informsistemas.forcadevenda.controller.fragments.RelatorioPedidoFragment;
import br.com.informsistemas.forcadevenda.model.dao.DatabaseManager;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.RegistroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.Registro;
import br.com.informsistemas.forcadevenda.model.utils.IOnBackPressed;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private String tagFragment;
    private Fragment movimentoFragment;
    private int indexMenu;
    private int indexSubMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout_parceiro);
        tabLayout.setVisibility(View.GONE);
        tabLayout.addTab(tabLayout.newTab().setText("Dados"));
        tabLayout.addTab(tabLayout.newTab().setText("Títulos"));

        DatabaseManager.init(this);

        FloatingActionButton fab = findViewById(R.id.fab_adicionar_pedido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowMeta(10);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        indexMenu = 0;
        indexSubMenu = 0;
        navigationView.getMenu().getItem(indexMenu).setChecked(true);

        ChecaPermissoes();

        onShow();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pedido) {
            onShowFragment("movimentoFragment");
            onSetIndexMenu(0, 0);
        } else if (id == R.id.nav_consulta_meta) {
            onShowMeta(11);
        } else if (id == R.id.nav_consulta_parceiro) {
            onShowFragment("parceiroConsultaFragment");
            onSetIndexMenu(1, 1);
        } else if (id == R.id.nav_consulta_estoque) {
            onShowFragment("materialSaldoFragment");
            onSetIndexMenu(1, 2);
        } else if (id == R.id.nav_relatorio_pedido) {
            onShowFragment("relatorioPedidoFragment");
            onSetIndexMenu(2, 0);
        } else if (id == R.id.nav_relatorio_parceiro) {
            onSetIndexMenu(2, 1);
        } else if (id == R.id.nav_relatorio_material) {
            onSetIndexMenu(2, 2);
        } else if (id == R.id.nav_configuracoes_limpeza) {
            apagarBanco();
            onSetItemMenu();
        } else if (id == R.id.nav_configuracoes_sincronia) {
            ((MovimentoFragment) movimentoFragment).getSincronia(true);
        } else if (id == R.id.nav_configuracoes_sair) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Registro reg = null;
        try {
            reg = (Registro) data.getExtras().getSerializable("Registro");
        } catch (Exception e) {
            reg = null;
        }

        if (reg != null) {
            if (Constants.DTO.registro == null) {
                RegistroDAO.getInstance(this).createOrUpdate(reg);
            } else {
                Constants.DTO.registro.status = reg.status;
                RegistroDAO.getInstance(this).createOrUpdate(Constants.DTO.registro);
            }
        }
        onShow();
    }

    private void onShow() {
        Constants.DTO.registro = RegistroDAO.getInstance(this).findFirst();

        if (Constants.DTO.registro == null) {
            onShowLogin();
        } else {
            onShowPrincipal();
        }
    }

    private void onShowLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_LOGIN.REGISTRO_PENDENTE);
    }

    private void onShowPrincipal() {
        if (Constants.DTO.registro.status.equals("P") || Constants.DTO.registro.status.equals("B")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("Status", Constants.DTO.registro.status);
            startActivityForResult(intent, Constants.REQUEST_LOGIN.ACESSO_PENDENTE);
        } else {
            onShowFragment("movimentoFragment");
        }
    }

    private void onShowFragment(String tag) {
        tagFragment = tag;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (tag) {
            case "movimentoFragment":
                int count = getSupportFragmentManager().getBackStackEntryCount();

                for (int i = 0; i < count; i++) {
                    onBackPressed();
                }

                movimentoFragment = getSupportFragmentManager().findFragmentByTag(tag);

                if (movimentoFragment == null) {
                    movimentoFragment = new MovimentoFragment();
                    ft.replace(R.id.fragment_container, movimentoFragment, tag);
                } else {
                    ft.detach(movimentoFragment);
                    ft.attach(movimentoFragment);
                }
                break;
            case "parceiroConsultaFragment":
                ParceiroConsultaFragment parceiroConsultaFragment = (ParceiroConsultaFragment) getSupportFragmentManager().findFragmentByTag(tag);

                if (parceiroConsultaFragment == null) {
                    parceiroConsultaFragment = new ParceiroConsultaFragment();
                }

                ft.replace(R.id.fragment_container, parceiroConsultaFragment, tag);
                ft.addToBackStack(null);

                break;

            case "materialSaldoFragment":
                MaterialSaldoFragment materialSaldoFragment = (MaterialSaldoFragment) getSupportFragmentManager().findFragmentByTag(tag);

                if (materialSaldoFragment == null) {
                    materialSaldoFragment = new MaterialSaldoFragment();
                }

                ft.replace(R.id.fragment_container, materialSaldoFragment, tag);
                ft.addToBackStack(null);

                break;

            case "relatorioPedidoFragment":
                RelatorioPedidoFragment relatorioPedidoFragment = (RelatorioPedidoFragment) getSupportFragmentManager().findFragmentByTag(tag);

                if (relatorioPedidoFragment == null){
                    relatorioPedidoFragment = new RelatorioPedidoFragment();
                }

                ft.replace(R.id.fragment_container, relatorioPedidoFragment, tag);
                ft.addToBackStack(null);

                break;
        }

        ft.commitAllowingStateLoss();
    }

    private void onShowMeta(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogDefault);
        builder.setTitle("Meta");
        builder.setMessage(
                Constants.DTO.metaFuncionario.descricao+"\n"+
                        "Meta Mensal:      R$ "+Misc.formatMoeda(Constants.DTO.metaFuncionario.metamensal)+"\n"+
                        "Meta Diária:         R$ "+Misc.formatMoeda(Constants.DTO.metaFuncionario.metadiaria)+"\n"+
                        "Meta Realizada:  R$ "+Misc.formatMoeda(Constants.DTO.metaFuncionario.metarealizada)+"\n"+
                        "Meta A Realizar: R$ "+Misc.formatMoeda(Constants.DTO.metaFuncionario.metaarealizar)
        );
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (requestCode == 10){
                    onShowPedido();
                }else{
                    onSetItemMenu();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagFragment);

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            if ((fragment instanceof IOnBackPressed)) {
                ((IOnBackPressed) fragment).onBackPressed();
            }

            getSupportFragmentManager().popBackStack();

            if (count == 1) {
                unCheckAllMenuItems(navigationView.getMenu());
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    public void onShowPedido() {
        Misc.setTabelasPadrao();
        Constants.MOVIMENTO.movimento = new Movimento(Constants.MOVIMENTO.codigoempresa,
                Constants.MOVIMENTO.codigofilialcontabil, Constants.MOVIMENTO.codigoalmoxarifado,
                Constants.MOVIMENTO.codigooperacao, Constants.MOVIMENTO.codigotabelapreco,
                null, "", new BigDecimal("0"), "", Misc.GetDateAtual(), null, null, null, "", "", Misc.gerarMD5(), "", "");
        Intent intent = new Intent(MainActivity.this, ParceiroActivity.class);
        startActivityForResult(intent, 0);
    }

    public void onSetIndexMenu(int iMenu, int iSubMenu) {
        indexMenu = iMenu;
        indexSubMenu = iSubMenu;
    }

    public void onSetItemMenu() {
        unCheckAllMenuItems(navigationView.getMenu());

        if (indexMenu == 0) {
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            navigationView.getMenu().getItem(indexMenu).getSubMenu().getItem(indexSubMenu).setChecked(true);
        }
    }

    private void apagarBanco(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogDefault);
        builder.setMessage("Deseja realmente confirmar? Todos os dados serão apagados");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseManager.getInstance().getHelper().onDeleteAllTable();
                logout();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                indexMenu = 0;
                indexSubMenu = 0;
                navigationView.getMenu().getItem(indexMenu).setChecked(true);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout(){
        deslogar();
    }

    private void deslogar(){
        RegistroDAO.getInstance(this).deleteAll();
        indexMenu = 0;
        indexSubMenu = 0;
        navigationView.getMenu().getItem(indexMenu).setChecked(true);
        movimentoFragment.getFragmentManager().beginTransaction().remove(movimentoFragment).commit();
        onShow();
    }

    private void ChecaPermissoes(){
        Constants.PERMISSION.READ_PHONE_STATE = Misc.GetReturnPermission(this, Manifest.permission.READ_PHONE_STATE);
    }
}
