package br.com.informsistemas.forcadevenda.controller.rest.Request;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoParcela;

public class RequestPedido implements Serializable {

    @Expose
    public String versao;

    @Expose
    public String cnpj;

    @Expose
    public String codigoconfiguracao;

    @Expose
    public String codigousuario;

    @Expose
    public String codigofuncionario;

    @Expose
    public String codigoalmoxarifado;

    @Expose
    public Date materialatualizacao;

    @Expose
    public Movimento movimento;

    @Expose
    public List<MovimentoItem> movimentoitem;

    @Expose
    public List<MovimentoParcela> movimentoparcela;

    public RequestPedido(String versao, String cnpj, String codigoconfiguracao, String codigousuario, String codigofuncionario, String codigoalmoxarifado, Date materialatualizacao, Movimento movimento, List<MovimentoItem> movimentoitem, List<MovimentoParcela> movimentoparcela) {
        this.versao = versao;
        this.cnpj = cnpj;
        this.codigousuario = codigousuario;
        this.codigoconfiguracao = codigoconfiguracao;
        this.codigofuncionario = codigofuncionario;
        this.codigoalmoxarifado = codigoalmoxarifado;
        this.materialatualizacao = materialatualizacao;
        this.movimento = movimento;
        this.movimentoitem = movimentoitem;
        this.movimentoparcela = movimentoparcela;
    }
}
