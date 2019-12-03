package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "materialestado")
public class MaterialEstado implements IEntidade {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField
    public String codigomaterial;

    @DatabaseField
    public String estado;

    @DatabaseField
    public Date atualizacao;

    @DatabaseField
    public BigDecimal mva;

    @DatabaseField
    public BigDecimal icms_interestadual;

    @DatabaseField
    public BigDecimal icms_interno;

    @DatabaseField
    public BigDecimal fecoep;

    @DatabaseField
    public BigDecimal pautafiscal;

    public MaterialEstado(){}
    public MaterialEstado(String codigomaterial, String estado, Date atualizacao, BigDecimal mva, BigDecimal icms_interestadual,
                          BigDecimal icms_interno, BigDecimal fecoep, BigDecimal pautafiscal) {
        this.codigomaterial = codigomaterial;
        this.estado = estado;
        this.atualizacao = atualizacao;
        this.mva = mva;
        this.icms_interestadual = icms_interestadual;
        this.icms_interno = icms_interno;
        this.fecoep = fecoep;
        this.pautafiscal = pautafiscal;
    }
}
