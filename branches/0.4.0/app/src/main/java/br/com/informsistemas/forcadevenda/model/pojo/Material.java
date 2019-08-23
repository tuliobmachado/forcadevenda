package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    public float precovenda1;

    @DatabaseField
    public String codigoauxiliar;

    @DatabaseField
    public String tipotributacao;

    @DatabaseField
    public Float percaliquota;

    @DatabaseField
    public String saldo;

    @DatabaseField
    public Date atualizacao;

    @DatabaseField
    public String editacomplemento;

    @DatabaseField
    public Float fator;

    @DatabaseField
    public String codigograde;

    @DatabaseField
    public Float quantmaximavenda;

    @DatabaseField
    public String origemmercadoria;

    @DatabaseField
    public String cst_csosn;

    @DatabaseField
    public Float percreducaobase;

    @DatabaseField
    public float percipi;

    @DatabaseField
    public String codigotabelaprecoitem;

    public float custo;

    public float quantidade;

    public float baseicms;

    public float icms;

    public float valoricms;

    public float baseicmssubst;

    public float icmssubst;

    public float valoricmssubst;

    public float ipi;

    public float valoripi;

    public float margemsubstituicao;

    public float pautafiscal;

    public float icmsfecoep;

    public float valoricmsfecoep;

    public float icmsfecoepst;

    public float valoricmsfecoepst;

    public float totalliquido;

    public float saldomaterial;

    public float valoracrescimo;

    public float percacrescimo;

    public float valordesconto;

    public float percdesconto;

    public Material(){}
    public Material(String codigomaterial, String codigotabelaprecoitem, String descricao, String unidadesaida, float precovenda1,
                    String codigoauxiliar, String tipotributacao, float percaliquota, String saldo,
                    Date atualizacao, String editacomplemento, float fator, String codigograde,
                    float quantmaximavenda, String origemmercadoria, String cst_csosn,
                    float percreducaobase, float percipi) {
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
