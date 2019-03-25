package br.com.informsistemas.forcadevenda.model.callback;

import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.Registro;
import br.com.informsistemas.forcadevenda.model.pojo.RestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistroService {

    @POST(Constants.URL.LOGIN_REGISTRAR)
    Call<RestResponse<Registro>> postRegistro(@Body Registro registro);
}
