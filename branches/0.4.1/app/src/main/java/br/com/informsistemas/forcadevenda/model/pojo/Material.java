package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "cadmaterial")
public class Material implements IEntidade, Cloneable  {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField
    public String codigomaterial;

    @DatabaseField
    public String descricao;

    @DatabaseField
    public String unidadesaida;

    @DatabaseField
    public BigDecimal precovenda1;

    @DatabaseField
    public String codigoauxiliar;

    @DatabaseField
    public String tipotributacao;

    @DatabaseField
    public BigDecimal percaliquota;

    @DatabaseField
    public String saldo;

    @DatabaseField
    public Date atualizacao;

    @DatabaseField
    public String editacomplemento;

    @DatabaseField
    public BigDecimal fator;

    @DatabaseField
    public String codigograde;

    @DatabaseField
    public BigDecimal quantmaximavenda;

    @DatabaseField
    public String origemmercadoria;

    @DatabaseField
    public String cst_csosn;

    @DatabaseField
    public BigDecimal percreducaobase;

    @DatabaseField
    public BigDecimal percipi;

    @DatabaseField
    public String codigotabelaprecoitem;

    public BigDecimal custo;

    public BigDecimal custooriginal;

    public BigDecimal quantidade;

    public BigDecimal baseicms;

    public BigDecimal icms;

    public BigDecimal valoricms;

    public BigDecimal baseicmssubst;

    public BigDecimal icmssubst;

    public BigDecimal valoricmssubst;

    public BigDecimal ipi;

    public BigDecimal valoripi;

    public BigDecimal margemsubstituicao;

    public BigDecimal pautafiscal;

    public BigDecimal icmsfecoep;

    public BigDecimal valoricmsfecoep;

    public BigDecimal icmsfecoepst;

    public BigDecimal valoricmsfecoepst;

    public BigDecimal totalliquido;

    public BigDecimal totalliquidooriginal;

    public BigDecimal saldomaterial;

    public BigDecimal valoracrescimo;

    public BigDecimal percacrescimo;

    public BigDecimal valordesconto;

    public BigDecimal percdesconto;

    public BigDecimal valoracrescimoant;

    public BigDecimal valordescontoant;

    public BigDecimal percacrescimoant;

    public BigDecimal percdescontoant;

    public BigDecimal precocalculado;

    public BigDecimal precovendafinal;

    public Material(){}
    public Material(String codigomaterial, String codigotabelaprecoitem, String descricao, String unidadesaida, BigDecimal precovenda1,
                    String codigoauxiliar, String tipotributacao, BigDecimal percaliquota, String saldo,
                    Date atualizacao, String editacomplemento, BigDecimal fator, String codigograde,
                    BigDecimal quantmaximavenda, String origemmercadoria, String cst_csosn,
                    BigDecimal percreducaobase, BigDecimal percipi) {
        this.codigomaterial = codigomaterial;
        this.codigotabelaprecoitem = codigotabelaprecoitem;
        this.descricao = descricao;
        this.unidadesaida = unidadesaida;
        this.precovenda1 = precovenda1;
        this.codigoauxiliar = codigoauxiliar;
        this.tipotributacao = tipotributacao;
        this.percaliquota = percaliquota;
        this.saldo = saldo;
        this.atualizacao = atualizacao;
        this.editacomplemento = editacomplemento;
        this.fator = fator;
        this.codigograde = codigograde;
        this.quantmaximavenda = quantmaximavenda;
        this.origemmercadoria = origemmercadoria;
        this.cst_csosn = cst_csosn;
        this.percreducaobase = percreducaobase;
        this.percipi = percipi;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
