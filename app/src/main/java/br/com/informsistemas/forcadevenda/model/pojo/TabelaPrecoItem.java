package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "tabelaprecoitem")
public class TabelaPrecoItem implements IEntidade {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField
    public String codigotabelapreco;

    @DatabaseField
    public String codigotabelaprecoitem;

    @DatabaseField
    public BigDecimal precovenda1;

    @DatabaseField
    public Date atualizacao;

    @DatabaseField
    public BigDecimal desconto;

    public TabelaPrecoItem(){}
    public TabelaPrecoItem(String codigotabelapreco, String codigotabelaprecoitem, BigDecimal precovenda1, Date atualizacao, BigDecimal desconto) {
        this.codigotabelapreco = codigotabelapreco;
        this.codigotabelaprecoitem = codigotabelaprecoitem;
        this.precovenda1 = precovenda1;
        this.atualizacao = atualizacao;
        this.desconto = desconto;
    }
}
