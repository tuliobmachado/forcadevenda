package br.com.informsistemas.forcadevenda.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.adapter.MaterialSearchAdapter;
import br.com.informsistemas.forcadevenda.model.dao.MaterialDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoItemDAO;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.utils.IOnBackPressed;

public class MaterialSearchFragment extends Fragment implements IOnBackPressed, MaterialSearchAdapter.OnMaterialListener {

    private SearchView searchView;
    private List<Material> listMaterial;
    private RecyclerView recyclerView;
    private MaterialSearchAdapter materialSearchAdapter;
    private TextView txtCategoriaSelecionada;
    private TextView txtTotalItem;
    private TabLayout tabLayout;
    private CategoriaFragment categoriaFragment;
    private Categoria categoriaFiltro;
    private List<Material> listMaterialFiltro;
    private List<Material> listSelecionado;
    private List<String> listCodigoMaterial;
    private List<Material> listMaterialMovItens;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        LinearLayout linearLayout = getActivity().findViewById(R.id.layout_categoria);
        linearLayout.setVisibility(View.VISIBLE);
        Button btn = getActivity().findViewById(R.id.btn_selecionar_pagamento);
        btn.setVisibility(View.GONE);
        txtCategoriaSelecionada = getActivity().findViewById(R.id.txt_categoria_selecionada);

        tabLayout = getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(getOnTabSelectedListener());

