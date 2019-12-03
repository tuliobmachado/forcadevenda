package br.com.informsistemas.forcadevenda.model.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "parceirolancamento")
public class ParceiroVencimento implements IEntidade {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField
    public String numerolancamento;

    @DatabaseField
    public String codigoparceiro;

    @DatabaseField
    public Date dataemissao;

    @DatabaseField
    public Date datavencimento;

    @DatabaseField
    public float valordocumento;

    @DatabaseField
    public float valoremaberto;

    @DatabaseField
    public String status;

    public ParceiroVencimento(){}
    public ParceiroVencimento(String numerolancamento, String codigoparceiro, Date dataemissao, Date datavencimento, float valordocumento, float valoremaberto, String status) {
        this.numerolancamento = numerolancamento;
        this.codigoparceiro = codigoparceiro;
        this.dataemissao = dataemissao;
        this.datavencimento = datavencimento;
        this.valordocumento = valordocumento;
        this.valoremaberto = valoremaberto;
        this.status = status;
    }
}
