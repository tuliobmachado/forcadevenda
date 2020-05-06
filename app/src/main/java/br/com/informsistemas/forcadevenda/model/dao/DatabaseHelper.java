package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;
import br.com.informsistemas.forcadevenda.model.pojo.CategoriaMaterial;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.pojo.MetaFuncionario;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoParcela;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.Registro;
import br.com.informsistemas.forcadevenda.model.pojo.TabelaPrecoItem;
import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String databaseName = "forcavenda.db";
    private static final int databaseVersion = 3;

    private Map<Class, Dao<IEntidade, Object>> daos = new HashMap<Class, Dao<IEntidade, Object>>();

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, Atualizacao.class);
            TableUtils.createTable(cs, Registro.class);
            TableUtils.createTable(cs, Parceiro.class);
            TableUtils.createTable(cs, ParceiroVencimento.class);
            TableUtils.createTable(cs, Categoria.class);
            TableUtils.createTable(cs, CategoriaMaterial.class);
            TableUtils.createTable(cs, Material.class);
            TableUtils.createTable(cs, MaterialEstado.class);
            TableUtils.createTable(cs, MaterialSaldo.class);
            TableUtils.createTable(cs, TabelaPrecoItem.class);
            TableUtils.createTable(cs, FormaPagamento.class);
            TableUtils.createTable(cs, Movimento.class);
            TableUtils.createTable(cs, MovimentoItem.class);
            TableUtils.createTable(cs, MovimentoParcela.class);
            TableUtils.createTable(cs, MetaFuncionario.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> Dao<T, Object> getDAO(Class<T> entidadeClass) {
        Dao<IEntidade, Object> dao = null;
        if (daos.get(entidadeClass) == null) {
            try {
                dao = getDao((Class) entidadeClass);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "exception during getDAO", e);
                throw new RuntimeException(e);
            }
            daos.put(entidadeClass, dao);
        }

        return (Dao<T, Object>) daos.get(entidadeClass);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {
        List<String> allSQL = new ArrayList<>();

        switch (oldVersion) {
            case 1:
                allSQL.add("PRAGMA foreign_keys=off");
                allSQL.add("BEGIN TRANSACTION");
                allSQL.add("ALTER TABLE movimento RENAME TO movimento_old");
                allSQL.add("CREATE TABLE movimento (`MD5` VARCHAR , `codigoalmoxarifado` VARCHAR , `codigoempresa` VARCHAR , `codigofilialcontabil` VARCHAR , `codigooperacao` VARCHAR , `codigoparceiro` VARCHAR , `codigotabelapreco` VARCHAR , `cpfcgc` VARCHAR , `data` VARCHAR , `dataalteracao` VARCHAR , `datafim` VARCHAR , `datainicio` VARCHAR , `descricaoparceiro` VARCHAR , `id` INTEGER PRIMARY KEY AUTOINCREMENT , `latitude` DOUBLE PRECISION , `longitude` DOUBLE PRECISION , `enviolatitude` DOUBLE PRECISION , `enviolongitude` DOUBLE PRECISION , `observacao` VARCHAR , `sincronizado` VARCHAR , `totalliquido` VARCHAR)");
                allSQL.add("INSERT INTO movimento (id, codigoempresa, codigofilialcontabil, codigoalmoxarifado, codigoparceiro, codigooperacao, codigotabelapreco, observacao, totalliquido, sincronizado, data, datainicio, datafim, dataalteracao, MD5, descricaoparceiro, cpfcgc, longitude, latitude, enviolongitude, enviolatitude) SELECT id, codigoempresa, codigofilialcontabil, codigoalmoxarifado, codigoparceiro, codigooperacao, codigotabelapreco, observacao, totalliquido, sincronizado, data, datainicio, datafim, dataalteracao, MD5, descricaoparceiro, cpfcgc, 0, 0, 0, 0 FROM movimento_old");
                allSQL.add("DROP TABLE movimento_old");
                allSQL.add("COMMIT");
                allSQL.add("PRAGMA foreign_keys=on");
                allSQL.add("ALTER TABLE movimento ADD COLUMN atualizarlocalizacao VARCHAR");
                allSQL.add("UPDATE movimento set atualizarlocalizacao = 'F'");
                allSQL.add("ALTER TABLE cadparceiro ADD COLUMN longitude DOUBLE PRECISION");
                allSQL.add("ALTER TABLE cadparceiro ADD COLUMN latitude DOUBLE PRECISION");
            case 2:
                allSQL.add("ALTER TABLE materialsaldo ADD COLUMN codigoauxiliar VARCHAR");
                allSQL.add("ALTER TABLE registro ADD COLUMN sincroniaautomatica INTEGER DEFAULT 0");
        }

        for (String sql : allSQL) {
            db.execSQL(sql);
        }
    }

    public void onDeleteAllTable() {
        ConnectionSource cs = this.getConnectionSource();

        try {
            TableUtils.dropTable(cs, MetaFuncionario.class, true);
            TableUtils.dropTable(cs, MovimentoParcela.class, true);
            TableUtils.dropTable(cs, MovimentoItem.class, true);
            TableUtils.dropTable(cs, Movimento.class, true);
            TableUtils.dropTable(cs, FormaPagamento.class, true);
            TableUtils.dropTable(cs, TabelaPrecoItem.class, true);
            TableUtils.dropTable(cs, MaterialSaldo.class, true);
            TableUtils.dropTable(cs, MaterialEstado.class, true);
            TableUtils.dropTable(cs, Material.class, true);
            TableUtils.dropTable(cs, CategoriaMaterial.class, true);
            TableUtils.dropTable(cs, Categoria.class, true);
            TableUtils.dropTable(cs, ParceiroVencimento.class, true);
            TableUtils.dropTable(cs, Parceiro.class, true);
            TableUtils.dropTable(cs, Registro.class, true);
            TableUtils.dropTable(cs, Atualizacao.class, true);

            onCreate(this.getWritableDatabase(), cs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
