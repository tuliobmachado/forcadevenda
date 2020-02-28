package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;
import br.com.informsistemas.forcadevenda.model.pojo.Parametro;

public class ParametroDAO extends BaseDAO<Parametro> {

    private static ParametroDAO dao;

    public static ParametroDAO getInstance(Context context){
        if (dao == null){
            dao = new ParametroDAO(context);
        }

        return dao;
    }

    private ParametroDAO(Context context){
        super();
        super.ctx = context;
    }
}
