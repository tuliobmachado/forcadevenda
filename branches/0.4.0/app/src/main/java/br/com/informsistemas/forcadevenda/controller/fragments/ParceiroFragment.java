package br.com.informsistemas.forcadevenda.controller.fragments;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.adapter.ParceiroAdapter;
import br.com.informsistemas.forcadevenda.model.dao.FormaPagamentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.utils.RecyclerItemClickListener;

public class ParceiroFragment extends Fragment {

    public Button btnSelecionarProduto;
    private List<Parceiro> listParceiro;
    private RecyclerView recyclerView;
    private ParceiroAdapter parceiroAdapter;
    private RecyclerView.OnItemTouchListener listener;
    private boolean isMultiSelect = false;
    private ActionMode actionMode;
    private ActionMode.Callback callback;
    private List<Integer> selectedIds = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        btnSelecionarProduto = getActivity().findViewById(R.id.btn_selecionar_produto);
        btnSelecionarProduto.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        getParceiro();

        setAdapter(listParceiro);
        listener = getListener();
        recyclerView.addOnItemTouchListener(listener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().setTitle("Parceiros");

        if (listParceiro.size() > 0) {
            if (listParceiro.get(0).statusvencimento.equals("")) {
                menu.clear();
            }else{
                MenuItem menuItem = menu.findItem(R.id.action_search_list);
                menuItem.setVisible(false);
            }
        }else{
            MenuItem menuItem = menu.findItem(R.id.action_titulos);
            menuItem.setVisible(false);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Parceiro p = (Parceiro) data.getExtras().getSerializable("Parceiro");
        Constants.MOVIMENTO.movimento.codigoparceiro = p.codigoparceiro;

        getTabelas(p);

        listParceiro.add(p);
    }

    private void getParceiro(){
        if (listParceiro == null){
            listParceiro = new ArrayList<>();
        }

        if (Constants.MOVIMENTO.movimento.id != null){
            Parceiro p = ParceiroDAO.getInstance(getActivity()).findByIdAuxiliar("codigoparceiro", Constants.MOVIMENTO.movimento.codigoparceiro);

            getTabelas(p);

            listParceiro.add(p);
        }
    }

    private void setAdapter(List<Parceiro> list){
        parceiroAdapter = new ParceiroAdapter(getActivity(), list);
        recyclerView.setAdapter(parceiroAdapter);
    }

    private RecyclerView.OnItemTouchListener getListener(){
        callback = getCallback();
        return  new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    multiSelect(listParceiro.get(position).id);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        actionMode = getActivity().startActionMode(callback);
                    }
                }

                multiSelect(listParceiro.get(position).id);
            }
        });
    }

    private ActionMode.Callback getCallback(){
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
                switch (item.getItemId()){
                    case R.id.action_delete:

                        for (int i : selectedIds){
                            for (int y = 0; y < listParceiro.size(); y++) {
                                if (listParceiro.get(y).id == i){
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
                parceiroAdapter.setSelectedIds(selectedIds);
            }
        };
    }

    private void deleteItem(int position){
        listParceiro.remove(position);
        parceiroAdapter.notifyItemRemoved(position);
        parceiroAdapter.notifyItemRangeChanged(position, parceiroAdapter.getItemCount());
        Constants.MOVIMENTO.movimento.codigoparceiro = null;
        Misc.setTabelasPadrao();
        getActivity().invalidateOptionsMenu();
    }

    private void multiSelect(int position){
        if (actionMode != null){
            if (selectedIds.contains(position)) {
                for (int i = 0; i < selectedIds.size(); i++) {
                    if (selectedIds.get(i) == position)
                        selectedIds.remove(i);
                }
            }else {
                selectedIds.add(position);
            }

            if (selectedIds.size() > 0){
                actionMode.setTitle(String.valueOf(selectedIds.size()));
            }else{
                actionMode.setTitle("");
                actionMode.finish();
            }

            parceiroAdapter.setSelectedIds(selectedIds);
        }
    }

    private void getTabelas(Parceiro p){
        if (!p.codigotabelapreco.equals("")){
            Constants.MOVIMENTO.codigotabelapreco = p.codigotabelapreco;
            Constants.MOVIMENTO.movimento.codigotabelapreco = p.codigotabelapreco;
        }

        if (!p.codigoformapagamento.equals("")){
            if (FormaPagamentoDAO.getInstance(getActivity()).findByIdAuxiliar("codigoforma", p.codigoformapagamento) != null) {
                Constants.MOVIMENTO.codigoformapagamento = p.codigoformapagamento;
            }
        }

        if (p.percdescontopadrao.floatValue() > 0){
            Constants.MOVIMENTO.percdescontopadrao = p.percdescontopadrao;
        }

        Constants.MOVIMENTO.estadoParceiro = p.estado;
    }

    public Parceiro onGetParceiroSelecionado(){
        return listParceiro.get(0);
    }
}
