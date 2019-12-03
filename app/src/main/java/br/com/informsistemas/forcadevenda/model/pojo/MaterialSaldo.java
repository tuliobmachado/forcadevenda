package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "materialsaldo")
public class MaterialSaldo implements IEntidade {

    @DatabaseField(generatedId = true)
    @Expose
    public Integer id;

    @DatabaseField
    @Expose
    public String codigomaterial;

    @DatabaseField
    @Expose
    public String descricao;

    @DatabaseField
    @Expose
    public String unidade;

    @DatabaseField
    @Expose
    public BigDecimal saldo;

    @DatabaseField
    @Expose
    public BigDecimal precovenda1;

    public MaterialSaldo(){}
    public MaterialSaldo(String codigomaterial, String descricao, String unidade, BigDecimal saldo, BigDecimal precovenda1) {
        this.codigomaterial = codigomaterial;
        this.descricao = descricao;
        this.unidade = unidade;
        this.saldo = saldo;
        this.precovenda1 = precovenda1;
    }
}
