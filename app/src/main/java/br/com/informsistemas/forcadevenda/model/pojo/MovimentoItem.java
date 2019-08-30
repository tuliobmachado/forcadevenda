package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    public float quantidade;

    @DatabaseField
    @Expose
    public float custo;

    @DatabaseField
    @Expose
    public float totalitem;

    @DatabaseField
    @Expose
    public float icms;

    @DatabaseField
    @Expose
    public float baseicms;

    @DatabaseField
    @Expose
    public float valoricms;

    @DatabaseField
    @Expose
    public float baseicmssubst;

    @DatabaseField
    @Expose
    public float icmssubst;

    @DatabaseField
    @Expose
    public  float valoricmssubst;

    @DatabaseField
    @Expose
    public float ipi;

    @DatabaseField
    @Expose
    public float valoripi;

    @DatabaseField
    @Expose
    public float margemsubstituicao;

    @DatabaseField
    @Expose
    public float pautafiscal;

    @DatabaseField
    @Expose
    public float icmsfecoep;

    @DatabaseField
    @Expose
    public float valoricmsfecoep;

    @DatabaseField
    @Expose
    public float icmsfecoepst;

    @DatabaseField
    @Expose
    public float valoricmsfecoepst;

    @DatabaseField
    @Expose
    public float totalliquido;

    @DatabaseField
    @Expose
    public float valoracrescimoitem;

    @DatabaseField
    @Expose
    public float percacrescimoitem;

    @DatabaseField
    @Expose
    public float valordescontoitem;

    @DatabaseField
    @Expose
    public float percdescontoitem;

    @DatabaseField
    @Expose
    public float custooriginal;

    public MovimentoItem(){}
    public MovimentoItem(Movimento movimento, String codigotabelapreco, String codigomaterial,
                         String unidade, float quantidade, float custo, float totalitem, float icms, float baseicms,
                         float valoricms, float icmssubst, float baseicmssubst, float valoricmssubst, float ipi, float valoripi,
                         float margemsubstituicao, float pautafiscal, float icmsfecoep, float valoricmsfecoep, float icmsfecoepst,
                         float valoricmsfecoepst, float totalliquido, float valoracrescimoitem, float percacrescimoitem,
                         float valordescontoitem, float percdescontoitem, float custooriginal) {
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
