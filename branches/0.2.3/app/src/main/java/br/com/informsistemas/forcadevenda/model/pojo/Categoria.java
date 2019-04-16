package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "cadcategoria")
public class Categoria implements IEntidade, Cloneable {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField
    public String codigogrupo;

     @DatabaseField
    public String descricao;

    @DatabaseField
    public Date atualizacao;

    public Categoria(){}
    public Categoria(String codigogrupo, String descricao, Date atualizacao) {
        this.codigogrupo = codigogrupo;
        this.descricao = descricao;
        this.atualizacao = atualizacao;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
