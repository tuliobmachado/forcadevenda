package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.Registro;

public class RegistroDAO extends BaseDAO<Registro> {

    private static RegistroDAO dao;

    public static RegistroDAO getInstance(Context context){
        if (dao == null){
            dao = new RegistroDAO(context);
        }

        return dao;
    }

    private RegistroDAO(Context context){
        super();
        super.ctx = context;
    }

}
