package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;

public class CategoriaDAO extends BaseDAO<Categoria> {

    private static CategoriaDAO dao;

    public static CategoriaDAO getInstance(Context context){
        if (dao == null){
            dao = new CategoriaDAO(context);
        }

        return dao;
    }

    private CategoriaDAO(Context context){
        super();
        super.ctx = context;
    }

    public List<Categoria> getListCategoria() {

        List<Categoria> listCategoria = null;
        try {
            listCategoria = new Misc().cloneCategoriaPesquisa(Constants.DTO.listPesquisaCategoria);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return listCategoria;
    }

    public List<Categoria> pesquisaLista(String s){
        List<Categoria> parceiroList = new ArrayList<>();

        for (Categoria p : Constants.DTO.listPesquisaCategoria){
            if (p.descricao.toUpperCase().contains(s.toUpperCase())){
                parceiroList.add(p);
            }
        }

        return parceiroList;
    }

    public List<Categoria> findAllOrderDescricao(){
        List<Categoria> items = new ArrayList<>();

        QueryBuilder<Categoria, Object> queryBuilder = getHelper().getDAO(Categoria.class).queryBuilder();
        try {
            queryBuilder.orderBy("descricao", true);

            items = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
