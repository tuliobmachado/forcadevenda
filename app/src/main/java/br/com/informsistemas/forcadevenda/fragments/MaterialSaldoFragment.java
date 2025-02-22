package br.com.informsistemas.forcadevenda.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.activity.MainActivity;
import br.com.informsistemas.forcadevenda.adapter.MaterialSaldoAdapter;
import br.com.informsistemas.forcadevenda.model.dao.MaterialSaldoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;

public class MaterialSaldoFragment extends Fragment {

    private List<MaterialSaldo> listMaterialSaldo;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private MaterialSaldoAdapter materialSaldoAdapter;
    private TabLayout tabLayout;

    @Override
    public void onDestroy() {
        searchView.clearFocus();
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        getActivity().setTitle("Consulta Estoque");

        FloatingActionButton btn = getActivity().findViewById(R.id.fab_adicionar_pedido);
        btn.setVisibility(View.GONE);

        ((MainActivity) getActivity()).onSetIndexMenu(1, 2);
        ((MainActivity) getActivity()).onSetItemMenu();

        tabLayout = getActivity().findViewById(R.id.tab_layout_parceiro);
        tabLayout.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        listMaterialSaldo = MaterialSaldoDAO.getInstance(getActivity()).findAll();
        setAdapter(listMaterialSaldo);

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
                listMaterialSaldo = MaterialSaldoDAO.getInstance(getActivity()).pesquisaLista(newText);
                setAdapter(listMaterialSaldo);
                return false;
            }
        });
//        searchView.setFocusable(true);
//        searchView.setIconified(false);
        searchView.setQueryHint("Pesquisar Material...");

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

    private void setAdapter(List<MaterialSaldo> list) {
        materialSaldoAdapter = new MaterialSaldoAdapter(getActivity(), list);
        recyclerView.setAdapter(materialSaldoAdapter);
    }
}
