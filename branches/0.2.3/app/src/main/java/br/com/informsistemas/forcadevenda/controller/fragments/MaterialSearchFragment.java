package br.com.informsistemas.forcadevenda.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import br.com.informsistemas.forcadevenda.model.utils.ItemClickListener;

public class MaterialSearchFragment extends Fragment implements IOnBackPressed {

    private SearchView searchView;
    private List<Material> listMaterial;
    private List<MovimentoItem> listMovimentoItem;
    private List<Material> listSelecionado;
    private List<Integer> listIds;
    private RecyclerView recyclerView;
    private MaterialSearchAdapter materialSearchAdapter;
    private TextView txtCategoriaSelecionada;
    private TextView txtTotalItem;
    private TabLayout tabLayout;
    private CategoriaFragment categoriaFragment;
    private Categoria categoriaFiltro;

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

        txtTotalItem = getActivity().findViewById(R.id.txt_total_item);
        txtTotalItem.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido));

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        listMovimentoItem = (List<MovimentoItem>) getArguments().getSerializable("listMovimentoItem");
        getArguments().clear();

        if (categoriaFiltro != null) {

            if (listMovimentoItem == null) {
                listMovimentoItem = new ArrayList<>();
            }

            if (categoriaFiltro.id > 1) {
                listMaterial = MaterialDAO.getInstance(getActivity()).aplicarFiltro(categoriaFiltro.codigogrupo);
            } else {
                listMaterial = MaterialDAO.getInstance(getActivity()).getListMaterial();
            }

            txtCategoriaSelecionada.setText(categoriaFiltro.descricao);

            if (listSelecionado != null) {
                for (int i = 0; i < listSelecionado.size(); i++) {
                    listMovimentoItem.add(new MovimentoItem(null, Constants.MOVIMENTO.codigotabelapreco,
                            listSelecionado.get(i).codigomaterial, listSelecionado.get(i).unidadesaida,
                            listSelecionado.get(i).quantidade, listSelecionado.get(i).custo, 0,
                            listSelecionado.get(i).icms, listSelecionado.get(i).baseicms, listSelecionado.get(i).valoricms,
                            listSelecionado.get(i).icmssubst, listSelecionado.get(i).baseicmssubst, listSelecionado.get(i).valoricmssubst,
                            listSelecionado.get(i).ipi, listSelecionado.get(i).valoripi,
                            listSelecionado.get(i).margemsubstituicao, listSelecionado.get(i).pautafiscal,
                            listSelecionado.get(i).icmsfecoep, listSelecionado.get(i).valoricmsfecoep,
                            listSelecionado.get(i).icmsfecoepst, listSelecionado.get(i).valoricmsfecoepst,
                            (listSelecionado.get(i).custo * listSelecionado.get(i).quantidade) +
                            (listSelecionado.get(i).valoricmssubst + listSelecionado.get(i).valoripi + listSelecionado.get(i).valoricmsfecoepst)));

                    addQuantidadeList(listSelecionado.get(i).codigomaterial, listSelecionado.get(i).quantidade);
                }
            }
        } else {
            txtCategoriaSelecionada.setText("TODAS");
            listMaterial = MaterialDAO.getInstance(getActivity()).getListMaterial();

            if (listMovimentoItem != null) {
                for (int i = 0; i < listMovimentoItem.size(); i++) {
                    addListSelecionadoMovItem(listMovimentoItem.get(i));
                }
            }
        }

        MaterialDAO.getInstance(getActivity()).setListPesquisa(getListPesquisa(listMaterial));
        setAdapter(listMaterial, false);

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
                listMaterial = MaterialDAO.getInstance(getActivity()).pesquisaLista(newText);
                setAdapter(listMaterial, true);
                return false;
            }
        });
