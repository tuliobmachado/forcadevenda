package br.com.informsistemas.forcadevenda.controller.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.com.informsistemas.forcadevenda.model.callback.DeviceService;
import br.com.informsistemas.forcadevenda.model.callback.PedidoService;
import br.com.informsistemas.forcadevenda.model.callback.RegistroService;
import br.com.informsistemas.forcadevenda.model.callback.SincroniaService;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.utils.DateDeserializer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private long timeoutDefault = 120;
    private Boolean excludeExposeDefault = false;
    private RegistroService registroService;
    private DeviceService deviceService;
    private SincroniaService sincroniaService;
    private PedidoService pedidoService;

    public RegistroService getRegistroService(){
        if (registroService == null){
            Retrofit retrofit = getRetrofit(timeoutDefault, excludeExposeDefault);

            registroService = retrofit.create(RegistroService.class);
        }

        return registroService;
    }

    public DeviceService getDeviceService(){
        if (deviceService == null){
            Retrofit retrofit = getRetrofit(timeoutDefault, excludeExposeDefault);

            deviceService = retrofit.create(DeviceService.class);
        }

        return deviceService;
    }

    public SincroniaService getSincroniaService(){
        if (sincroniaService == null){
            Retrofit retrofit = getRetrofit(300, excludeExposeDefault);

            sincroniaService = retrofit.create(SincroniaService.class);
        }

        return sincroniaService;
    }

    public PedidoService getPedidoService(){
        if (pedidoService == null){
            Retrofit retrofit = getRetrofit(500, true);

            pedidoService = retrofit.create(PedidoService.class);
        }

        return pedidoService;
    }

    private OkHttpClient getHttpClient(long timeout){
        return new OkHttpClient().newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
    }

    private Retrofit getRetrofit(long timeout, Boolean excludeExpose){
        Gson gson = null;

        if (excludeExpose){
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(Date.class, new DateDeserializer()).create();
        }else {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(Date.class, new DateDeserializer()).create();
        }

        return new Retrofit.Builder()
                .baseUrl(Constants.URL.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(getHttpClient(timeout))
                .build();
    }

}
