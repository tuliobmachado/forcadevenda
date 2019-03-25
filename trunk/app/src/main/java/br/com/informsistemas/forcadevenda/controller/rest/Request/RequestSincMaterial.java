package br.com.informsistemas.forcadevenda.controller.rest.Request;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.pojo.DadoSincronia;

public class RequestSincMaterial extends DadoSincronia {

    public RequestSincMaterial(String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        super(codigoconfiguracao, cnpj, dataatualizacao);
    }
}
