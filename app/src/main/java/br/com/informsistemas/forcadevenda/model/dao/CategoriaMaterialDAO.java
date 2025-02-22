package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import br.com.informsistemas.forcadevenda.model.pojo.CategoriaMaterial;

public class CategoriaMaterialDAO extends BaseDAO<CategoriaMaterial> {

    private static CategoriaMaterialDAO dao;

    public static CategoriaMaterialDAO getInstance(Context context){
        if (dao == null){
            dao = new CategoriaMaterialDAO(context);
        }

        return dao;
    }

    private CategoriaMaterialDAO(Context context){
        super();
        super.ctx = context;
    }
}
