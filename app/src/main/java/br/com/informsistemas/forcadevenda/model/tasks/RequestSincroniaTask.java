package br.com.informsistemas.forcadevenda.model.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.fragments.MovimentoFragment;
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
import br.com.informsistemas.forcadevenda.controller.rest.RestManager;
import br.com.informsistemas.forcadevenda.model.callback.SincroniaService;
import br.com.informsistemas.forcadevenda.model.dao.ParceiroDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.helper.Enums;
import br.com.informsistemas.forcadevenda.model.helper.Misc;
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
import br.com.informsistemas.forcadevenda.model.utils.DialogClass;
import retrofit2.Call;
import retrofit2.Response;

public class RequestSincroniaTask <T> extends AsyncTask<String, Void, List<T>> {

    private Fragment fragment;
    private ProgressDialog dialog;
    private String title;
    private final String tabela;
    private Atualizacao atualizacao;
    private String codigoConfiguracao;
    private Date dataSincronia;
    private Class<T> type;
    private Enums.TIPO_SINCRONIA tipoSincronia;
    private Date dataSincronizacao;
    private Boolean erro;

    public RequestSincroniaTask(Fragment f, Class<T> tp, Enums.TIPO_SINCRONIA tipoSincronia, Atualizacao atualizacao, String tabela, String codigoConfiguracao, Date dataSincronia){
        this.fragment = f;
        this.codigoConfiguracao = codigoConfiguracao;
        this.dataSincronia = dataSincronia;
        this.type = tp;
        this.tabela = tabela;
        this.title = tipoSincronia.getString();
        this.tipoSincronia = tipoSincronia;
        this.atualizacao = atualizacao;
        this.erro = false;

        if (tipoSincronia == Enums.TIPO_SINCRONIA.TOTAL){
            this.dataSincronizacao = null;
        }else{
            this.dataSincronizacao = dataSincronia;
        }

        dialog = new ProgressDialog(f.getActivity(), R.style.DialogDefault);
        dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        dialog.setTitle("Solicitando Sincronia "+title);
        dialog.setMessage("Solicitando dados "+tabela+"...");
        dialog.show();
    }

    @Override
    protected List<T> doInBackground(String... strings) {

        switch (type.getSimpleName()){
            case "Parceiro":
                return (List<T>) getParceiro();
            case "ParceiroVencimento":
                return (List<T>) getParceiroVencimento();
            case "Material":
                return (List<T>) getMaterial();
            case "MaterialEstado":
                return (List<T>) getMaterialEstado();
            case "MaterialSaldo":
                return (List<T>) getMaterialSaldo();
            case "FormaPagamento":
                return (List<T>) getFormaPagamento();
            case "TabelaPrecoItem":
                return (List<T>) getTabelaPrecoItem();
            case "Categoria":
                return (List<T>) getCategoria();
            case "CategoriaMaterial":
                return (List<T>) getCategoriaMaterial();
            case "MetaFuncionario":
                return (List<T>) getMetaFuncionario();
        }
        return null;
    }


