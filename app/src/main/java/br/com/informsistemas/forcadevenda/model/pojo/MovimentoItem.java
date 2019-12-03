package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "movimentoitem")
public class MovimentoItem implements IEntidade {

    @DatabaseField(generatedId = true)
    @Expose
    public Integer id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "movimento_id")
    @Expose(serialize = false)
    public Movimento movimento;

    @DatabaseField
    @Expose
    public String codigotabelapreco;

    @DatabaseField
    @Expose
    public String codigomaterial;

    @DatabaseField
    @Expose
    public String unidade;

    @DatabaseField
    @Expose
    public BigDecimal quantidade;

    @DatabaseField
    @Expose
    public BigDecimal custo;

    @DatabaseField
    @Expose
    public BigDecimal totalitem;

    @DatabaseField
    @Expose
    public BigDecimal icms;

    @DatabaseField
    @Expose
    public BigDecimal baseicms;

    @DatabaseField
    @Expose
    public BigDecimal valoricms;

    @DatabaseField
    @Expose
    public BigDecimal baseicmssubst;

    @DatabaseField
    @Expose
    public BigDecimal icmssubst;

    @DatabaseField
    @Expose
    public BigDecimal valoricmssubst;

    @DatabaseField
    @Expose
    public BigDecimal ipi;

    @DatabaseField
    @Expose
    public BigDecimal valoripi;

    @DatabaseField
    @Expose
    public BigDecimal margemsubstituicao;

    @DatabaseField
    @Expose
    public BigDecimal pautafiscal;

    @DatabaseField
    @Expose
    public BigDecimal icmsfecoep;

    @DatabaseField
    @Expose
    public BigDecimal valoricmsfecoep;

    @DatabaseField
    @Expose
    public BigDecimal icmsfecoepst;

    @DatabaseField
    @Expose
    public BigDecimal valoricmsfecoepst;

    @DatabaseField
    @Expose
    public BigDecimal totalliquido;

    @DatabaseField
    @Expose
    public BigDecimal valoracrescimoitem;

    @DatabaseField
    @Expose
    public BigDecimal percacrescimoitem;

    @DatabaseField
    @Expose
    public BigDecimal valordescontoitem;

    @DatabaseField
    @Expose
    public BigDecimal percdescontoitem;

    @DatabaseField
    @Expose
    public BigDecimal custooriginal;

    public MovimentoItem(){}
    public MovimentoItem(Movimento movimento, String codigotabelapreco, String codigomaterial,
                         String unidade, BigDecimal quantidade, BigDecimal custo, BigDecimal totalitem, BigDecimal icms, BigDecimal baseicms,
                         BigDecimal valoricms, BigDecimal icmssubst, BigDecimal baseicmssubst, BigDecimal valoricmssubst, BigDecimal ipi, BigDecimal valoripi,
                         BigDecimal margemsubstituicao, BigDecimal pautafiscal, BigDecimal icmsfecoep, BigDecimal valoricmsfecoep, BigDecimal icmsfecoepst,
                         BigDecimal valoricmsfecoepst, BigDecimal totalliquido, BigDecimal valoracrescimoitem, BigDecimal percacrescimoitem,
                         BigDecimal valordescontoitem, BigDecimal percdescontoitem, BigDecimal custooriginal) {
        this.movimento = movimento;
        this.codigotabelapreco = codigotabelapreco;
        this.codigomaterial = codigomaterial;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.custo = custo;
        this.totalitem = totalitem;
        this.icms = icms;
        this.baseicms = baseicms;
        this.valoricms = valoricms;
        this.icmssubst = icmssubst;
        this.baseicmssubst = baseicmssubst;
        this.valoricmssubst = valoricmssubst;
        this.ipi = ipi;
        this.valoripi = valoripi;
        this.margemsubstituicao = margemsubstituicao;
        this.pautafiscal = pautafiscal;
        this.icmsfecoep = icmsfecoep;
        this.valoricmsfecoep = valoricmsfecoep;
        this.icmsfecoepst = icmsfecoepst;
        this.valoricmsfecoepst = valoricmsfecoepst;
        this.totalliquido = totalliquido;
        this.valoracrescimoitem = valoracrescimoitem;
        this.percacrescimoitem = percacrescimoitem;
        this.valordescontoitem = valordescontoitem;
        this.percdescontoitem = percdescontoitem;
        this.custooriginal = custooriginal;
    }
}
