package br.com.informsistemas.forcadevenda.controller.rest.Response;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;

public class ResponsePedido implements Serializable {

    @Expose
    public Integer id;

    @Expose
    public List<MaterialSaldo> materialsaldo;

    @Expose
    public Date dataatualizacao;
}
