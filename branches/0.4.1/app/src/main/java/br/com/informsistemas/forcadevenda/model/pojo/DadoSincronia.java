package br.com.informsistemas.forcadevenda.model.pojo;

import java.io.Serializable;
import java.util.Date;

public class DadoSincronia implements Serializable {

    public String codigoconfiguracao;
    public String cnpj;
    public Date dataatualizacao;

    public DadoSincronia(String codigoconfiguracao, String cnpj, Date dataatualizacao) {
        this.codigoconfiguracao = codigoconfiguracao;
        this.cnpj = cnpj;
        this.dataatualizacao = dataatualizacao;
    }
}