        txtTotalItem = getActivity().findViewById(R.id.txt_total_item);
        txtTotalItem.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido.floatValue()));

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        listMaterialMovItens = (List<Material>) getArguments().getSerializable("listMaterialSelecionados");
        getArguments().clear();

        if (categoriaFiltro != null) {

            if (categoriaFiltro.descricao.equals("TODAS")) {
                listMaterial = Constants.DTO.listMaterialPreco;
                listMaterialFiltro = null;
            } else {
                listMaterial = getListFiltro(categoriaFiltro.codigogrupo);
            }

            txtCategoriaSelecionada.setText(categoriaFiltro.descricao);
        } else {
            txtCategoriaSelecionada.setText("TODAS");
            listMaterial = Constants.DTO.listMaterialPreco;
            listMaterialFiltro = null;

            if (listMaterialMovItens != null){
                for (int i = 0; i < listMaterialMovItens.size(); i++) {
                    onAddSelecionado(listMaterialMovItens.get(i));
                }
            }
        }

        MaterialDAO.getInstance(getActivity()).setListPesquisa(listMaterial);
        setAdapter(listMaterial);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listMaterial = MaterialDAO.getInstance(getActivity()).pesquisaLista(newText, listMaterialFiltro);
                setAdapter(listMaterial);
                return false;
            }
        });
        searchView.setQueryHint("Pesquisar Materiais...");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setAdapter(List<Material> list) {
        materialSearchAdapter = new MaterialSearchAdapter(getActivity(), list, this);
        recyclerView.setAdapter(materialSearchAdapter);
    }

    private TabLayout.OnTabSelectedListener getOnTabSelectedListener(){
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (categoriaFragment != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        break;
                    case 1:
                        categoriaFragment = (CategoriaFragment) getActivity().getSupportFragmentManager().findFragmentByTag("categoriaFragment");

                        if (categoriaFragment == null) {
                            categoriaFragment = new CategoriaFragment();
                        }

                        categoriaFragment.setTargetFragment(MaterialSearchFragment.this, 0);
                        categoriaFragment.setArguments(getCategoriaFiltro());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, categoriaFragment, "categoriaFragment");
                        ft.addToBackStack(null);
                        ft.commit();

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private List<Material> getListFiltro(String codigogrupo){
        listMaterialFiltro = MaterialDAO.getInstance(getActivity()).aplicarFiltro(codigogrupo);
        List<Material> listFiltro = new ArrayList<>();

        for (int i = 0; i < listMaterialFiltro.size(); i++) {
            for (int j = 0; j < Constants.DTO.listMaterialPreco.size(); j++) {
                if (listMaterialFiltro.get(i).codigomaterial.equals(Constants.DTO.listMaterialPreco.get(j).codigomaterial)){
                    listFiltro.add(Constants.DTO.listMaterialPreco.get(j));
                    break;
                }
            }

        }

        return listFiltro;
    }

    private void setTotal(BigDecimal valor, int position) {
        BigDecimal valorAtual = Constants.MOVIMENTO.movimento.totalliquido;

        valorAtual = valorAtual.add(valor);

        if (valorAtual.floatValue() < 0){
            valorAtual = new BigDecimal("0");
        }

        txtTotalItem.setText("R$ " + Misc.formatMoeda(valorAtual.floatValue()));

        Constants.MOVIMENTO.movimento.totalliquido = valorAtual;
    }

    private Intent getIntent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("listMaterial", (Serializable) listSelecionado);
        intent.putExtras(bundle);

        return intent;
    }

    @Override
    public boolean onBackPressed() {
        searchView.clearFocus();
        tabLayout.setVisibility(View.GONE);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getIntent());
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                categoriaFiltro = (Categoria) data.getExtras().getSerializable("Categoria");
                tabLayout.getTabAt(0).select();
                break;
            case 1:
                AdicionarMaterialQuantidade(data);
                break;
        }
    }

    private Bundle getCategoriaFiltro() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Categoria", categoriaFiltro);

        return bundle;
    }

    private Material calculaTotalLiquido(Material material){
        material.precocalculado = material.precocalculado.multiply(material.quantidade);
        CalculoClass calculoClass = new CalculoClass(getActivity(), material);
        calculoClass.setTributos();

        return material;
    }

    private BigDecimal calculaTotalExclusao(Material m){
        m.precocalculado = m.precocalculado.multiply(m.quantidade);
        CalculoClass calculoClass = new CalculoClass(getActivity(), m);
        calculoClass.setTributos();

        return calculoClass.getTotalLiquido();
    }

    private void removeMovimentoItem(String codigomaterial){
        if (Constants.MOVIMENTO.movimento.id != null) {
            MovimentoItem movimentoItem = MovimentoItemDAO.getInstance(getActivity()).findByMovimentoIdItem(Constants.MOVIMENTO.movimento.id, codigomaterial);

            if (movimentoItem != null){
                MovimentoItemDAO.getInstance(getActivity()).deleteByMovimentoId(Constants.MOVIMENTO.movimento.id, codigomaterial);
            }
        }
    }

    private void onAddSelecionado(Material material) {
        boolean remover = false;

        if (listCodigoMaterial == null) {
            listCodigoMaterial = new ArrayList<>();
        }

        if (listSelecionado == null) {
            listSelecionado = new ArrayList<>();
        }

        if (listCodigoMaterial.contains(material.codigomaterial)) {

            if (material.quantidade.floatValue() == 0) {
                remover = true;
            }

            for (int i = 0; i < listSelecionado.size(); i++) {
                if (material.codigomaterial.equals(listSelecionado.get(i).codigomaterial)) {
                    if (remover) {
                        listCodigoMaterial.remove(material.codigomaterial);
                        listSelecionado.remove(i);
                    } else {
                        listSelecionado.set(i, material);
                    }
                }
            }
        } else {
            listCodigoMaterial.add(material.codigomaterial);
            listSelecionado.add(material);
        }
    }

    @Override
    public void onBotaoExcluirClick(int position) {
        Material material = null;
        Material materialExclusao = null;
        BigDecimal valorRemovido;
        BigDecimal valorQtdTotal;
        BigDecimal valorQtdReduzida;

        try {
            material = Misc.cloneMaterial(listMaterial.get(position));
            materialExclusao = Misc.cloneMaterial(listMaterial.get(position));

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        listMaterial.get(position).quantidade = listMaterial.get(position).quantidade.subtract(new BigDecimal("1"));

        if (listMaterial.get(position).quantidade.floatValue() >= 1){
            materialExclusao.quantidade = listMaterial.get(position).quantidade;

            valorQtdTotal = new BigDecimal(String.valueOf(calculaTotalExclusao(material)));
            valorQtdReduzida = new BigDecimal(String.valueOf(calculaTotalExclusao(materialExclusao)));
            valorRemovido = valorQtdTotal.subtract(valorQtdReduzida);
        }else{
            valorRemovido = listMaterial.get(position).totalliquido;
        }

        if (listMaterial.get(position).quantidade.floatValue() == 0){
            removeMovimentoItem(listMaterial.get(position).codigomaterial);
            listMaterial.get(position).valordesconto = new BigDecimal("0");
            listMaterial.get(position).valoracrescimo = new BigDecimal("0");
            listMaterial.get(position).percdesconto = new BigDecimal("0");
            listMaterial.get(position).percacrescimo = new BigDecimal("0");
            listMaterial.get(position).valordescontoant = new BigDecimal("0");
            listMaterial.get(position).valoracrescimoant = new BigDecimal("0");
            listMaterial.get(position).percdescontoant = new BigDecimal("0");
            listMaterial.get(position).percacrescimoant = new BigDecimal("0");
            listMaterial.get(position).custo = listMaterial.get(position).custooriginal;
            listMaterial.get(position).totalliquido = listMaterial.get(position).totalliquidooriginal;

            CalculoClass calculoClass = new CalculoClass(getActivity(), listMaterial.get(position));
            calculoClass.setTotal();
        }

        if ((listMaterial.get(position).quantidade.floatValue() > 0) || (listMaterial.get(position).quantidade.floatValue() == 0)) {

            setTotal(valorRemovido.negate(), position);

            materialSearchAdapter.notifyItemChanged(position);

            try {
                onAddSelecionado(Misc.cloneMaterial(listMaterial.get(position)));
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }else{
            listMaterial.get(position).quantidade = new BigDecimal("0");
        }
    }

    @Override
    public void onMaterialClick(int position) {
        Material material = null;
        Material materialExclusao = null;

        try {
            material = Misc.cloneMaterial(listMaterial.get(position));
            materialExclusao = Misc.cloneMaterial(listMaterial.get(position));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        listMaterial.get(position).quantidade = listMaterial.get(position).quantidade.add(new BigDecimal("1"));

        if (listMaterial.get(position).quantidade.floatValue() > 1){
            Constants.MOVIMENTO.movimento.totalliquido = Constants.MOVIMENTO.movimento.totalliquido.subtract(calculaTotalExclusao(materialExclusao));
            material.quantidade = listMaterial.get(position).quantidade;
            material = calculaTotalLiquido(material);
        }

        setTotal(material.totalliquido, position);

        materialSearchAdapter.notifyItemChanged(position);

        try {
            onAddSelecionado(Misc.cloneMaterial(listMaterial.get(position)));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMaterialLongClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("material", listMaterial.get(position));

        DialogFragment fragmentModal = MaterialSearchModalFragment.newInstance();
        fragmentModal.setTargetFragment(this, 1);
        fragmentModal.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentModal.show(ft, "materialSearchModalFragment");
    }

    private void AdicionarMaterialQuantidade(Intent data){
        int position = data.getExtras().getInt("position");
        BigDecimal qtdNova = new BigDecimal(String.valueOf(data.getExtras().getFloat("quantidade")));
        BigDecimal valoracrescimo = new BigDecimal(String.valueOf(data.getExtras().getFloat("valoracrescimo")));
        BigDecimal percacrescimo = new BigDecimal(String.valueOf(data.getExtras().getFloat("percacrescimo")));
        BigDecimal valordesconto = new BigDecimal(String.valueOf(data.getExtras().getFloat("valordesconto")));
        BigDecimal percdesconto = new BigDecimal(String.valueOf(data.getExtras().getFloat("percdesconto")));
        BigDecimal custo = new BigDecimal(String.valueOf(data.getExtras().getFloat("custo")));
        BigDecimal qtdAtual = listMaterial.get(position).quantidade;
        BigDecimal vezes;
        boolean excluir = false;

        listMaterial.get(position).valoracrescimoant = listMaterial.get(position).valoracrescimo;
        listMaterial.get(position).valordescontoant = listMaterial.get(position).valordesconto;
        listMaterial.get(position).percacrescimoant = listMaterial.get(position).percacrescimo;
        listMaterial.get(position).percdescontoant = listMaterial.get(position).percdesconto;

        if ((qtdNova.floatValue() >= 1) &&
            ((valoracrescimo.floatValue() != listMaterial.get(position).valoracrescimoant.floatValue()) ||
            (valordesconto.floatValue() != listMaterial.get(position).valordescontoant.floatValue()) ||
            (percacrescimo.floatValue() != listMaterial.get(position).percacrescimoant.floatValue()) ||
            (percdesconto.floatValue() != listMaterial.get(position).percdescontoant.floatValue()) ||
            (custo.floatValue() != listMaterial.get(position).custooriginal.floatValue()))) {

            for (int i = 0; i < qtdAtual.floatValue(); i++) {
                onBotaoExcluirClick(position);
            }

            listMaterial.get(position).percacrescimo = percacrescimo;
            listMaterial.get(position).percdesconto = percdesconto;
            listMaterial.get(position).valoracrescimo = valoracrescimo;
            listMaterial.get(position).valordesconto = valordesconto;
            listMaterial.get(position).custo = custo;

            CalculoClass calculoClass = new CalculoClass(getActivity(), listMaterial.get(position));
            calculoClass.setTotal();

            qtdAtual = new BigDecimal("0");
        }


        if (qtdNova.floatValue() > qtdAtual.floatValue()){
            vezes = qtdNova.subtract(qtdAtual);
        }else{
            excluir = true;
            vezes = qtdAtual.subtract(qtdNova);
        }

        for (int i = 0; i < vezes.floatValue(); i++) {
            if (excluir){
                onBotaoExcluirClick(position);
            }else{
                onMaterialClick(position);
            }
        }
    }
}
