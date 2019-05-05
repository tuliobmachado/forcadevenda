package br.com.informsistemas.forcadevenda.model.callback;

import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincAtualizacao;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincCategoria;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincCategoriaMaterial;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincFormaPagamento;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincMaterial;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincMaterialEstado;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincMaterialSaldo;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincMetaFuncionario;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincParceiro;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincParceiroVencimento;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestSincTabelaPrecoItem;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;
import br.com.informsistemas.forcadevenda.model.pojo.CategoriaMaterial;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.pojo.MetaFuncionario;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.ParceiroVencimento;
import br.com.informsistemas.forcadevenda.model.pojo.RestResponse;
import br.com.informsistemas.forcadevenda.model.pojo.TabelaPrecoItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SincroniaService {

    @POST(Constants.URL.SINCRONIA_CONSULTAR)
    Call<RestResponse<Atualizacao>> postAtualizacao(@Body RequestSincAtualizacao requestSincAtualizacao);

    @POST(Constants.URL.SINCRONIA_PARCEIRO)
    Call<RestResponse<Parceiro>> postParceiro(@Body RequestSincParceiro requestSincParceiro);

    @POST(Constants.URL.SINCRONIA_PARCEIROVENCIMENTO)
    Call<RestResponse<ParceiroVencimento>> postParceiroVencimento(@Body RequestSincParceiroVencimento requestSincParceiroVencimento);

    @POST(Constants.URL.SINCRONIA_FORMAPAGAMENTO)
    Call<RestResponse<FormaPagamento>> postFormaPagamento(@Body RequestSincFormaPagamento requestSincFormaPagamento);

    @POST(Constants.URL.SINCRONIA_MATERIAL)
    Call<RestResponse<Material>> postMaterial(@Body RequestSincMaterial requestSincMaterial);

    @POST(Constants.URL.SINCRONIA_MATERIALESTADO)
    Call<RestResponse<MaterialEstado>> postMaterialEstado(@Body RequestSincMaterialEstado requestSincMaterialEstado);

    @POST(Constants.URL.SINCRONIA_MATERIALSALDO)
    Call<RestResponse<MaterialSaldo>> postMaterialSaldo(@Body RequestSincMaterialSaldo requestSincMaterialSaldo);

    @POST(Constants.URL.SINCRONIA_TABELAPRECOITEM)
    Call<RestResponse<TabelaPrecoItem>> postTabelaPrecoItem(@Body RequestSincTabelaPrecoItem requestSincTabelaPrecoItem);

    @POST(Constants.URL.SINCRONIA_CATEGORIA)
    Call<RestResponse<Categoria>> postCategoria(@Body RequestSincCategoria requestSincCategoria);

    @POST(Constants.URL.SINCRONIA_CATEGORIAMATERIAL)
    Call<RestResponse<CategoriaMaterial>> postCategoriaMaterial(@Body RequestSincCategoriaMaterial requestSincCategoriaMaterial);

    @POST(Constants.URL.SINCRONIA_METAFUNCIONARIO)
    Call<RestResponse<MetaFuncionario>> postMetaFuncionario(@Body RequestSincMetaFuncionario requestSincMetaFuncionario);
}
