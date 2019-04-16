package br.com.informsistemas.forcadevenda.controller.rest.Request;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.pojo.DadoSincronia;

public class RequestSincMetaFuncionario extends DadoSincronia {

    public String codigometa;
    public String codigofuncionario;

    public RequestSincMetaFuncionario(String codigometa, String codigofuncionario, String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        super(codigoconfiguracao, cnpj, dataatualizacao);
        this.codigometa = codigometa;
        this.codigofuncionario = codigofuncionario;
    }
}