//        searchView.setFocusable(true);
//        searchView.setIconified(false);
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

    private void setAdapter(List<Material> list, Boolean Pesquisa) {
        materialSearchAdapter = new MaterialSearchAdapter(getActivity(), list);
        if (Pesquisa) {
            materialSearchAdapter.setfListSearch(getListPesquisa(listSelecionado));
        }
        materialSearchAdapter.setfListSelecionado(listMovimentoItem);
        materialSearchAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Material material = null;
                Material materialexclusao = null;
                float valorQtdTotal = 0;
                float valorQtdReduzida = 0;
                float valorRemovido = 0;
                switch (view.getId()) {
                    case R.id.img_excluir:

                        try {
                            material = Misc.cloneMaterial(listMaterial.get(position));
                            materialexclusao = Misc.cloneMaterial(listMaterial.get(position));

                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        listMaterial.get(position).quantidade = listMaterial.get(position).quantidade - 1;

                        if (listMaterial.get(position).quantidade >= 1){
                            materialexclusao.quantidade = listMaterial.get(position).quantidade;

                            valorQtdTotal = calculaTotalExclusao(material);
                            valorQtdReduzida = calculaTotalExclusao(materialexclusao);
                            valorRemovido = valorQtdTotal - valorQtdReduzida;
                        }else{
                            valorRemovido = listMaterial.get(position).totalliquido;
                        }

                        if (listMaterial.get(position).quantidade == 0){
                            removeMovimentoItem(listMaterial.get(position).codigomaterial);
                        }

                        setTotal(-valorRemovido);

                        break;
                    default:

                        try {
                            material = Misc.cloneMaterial(listMaterial.get(position));
                            materialexclusao = Misc.cloneMaterial(listMaterial.get(position));

                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        listMaterial.get(position).quantidade = listMaterial.get(position).quantidade + 1;

                        if (listMaterial.get(position).quantidade > 1){
                            Constants.MOVIMENTO.movimento.totalliquido = Constants.MOVIMENTO.movimento.totalliquido - calculaTotalExclusao(materialexclusao);
                            material = calculaTotalLiquido(listMaterial.get(position));
                        }

                        setTotal(material.totalliquido);

                        break;
                }

                addListSelecionado(listMaterial.get(position));
                materialSearchAdapter.notifyItemChanged(position);
            }
        });
        recyclerView.setAdapter(materialSearchAdapter);
    }

    private void setTotal(float valor) {
        float valorAtual = Constants.MOVIMENTO.movimento.totalliquido;

        valorAtual = valorAtual + valor;

        if (valorAtual < 0){
            valorAtual = 0;
        }

        txtTotalItem.setText("R$ " + Misc.formatMoeda(valorAtual));

        Constants.MOVIMENTO.movimento.totalliquido = valorAtual;
    }

    private Intent getIntent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("listMaterial", (Serializable) listSelecionado);
        intent.putExtras(bundle);

        return intent;
    }

    private void addListSelecionado(Material material) {
        boolean remover = false;

        if (listIds == null) {
            listIds = new ArrayList<>();
        }

        if (listSelecionado == null) {
            listSelecionado = new ArrayList<>();
        }

        if (listIds.contains(material.id)) {

            if (material.quantidade == 0) {
                remover = true;
            }

            for (int i = 0; i < listSelecionado.size(); i++) {
                if (material.id == listSelecionado.get(i).id) {
                    if (remover) {
                        listIds.remove(material.id);
                        listSelecionado.remove(i);
                    } else {
                        listSelecionado.set(i, material);
                    }
                }
            }
        } else {
            listIds.add(material.id);
            listSelecionado.add(material);
        }
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
        categoriaFiltro = (Categoria) data.getExtras().getSerializable("Categoria");
        listMovimentoItem = null;

        tabLayout.getTabAt(0).select();
    }

    private Bundle getCategoriaFiltro() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Categoria", categoriaFiltro);

        return bundle;
    }

    private Material calculaTotalLiquido(Material material){
        material.precovenda1 = (material.custo * material.quantidade);
        CalculoClass.getTributos(getActivity(), material);

        return material;
    }

    private float calculaTotalExclusao(Material m){
        m.precovenda1 = (m.custo * m.quantidade);
        CalculoClass.getTributos(getActivity(), m);

        return m.totalliquido;
    }

    private void removeMovimentoItem(String codigomaterial){
        if (Constants.MOVIMENTO.movimento.id != null) {
            MovimentoItem movimentoItem = MovimentoItemDAO.getInstance(getActivity()).findByMovimentoIdItem(Constants.MOVIMENTO.movimento.id, codigomaterial);

            if (movimentoItem != null){
                MovimentoItemDAO.getInstance(getActivity()).deleteByMovimentoId(Constants.MOVIMENTO.movimento.id, codigomaterial);
            }
        }
    }

    private void addQuantidadeList(String codigomaterial, float quantidade){
        for (int i = 0; i < listMaterial.size(); i++) {
            if (listMaterial.get(i).codigomaterial.equals(codigomaterial)){
                listMaterial.get(i).quantidade = quantidade;
            }

        }
    }

    private void addListSelecionadoMovItem(MovimentoItem movimentoItem) {
        Material material = null;
        try {
            material = Misc.cloneMaterial(MaterialDAO.getInstance(getActivity()).findByIdAuxiliar("codigomaterial", movimentoItem.codigomaterial));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if (material.quantidade == 0) {
            material.precovenda1 = CalculoClass.getPrecoVenda(getActivity(), material);
            material.quantidade = 1;
            CalculoClass.getTributos(getActivity(), material);
            material.quantidade = movimentoItem.quantidade;
        }

        addListSelecionado(material);
    }

    private List<Material> getListPesquisa(List<Material> materials){
        List<Material> materialList = new ArrayList<>();

        if (materials != null) {
            for (Material m : materials) {
                try {
                    materialList.add(Misc.cloneMaterial(m));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        return materialList;
    }
}
