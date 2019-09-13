package br.com.informsistemas.forcadevenda.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.adapter.PagamentoSearchAdapter;
import br.com.informsistemas.forcadevenda.model.dao.FormaPagamentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.utils.ItemClickListener;

public class FormaPagamentoSearchFragment extends Fragment implements ItemClickListener {

    private List<FormaPagamento> listPagamento;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private PagamentoSearchAdapter pagamentoSearchAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        Button btn = getActivity().findViewById(R.id.btn_resumo_pedido);
        btn.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        Parceiro parceiro = ParceiroDAO.getInstance(getActivity()).findByIdAuxiliar("codigoparceiro", Constants.MOVIMENTO.movimento.codigoparceiro);

        if (parceiro.formaspermitidas.equals("")) {
            listPagamento = FormaPagamentoDAO.getInstance(getActivity()).findAll();
        }else{
            listPagamento = FormaPagamentoDAO.getInstance(getActivity()).findByFormasPermitidas(parceiro.formaspermitidas);
        }

        setAdapter(listPagamento);

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
                listPagamento = FormaPagamentoDAO.getInstance(getActivity()).pesquisaLista(newText);
                setAdapter(listPagamento);
                return false;
            }
        });
//        searchView.setFocusable(true);
//        searchView.setIconified(false);
        searchView.setQueryHint("Pesquisar Pagamento...");

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                searchView.clearFocus();
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setAdapter(List<FormaPagamento> list){
        pagamentoSearchAdapter = new PagamentoSearchAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pagamentoSearchAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Pagamento", listPagamento.get(position));
        intent.putExtras(bundle);
        searchView.clearFocus();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onItemClickLong(int position) {

    }
}
