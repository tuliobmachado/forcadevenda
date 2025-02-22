package br.com.informsistemas.forcadevenda.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.activity.MainActivity;
import br.com.informsistemas.forcadevenda.adapter.ParceiroVencimentoAdapter;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroVencimentoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;

public class ParceiroVencimentoFragment extends Fragment {

    private List<ParceiroVencimento> listParceiroVencimento;
    private RecyclerView recyclerView;
    private ParceiroVencimentoAdapter parceiroVencimentoAdapter;
    private TabLayout tabLayout;

    @Override
    public void onDestroy() {
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

        getActivity().setTitle("Consulta Parceiro");
        tabLayout = getActivity().findViewById(R.id.tab_layout_parceiro);

        if (tabLayout != null) {
            tabLayout.setVisibility(View.VISIBLE);

            ((MainActivity) getActivity()).onSetIndexMenu(1, 1);
            ((MainActivity) getActivity()).onSetItemMenu();
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        Parceiro parceiro = (Parceiro) getArguments().getSerializable("Parceiro");

        listParceiroVencimento = ParceiroVencimentoDAO.getInstance(getActivity()).findAllByIdAuxiliar("codigoparceiro", parceiro.codigoparceiro);
        setAdapter(listParceiroVencimento);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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

    private void setAdapter(List<ParceiroVencimento> list) {
        parceiroVencimentoAdapter = new ParceiroVencimentoAdapter(getActivity(), list);
        recyclerView.setAdapter(parceiroVencimentoAdapter);
    }
}
