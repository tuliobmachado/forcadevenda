package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.android.material.internal.Experimental;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "movimento")
public class Movimento implements IEntidade {

    @DatabaseField(generatedId = true)
    @Expose
    public Integer id;

    @DatabaseField
    @Expose
    public String codigoempresa;

    @DatabaseField
    @Expose
    public String codigofilialcontabil;

    @DatabaseField
    @Expose
    public String codigoalmoxarifado;

    @DatabaseField
    @Expose
    public String codigoparceiro;

    @DatabaseField
    @Expose
    public String codigooperacao;

    @DatabaseField
    @Expose
    public String codigotabelapreco;

    @DatabaseField
    @Expose
    public String observacao;

    @DatabaseField
    @Expose
    public BigDecimal totalliquido;

    @DatabaseField
    @Expose
    public String sincronizado;

    @DatabaseField
    @Expose
    public Date data;

    @DatabaseField
    @Expose
    public Date datainicio;

    @DatabaseField
    @Expose
    public Date datafim;

    @DatabaseField
    @Expose
    public Date dataalteracao;

    @DatabaseField
    @Expose
    public double longitude;

    @DatabaseField
    @Expose
    public double latitude;

    @DatabaseField
    @Expose
    public String MD5;

    @DatabaseField
    @Expose
    public String descricaoparceiro;

    @DatabaseField
    @Expose
    public String cpfcgc;

    @DatabaseField
    @Expose
    public double enviolongitude;

    @DatabaseField
    @Expose
    public double enviolatitude;

    @DatabaseField
    @Expose
    public String atualizarlocalizacao;

    public Movimento(){}
    public Movimento(String codigoempresa, String codigofilialcontabil, String codigoalmoxarifado, String codigooperacao,
                     String codigotabelapreco, String codigoparceiro, String observacao, BigDecimal totalliquido, String sincronizado,
                     Date data, Date datainicio, Date datafim, Date dataalteracao, String MD5, String descricaoparceiro, String cpfcgc,
                     double longitude, double latitude, double enviolongitude, double enviolatitude, String atualizarlocalizacao) {
        this.codigoempresa = codigoempresa;
        this.codigofilialcontabil = codigofilialcontabil;
        this.codigoalmoxarifado = codigoalmoxarifado;
        this.codigooperacao = codigooperacao;
        this.codigotabelapreco = codigotabelapreco;
        this.codigoparceiro = codigoparceiro;
        this.observacao = observacao;
        this.totalliquido = totalliquido;
        this.sincronizado = "F";
        this.data = data;
        this.datainicio = datainicio;
        this.datafim = datafim;
        this.dataalteracao = dataalteracao;
        this.longitude = longitude;
        this.latitude = latitude;
        this.MD5 = MD5;
        this.descricaoparceiro = descricaoparceiro;
        this.cpfcgc = cpfcgc;
        this.enviolatitude = enviolatitude;
        this.enviolongitude = enviolongitude;
        this.atualizarlocalizacao = atualizarlocalizacao;
    }
}
