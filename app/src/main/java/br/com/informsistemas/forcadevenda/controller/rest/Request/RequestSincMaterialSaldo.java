package br.com.informsistemas.forcadevenda.controller.rest.Request;

import java.util.Date;

import br.com.informsistemas.forcadevenda.model.pojo.DadoSincronia;

public class RequestSincMaterialSaldo extends DadoSincronia {

    public String codigomaterial;
    public String codigoalmoxarifado;

    public RequestSincMaterialSaldo(String codigomaterial, String codigoalmoxarifado, String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        super(codigoconfiguracao, cnpj, dataatualizacao);
        this.codigomaterial = codigomaterial;
        this.codigoalmoxarifado = codigoalmoxarifado;
    }
}
