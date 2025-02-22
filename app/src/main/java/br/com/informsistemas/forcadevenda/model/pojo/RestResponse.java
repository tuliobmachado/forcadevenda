package br.com.informsistemas.forcadevenda.model.pojo;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class RestResponse<T> implements Serializable {

    @Expose
    public List<T> data;

    @Expose
    public Meta meta;
}
