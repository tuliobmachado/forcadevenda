package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "movimentoparcela")
public class MovimentoParcela implements IEntidade {

    @DatabaseField(generatedId = true)
    @Expose
    public Integer id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "movimento_id")
    @Expose(serialize = false)
    public Movimento movimento;

    @DatabaseField
    @Expose
    public String codigoforma;

    @DatabaseField
    @Expose
    public BigDecimal valor;

    @DatabaseField
    @Expose
    public String formadescricao;

    public MovimentoParcela(){}
    public MovimentoParcela(Movimento movimento, String codigoforma, String formadescricao, BigDecimal valor) {
        this.movimento = movimento;
        this.codigoforma = codigoforma;
        this.formadescricao = formadescricao;
        this.valor = valor;
    }
}
