package br.com.informsistemas.forcadevenda.controller.rest.Request;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.pojo.DadoSincronia;

public class RequestSincCategoriaMaterial extends DadoSincronia {

    public RequestSincCategoriaMaterial(String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        super(codigoconfiguracao, cnpj, dataatualizacao);
    }
}
