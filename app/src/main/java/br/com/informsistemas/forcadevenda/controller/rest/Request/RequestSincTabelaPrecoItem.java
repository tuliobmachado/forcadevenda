package br.com.informsistemas.forcadevenda.controller.rest.Request;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.pojo.DadoSincronia;

public class RequestSincTabelaPrecoItem extends DadoSincronia {

    public String codigotabelapreco;

    public RequestSincTabelaPrecoItem(String codigotabelapreco, String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        super(codigoconfiguracao, cnpj, dataatualizacao);
        this.codigotabelapreco = codigotabelapreco;
    }
}
