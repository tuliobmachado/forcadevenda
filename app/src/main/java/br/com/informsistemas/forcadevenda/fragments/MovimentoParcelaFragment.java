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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.adapter.MovimentoParcelaAdapter;
import br.com.informsistemas.forcadevenda.model.dao.FormaPagamentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoItemDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoParcelaDAO;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoParcela;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.utils.RecyclerItemClickListener;

public class MovimentoParcelaFragment extends Fragment {

    private List<MovimentoParcela> listMovimentoParcela;
    private RecyclerView recyclerView;
    private MovimentoParcelaAdapter movimentoParcelaAdapter;
    private RecyclerView.OnItemTouchListener listener;
    private boolean isMultiSelect = false;
    private ActionMode actionMode;
    private ActionMode.Callback callback;
    private List<Integer> selectedIds = new ArrayList<>();
    private Button btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        btn = getActivity().findViewById(R.id.btn_resumo_pedido);
        btn.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        getMovimentoParcela();

        setAdapter(listMovimentoParcela);
        listener = getListener();
        recyclerView.addOnItemTouchListener(listener);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (listMovimentoParcela.size() > 0) {
            menu.clear();
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
        FormaPagamento p = (FormaPagamento) data.getExtras().getSerializable("Pagamento");

        MovimentoParcela movimentoParcela = new MovimentoParcela(Constants.MOVIMENTO.movimento, p.codigoforma, p.descricao, Constants.MOVIMENTO.movimento.totalliquido);
        MovimentoParcelaDAO.getInstance(getActivity()).createOrUpdate(movimentoParcela);
    }

    private void getMovimentoParcela(){
        RecalcularTotalMovimento();

        listMovimentoParcela = MovimentoParcelaDAO.getInstance(getActivity()).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

        if (listMovimentoParcela.size() == 0){
            if (!Constants.MOVIMENTO.codigoformapagamento.equals("")){
                insereCodigoFormaPadrao();
            }
        }else{
            if (listMovimentoParcela.get(0).valor != Constants.MOVIMENTO.movimento.totalliquido){
                listMovimentoParcela.get(0).valor = Constants.MOVIMENTO.movimento.totalliquido;

                MovimentoParcelaDAO.getInstance(getActivity()).createOrUpdate(listMovimentoParcela.get(0));
            }
        }
    }

    private void setAdapter(List<MovimentoParcela> list){
        movimentoParcelaAdapter = new MovimentoParcelaAdapter(getActivity(), list);
        recyclerView.setAdapter(movimentoParcelaAdapter);
    }

    private RecyclerView.OnItemTouchListener getListener(){
        callback = getCallback();
        return  new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    multiSelect(listMovimentoParcela.get(position).id);
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

                multiSelect(listMovimentoParcela.get(position).id);
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
                            for (int y = 0; y < listMovimentoParcela.size(); y++) {
                                if (listMovimentoParcela.get(y).id == i){
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
                movimentoParcelaAdapter.setSelectedIds(selectedIds);
            }
        };
    }

    private void deleteItem(int position){
        MovimentoParcelaDAO.getInstance(getActivity()).delete(listMovimentoParcela.get(position));
        listMovimentoParcela.remove(position);
        movimentoParcelaAdapter.notifyItemRemoved(position);
        movimentoParcelaAdapter.notifyItemRangeChanged(position, movimentoParcelaAdapter.getItemCount());
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

            movimentoParcelaAdapter.setSelectedIds(selectedIds);
        }
    }

    private void insereCodigoFormaPadrao(){
        FormaPagamento p = FormaPagamentoDAO.getInstance(getActivity()).findByIdAuxiliar("codigoforma", Constants.MOVIMENTO.codigoformapagamento);
        MovimentoParcela movimentoParcela = new MovimentoParcela(Constants.MOVIMENTO.movimento, Constants.MOVIMENTO.codigoformapagamento, p.descricao, Constants.MOVIMENTO.movimento.totalliquido);
        MovimentoParcelaDAO.getInstance(getActivity()).createOrUpdate(movimentoParcela);
        listMovimentoParcela.add(movimentoParcela);
        btn.setVisibility(View.VISIBLE);
    }

    private void RecalcularTotalMovimento(){
        List<MovimentoItem> listMovItens = MovimentoItemDAO.getInstance(getActivity()).findByMovimentoId(Constants.MOVIMENTO.movimento.id);

        if (listMovItens.size() > 0) {
            CalculoClass calculoClass = new CalculoClass(getActivity(), null);
            calculoClass.recalcularMovimento(Constants.MOVIMENTO.movimento, listMovItens);
        }
    }
}
