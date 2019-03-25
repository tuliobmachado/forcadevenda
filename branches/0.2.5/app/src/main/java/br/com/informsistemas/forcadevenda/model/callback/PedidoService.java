package br.com.informsistemas.forcadevenda.model.callback;

import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestPedido;
import br.com.informsistemas.forcadevenda.controller.rest.Response.ResponsePedido;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.RestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PedidoService {

    @POST(Constants.URL.PEDIDO_GERAR)
    Call<RestResponse<ResponsePedido>> postPedido(@Body RequestPedido requestPedido);
}
