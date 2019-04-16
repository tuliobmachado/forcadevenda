package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;

public class AtualizacaoDAO extends BaseDAO<Atualizacao> {

    private static AtualizacaoDAO dao;

    public static AtualizacaoDAO getInstance(Context context){
        if (dao == null){
            dao = new AtualizacaoDAO(context);
        }

        return dao;
    }

    private AtualizacaoDAO(Context context){
        super();
        super.ctx = context;
    }

    public Atualizacao findByNomeTabela(String nomeTabela){
        List<Atualizacao> listAtualizacao = null;
        Atualizacao atualizacao = null;

        try {
            listAtualizacao = getHelper().getDAO(Atualizacao.class).queryForEq("nometabela", nomeTabela);

            if (listAtualizacao.size() > 0){
                atualizacao = listAtualizacao.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return atualizacao;

    }
}
