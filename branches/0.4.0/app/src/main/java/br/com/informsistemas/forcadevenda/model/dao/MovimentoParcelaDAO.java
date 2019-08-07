package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoParcela;

public class MovimentoParcelaDAO extends BaseDAO<MovimentoParcela> {

    private static MovimentoParcelaDAO dao;

    public static MovimentoParcelaDAO getInstance(Context context){
        if (dao == null){
            dao = new MovimentoParcelaDAO(context);
        }

        return dao;
    }

    private MovimentoParcelaDAO(Context context){
        super();
        super.ctx = context;
    }

    public List<MovimentoParcela> findByMovimentoId(int id){
        List<MovimentoParcela> items = null;

        try{
            QueryBuilder<Movimento, Object> movimentoQB = getHelper().getDAO(Movimento.class).queryBuilder();
            QueryBuilder<MovimentoParcela, Object> movimentoParcelaQB = getHelper().getDAO(MovimentoParcela.class).queryBuilder();
            movimentoQB.where().eq("id", id);

            items = movimentoParcelaQB.join(movimentoQB).query();
        } catch (SQLException e){
            e.printStackTrace();
            return items;
        }

        return items;
    }
}