    @Override
    protected void onPostExecute(final List<T> listDados) {
        if (listDados != null){
            DialogClass.dialogDismiss(dialog);
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MovimentoFragment) fragment).onSetDados(listDados, type, atualizacao,"Carregando tabela de "+tabela, tipoSincronia);
                }
            });
        }else{
            if (erro){
                ((MovimentoFragment) fragment).getSincronia(false);
            }
        }
    }

    private List<Parceiro> getParceiro(){
        RequestSincParceiro requestSincParceiro = new RequestSincParceiro(Constants.DTO.registro.codigofuncionario, codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);
        SincroniaService sincroniaService = new RestManager().getSincroniaService();

        Call<RestResponse<Parceiro>> requestParceiro = sincroniaService.postParceiro(requestSincParceiro);

        try {
            RestResponse<Parceiro> restResponse = null;
            Response<RestResponse<Parceiro>> response = requestParceiro.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<ParceiroVencimento> getParceiroVencimento(){

        RequestSincParceiroVencimento requestSincParceiroVencimento = new RequestSincParceiroVencimento(ParceiroDAO.getInstance(fragment.getActivity()).retornaParceiros(), codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<ParceiroVencimento>> requestParceiroVencimento = sincroniaService.postParceiroVencimento(requestSincParceiroVencimento);

        try {
            RestResponse<ParceiroVencimento> restResponse = null;
            Response<RestResponse<ParceiroVencimento>> response = requestParceiroVencimento.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<Material> getMaterial(){
        RequestSincMaterial requestSincMaterial = new RequestSincMaterial(codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<Material>> requestMaterial = sincroniaService.postMaterial(requestSincMaterial);

        try {
            RestResponse<Material> restResponse = null;
            Response<RestResponse<Material>> response = requestMaterial.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<MaterialEstado> getMaterialEstado(){
        RequestSincMaterialEstado requestSincMaterialEstado = new RequestSincMaterialEstado(getEstados(), codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<MaterialEstado>> requestMaterialEstado = sincroniaService.postMaterialEstado(requestSincMaterialEstado);

        try {
            RestResponse<MaterialEstado> restResponse = null;
            Response<RestResponse<MaterialEstado>> response = requestMaterialEstado.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<MaterialSaldo> getMaterialSaldo(){
        RequestSincMaterialSaldo requestSincMaterialSaldo = new RequestSincMaterialSaldo("", Constants.DTO.registro.codigoalmoxarifado, codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<MaterialSaldo>> requestMaterialSaldo = sincroniaService.postMaterialSaldo(requestSincMaterialSaldo);

        try {
            RestResponse<MaterialSaldo> restResponse = null;
            Response<RestResponse<MaterialSaldo>> response = requestMaterialSaldo.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<FormaPagamento> getFormaPagamento(){
        RequestSincFormaPagamento requestSincFormaPagamento = new RequestSincFormaPagamento(codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<FormaPagamento>> requestPagamento = sincroniaService.postFormaPagamento(requestSincFormaPagamento);

        try {
            RestResponse<FormaPagamento> restResponse = null;
            Response<RestResponse<FormaPagamento>> response = requestPagamento.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<TabelaPrecoItem> getTabelaPrecoItem(){
        RequestSincTabelaPrecoItem requestSincTabelaPrecoItem = new RequestSincTabelaPrecoItem(getCodigoTabelaPreco(), codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<TabelaPrecoItem>> requestTabelaPrecoItem = sincroniaService.postTabelaPrecoItem(requestSincTabelaPrecoItem);

        try {
            RestResponse<TabelaPrecoItem> restResponse = null;
            Response<RestResponse<TabelaPrecoItem>> response = requestTabelaPrecoItem.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<Categoria> getCategoria(){
        RequestSincCategoria requestSincCategoria = new RequestSincCategoria(codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<Categoria>> request = sincroniaService.postCategoria(requestSincCategoria);

        try {
            RestResponse<Categoria> restResponse = null;
            Response<RestResponse<Categoria>> response = request.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }

        return null;
    }

    private List<CategoriaMaterial> getCategoriaMaterial(){
        RequestSincCategoriaMaterial requestSincCategoriaMaterial = new RequestSincCategoriaMaterial(codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<CategoriaMaterial>> requestCategoriaMaterial = sincroniaService.postCategoriaMaterial(requestSincCategoriaMaterial);

        try {
            RestResponse<CategoriaMaterial> restResponse = null;
            Response<RestResponse<CategoriaMaterial>> response = requestCategoriaMaterial.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private List<MetaFuncionario> getMetaFuncionario(){
        RequestSincMetaFuncionario requestSincMetaFuncionario = new RequestSincMetaFuncionario(Misc.getPeriodoMeta(new Date()), Constants.DTO.registro.codigofuncionario,
                codigoConfiguracao, Constants.DTO.registro.cnpj, dataSincronizacao);

        SincroniaService sincroniaService = new RestManager().getSincroniaService();
        Call<RestResponse<MetaFuncionario>> requestMaterialSaldo = sincroniaService.postMetaFuncionario(requestSincMetaFuncionario);

        try {
            RestResponse<MetaFuncionario> restResponse = null;
            Response<RestResponse<MetaFuncionario>> response = requestMaterialSaldo.execute();

            if (response.errorBody() != null){
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            }else{
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")){
                return restResponse.data;
            }else{
                DialogClass.showToastFragment(fragment, restResponse.meta.message);
            }
        }catch (IOException ex){
            DialogClass.showToastFragment(fragment, ex.getMessage());
//            Log.i("Sincronia", ex.getMessage());
            DialogClass.dialogDismiss(dialog);
            erro = true;
        }
        return null;
    }

    private String getEstados(){
        String estado = "";

        if (Constants.SINCRONIA.listEstados.size() == 0){
            ParceiroDAO.getInstance(fragment.getActivity()).getEstados();
        }

        for (int i = 0; i < Constants.SINCRONIA.listEstados.size(); i++) {
            if (estado.equals("")){
                estado = Constants.SINCRONIA.listEstados.get(i);
            }else{
                estado = estado + ";" + Constants.SINCRONIA.listEstados.get(i);
            }
        }

        return estado;
    }

    private String getCodigoTabelaPreco(){
        String codigotabelapreco = "";

        ParceiroDAO.getInstance(fragment.getActivity()).getTabelaPreco();

        for (int i = 0; i < Constants.SINCRONIA.listTabelaPreco.size(); i++) {
            if (codigotabelapreco.equals("")){
                codigotabelapreco = Constants.SINCRONIA.listTabelaPreco.get(i);
            }else{
                codigotabelapreco = codigotabelapreco + ";" + Constants.SINCRONIA.listTabelaPreco.get(i);
            }
        }

        return codigotabelapreco;
    }
}
