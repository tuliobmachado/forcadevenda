package br.com.informsistemas.forcadevenda.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.adapter.MovimentoItemAdapter;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoItemDAO;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.utils.RecyclerItemClickListener;

public class MovimentoItemFragment extends Fragment {

    private List<MovimentoItem> listMovimentoItem;
    private RecyclerView recyclerView;
    private MovimentoItemAdapter movimentoItemAdapter;
    private TextView txtTotalItem;
    private RecyclerView.OnItemTouchListener listener;
    private boolean isMultiSelect = false;
    private ActionMode actionMode;
    private ActionMode.Callback callback;
    private List<Integer> selectedIds = new ArrayList<>();
    private List<Material> listMaterialSelecionados;

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
        linearLayout.setVisibility(View.GONE);
        Button btn = getActivity().findViewById(R.id.btn_selecionar_pagamento);
        btn.setVisibility(View.VISIBLE);

        txtTotalItem = getActivity().findViewById(R.id.txt_total_item);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        getMovimentoItem();

        txtTotalItem.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido.floatValue()));

        setAdapter(listMovimentoItem);
        listener = getListener();
        recyclerView.addOnItemTouchListener(listener);

        return view;
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

    private void setAdapter(List<MovimentoItem> list) {
        movimentoItemAdapter = new MovimentoItemAdapter(getActivity(), list);
        recyclerView.setAdapter(movimentoItemAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Material> listAdicionado = (List<Material>) data.getExtras().getSerializable("listMaterial");

        if (listAdicionado != null) {

            MovimentoDAO.getInstance(getActivity()).createOrUpdate(Constants.MOVIMENTO.movimento);

            for (int i = 0; i < listAdicionado.size(); i++) {
                if (listAdicionado.get(i).quantidade.floatValue() > 0) {

                    MovimentoItem movimentoItem = checaMaterialMovimento(listAdicionado.get(i).codigomaterial);

                    Material material = null;
                    try {
                        material = Misc.cloneMaterial(listAdicionado.get(i));
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    if (movimentoItem == null) {
                        movimentoItem = new MovimentoItem(Constants.MOVIMENTO.movimento, Constants.MOVIMENTO.codigotabelapreco,
                                material.codigomaterial, material.descricao, material.unidadesaida, material.unidadesaida, new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
                                new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"));
                    }

                    CalculaMovimentoItem(movimentoItem, material);
                    MovimentoItemDAO.getInstance(getActivity()).createOrUpdate(movimentoItem);
                }
            }
        }
    }

    private void CalculaMovimentoItem(MovimentoItem movItem, Material material) {
        material.precocalculado = material.precocalculado.multiply(material.quantidade);
        CalculoClass calculoClass = new CalculoClass(getActivity(), material);
        calculoClass.setTributos();

        movItem.codigotabelapreco = Constants.MOVIMENTO.codigotabelapreco;
        movItem.quantidade = material.quantidade;
        movItem.custo = material.custo;
        movItem.totalitem = material.quantidade.multiply(material.custo);
        movItem.icms = material.icms;
        movItem.valoricms = material.valoricms;
        movItem.baseicms = material.baseicms;
        movItem.icmssubst = material.icmssubst;
        movItem.baseicmssubst = material.baseicmssubst;
        movItem.valoricmssubst = material.valoricmssubst;
        movItem.ipi = material.ipi;
        movItem.valoripi = material.valoripi;
        movItem.margemsubstituicao = material.margemsubstituicao;
        movItem.pautafiscal = material.pautafiscal;
        movItem.icmsfecoep = material.icmsfecoep;
        movItem.valoricmsfecoep = material.valoricmsfecoep;
        movItem.icmsfecoepst = material.icmsfecoepst;
        movItem.valoricmsfecoepst = material.valoricmsfecoepst;
        movItem.valoracrescimoitem = material.valoracrescimo.multiply(material.quantidade);
        movItem.percacrescimoitem = material.percacrescimo;
        movItem.valordescontoitem = material.valordesconto.multiply(material.quantidade);
        movItem.percdescontoitem = material.percdesconto;
        movItem.totalliquido = material.totalliquido;
        movItem.custooriginal = material.custooriginal;
    }

    private MovimentoItem checaMaterialMovimento(String codigomaterial) {
        MovimentoItem movimentoItem = null;

        movimentoItem = MovimentoItemDAO.getInstance(getActivity()).findByMovimentoIdItem(Constants.MOVIMENTO.movimento.id, codigomaterial);

        return movimentoItem;
    }

    private void getMovimentoItem() {
        if (Constants.MOVIMENTO.movimento.id == null) {
            listMovimentoItem = new ArrayList<>();
        } else {
            listMovimentoItem = MovimentoItemDAO.getInstance(getActivity()).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

            if (listMovimentoItem.size() > 0) {

                if (movimentoItemAdapter == null) {
                    listMaterialSelecionados = new ArrayList<>();

                    for (int i = 0; i < listMovimentoItem.size(); i++) {
                        for (int j = 0; j < Constants.DTO.listMaterialPreco.size(); j++) {
                            if (listMovimentoItem.get(i).codigomaterial.equals(Constants.DTO.listMaterialPreco.get(j).codigomaterial)) {

                                Constants.DTO.listMaterialPreco.get(j).percacrescimo = listMovimentoItem.get(i).percacrescimoitem;
                                Constants.DTO.listMaterialPreco.get(j).percdesconto = listMovimentoItem.get(i).percdescontoitem;

                                if (listMovimentoItem.get(i).valoracrescimoitem.floatValue() > 0){
                                    Constants.DTO.listMaterialPreco.get(j).valoracrescimo = listMovimentoItem.get(i).valoracrescimoitem.divide(listMovimentoItem.get(i).quantidade, Constants.DTO.registro.casasvalor, BigDecimal.ROUND_HALF_EVEN);
                                }

                                if (listMovimentoItem.get(i).valordescontoitem.floatValue() > 0){
                                    Constants.DTO.listMaterialPreco.get(j).valordesconto = listMovimentoItem.get(i).valordescontoitem.divide(listMovimentoItem.get(i).quantidade, Constants.DTO.registro.casasvalor, BigDecimal.ROUND_HALF_EVEN);
                                }

                                Constants.DTO.listMaterialPreco.get(j).valoracrescimoant = listMovimentoItem.get(i).valoracrescimoitem;
                                Constants.DTO.listMaterialPreco.get(j).percacrescimoant = listMovimentoItem.get(i).percacrescimoitem;
                                Constants.DTO.listMaterialPreco.get(j).valordescontoant = listMovimentoItem.get(i).valordescontoitem;
                                Constants.DTO.listMaterialPreco.get(j).percdescontoant = listMovimentoItem.get(i).percdescontoitem;

                                if (listMovimentoItem.get(i).custo != Constants.DTO.listMaterialPreco.get(j).custooriginal){
                                    Constants.DTO.listMaterialPreco.get(j).custo = listMovimentoItem.get(i).custo;
                                }

                                CalculoClass calculoClass = new CalculoClass(getActivity(), Constants.DTO.listMaterialPreco.get(j));
                                calculoClass.setTotal();

                                Constants.DTO.listMaterialPreco.get(j).quantidade = listMovimentoItem.get(i).quantidade;
                                try {
                                    listMaterialSelecionados.add(Misc.cloneMaterial(Constants.DTO.listMaterialPreco.get(j)));
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }

                                break;
                            }
                        }
                    }
                }else{
                    listMaterialSelecionados = null;
                }

                CalculoClass calculoClass = new CalculoClass(getActivity(), null);
                calculoClass.recalcularMovimento(Constants.MOVIMENTO.movimento, listMovimentoItem);
            }
        }
    }

    private RecyclerView.OnItemTouchListener getListener() {
        callback = getCallback();
        return new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    multiSelect(listMovimentoItem.get(position).id);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null) {
                        actionMode = getActivity().startActionMode(callback);
                    }
                }

                multiSelect(listMovimentoItem.get(position).id);
            }
        });
    }

    private ActionMode.Callback getCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:

                        for (int i : selectedIds) {
                            for (int y = 0; y < listMovimentoItem.size(); y++) {
                                if (listMovimentoItem.get(y).id == i) {
                                    deleteItem(y);

                                }
                            }
                        }
                        mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                isMultiSelect = false;
                selectedIds = new ArrayList<>();
                movimentoItemAdapter.setSelectedIds(selectedIds);
            }
        };
    }

    private void deleteItem(int position) {
        Constants.MOVIMENTO.movimento.totalliquido = Constants.MOVIMENTO.movimento.totalliquido.subtract(listMovimentoItem.get(position).totalliquido);
        txtTotalItem.setText("R$ " + Misc.formatMoeda(Constants.MOVIMENTO.movimento.totalliquido.floatValue()));
        removeQuantidadeLista(listMovimentoItem.get(position));
        MovimentoItemDAO.getInstance(getActivity()).delete(listMovimentoItem.get(position));
        listMovimentoItem.remove(position);
        movimentoItemAdapter.notifyItemRemoved(position);
        movimentoItemAdapter.notifyItemRangeChanged(position, movimentoItemAdapter.getItemCount());

        if (Constants.MOVIMENTO.movimento.totalliquido.floatValue() < 0) {
            Constants.MOVIMENTO.movimento.totalliquido = new BigDecimal("0");
        }

        MovimentoDAO.getInstance(getActivity()).createOrUpdate(Constants.MOVIMENTO.movimento);
    }

    private void multiSelect(int position) {
        if (actionMode != null) {
            if (selectedIds.contains(position)) {
                for (int i = 0; i < selectedIds.size(); i++) {
                    if (selectedIds.get(i) == position)
                        selectedIds.remove(i);
                }
            } else {
                selectedIds.add(position);
            }

            if (selectedIds.size() > 0) {
                actionMode.setTitle(String.valueOf(selectedIds.size()));
            } else {
                actionMode.setTitle("");
                actionMode.finish();
            }

            movimentoItemAdapter.setSelectedIds(selectedIds);
        }
    }

    public List<Material> getListMaterialSelecionados() {
        return listMaterialSelecionados;
    }

    private void removeQuantidadeLista(MovimentoItem movimentoItem){
        for (int i = 0; i < Constants.DTO.listMaterialPreco.size(); i++) {
            if (Constants.DTO.listMaterialPreco.get(i).codigomaterial.equals(movimentoItem.codigomaterial)){
                Constants.DTO.listMaterialPreco.get(i).quantidade = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).valordesconto = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).valoracrescimo = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).percdesconto = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).percacrescimo = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).valordescontoant = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).valoracrescimoant = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).percdescontoant = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).percacrescimoant = new BigDecimal("0");
                Constants.DTO.listMaterialPreco.get(i).custo = Constants.DTO.listMaterialPreco.get(i).custooriginal;
                Constants.DTO.listMaterialPreco.get(i).totalliquido = Constants.DTO.listMaterialPreco.get(i).totalliquidooriginal;

                CalculoClass calculoClass = new CalculoClass(getActivity(), Constants.DTO.listMaterialPreco.get(i));
                calculoClass.setTotal();
                break;
            }
        }

        if (listMaterialSelecionados != null) {
            for (int i = 0; i < listMaterialSelecionados.size(); i++) {
                if (listMaterialSelecionados.get(i).codigomaterial.equals(movimentoItem.codigomaterial)) {
                    listMaterialSelecionados.remove(i);
                    break;
                }
            }
        }
    }
}
