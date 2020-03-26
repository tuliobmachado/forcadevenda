package br.com.informsistemas.forcadevenda.model.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;

import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.fragments.MovimentoFragment;
import br.com.informsistemas.forcadevenda.controller.rest.Request.RequestPedido;
import br.com.informsistemas.forcadevenda.controller.rest.Response.ResponsePedido;
import br.com.informsistemas.forcadevenda.controller.rest.RestManager;
import br.com.informsistemas.forcadevenda.model.callback.PedidoService;
import br.com.informsistemas.forcadevenda.model.dao.AtualizacaoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MaterialSaldoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoDAO;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.RestResponse;
import br.com.informsistemas.forcadevenda.model.utils.DialogClass;
import retrofit2.Call;
import retrofit2.Response;

public class PedidoTask extends AsyncTask<String, Void, List<ResponsePedido>> {

    private Fragment fragment;
    private ProgressDialog dialog;

    public PedidoTask(Fragment f) {
        this.fragment = f;

        dialog = new ProgressDialog(f.getActivity(), R.style.DialogDefault);
        dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Enviando pedido...");
        dialog.show();
    }

    @Override
    protected List<ResponsePedido> doInBackground(String... strings) {
        RequestPedido reqPedido = new RequestPedido(Constants.APP.VERSAO, Constants.DTO.registro.cnpj,
                Constants.DTO.registro.codigoconfiguracao, Constants.DTO.registro.codigousuario, Constants.DTO.registro.codigofuncionario,
                Constants.DTO.registro.codigoalmoxarifado, new Date(), Constants.PEDIDO.movimento, Constants.PEDIDO.movimentoItems,
                Constants.PEDIDO.movimentoParcelas);

        Movimento mov = getMovimentoAtual(reqPedido.movimento.id);
        atualizaStatusMovimento(mov, "P");

        Constants.PEDIDO.movimento = null;
        Constants.PEDIDO.movimentoItems = null;
        Constants.PEDIDO.movimentoParcelas = null;

//        Log.i("JSON", Misc.getJsonString(reqPedido, true));

        PedidoService sincroniaService = new RestManager().getPedidoService();
        Call<RestResponse<ResponsePedido>> requestPedido = sincroniaService.postPedido(reqPedido);

        try {
            RestResponse<ResponsePedido> restResponse = null;
            Response<RestResponse<ResponsePedido>> response = requestPedido.execute();

            if (response.errorBody() != null) {
                restResponse = new Gson().fromJson(response.errorBody().charStream(), RestResponse.class);
            } else {
                String responseString = response.body().toString();
                restResponse = response.body();
            }

            if (restResponse.meta.status.equals("OK")) {
                return restResponse.data;
            } else {
                showToast(restResponse.meta.message);
            }
        } catch (IOException ex) {
            showToast(ex.getMessage());
            Constants.PEDIDO.PEDIDOATUAL = 0;
            Constants.PEDIDO.listPedidos = null;
            Constants.PEDIDO.listPedidos = new ArrayList<>();
            Constants.MOVIMENTO.enviarPedido = false;
            DialogClass.dialogDismiss(dialog);
        }

        return null;
    }

    @Override
    protected void onPostExecute(final List<ResponsePedido> requestPedido) {
        if (requestPedido != null) {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Movimento mov = getMovimentoAtual(requestPedido.get(0).id);
                        String status = "";

                        if (requestPedido.get(0).status.equals("G")) {
                            status = "T";
                        } else {
                            status = "P";
                        }

                        atualizaStatusMovimento(mov, status);

                        if (requestPedido.get(0).materialsaldo != null) {
                            atualizaSaldo(requestPedido.get(0).materialsaldo);
                        }

                        DialogClass.dialogDismiss(dialog);

                        if (Constants.PEDIDO.PEDIDOATUAL > Constants.PEDIDO.listPedidos.size()) {
                            ((MovimentoFragment) fragment).getSincronia(false);
                            ((MovimentoFragment) fragment).atualizaLista();
                        } else {
                            ((MovimentoFragment) fragment).verificaPedido(Constants.PEDIDO.PEDIDOATUAL);
                        }
                    }catch (Exception ex) {
                        showToast(ex.getMessage());
                        DialogClass.dialogDismiss(dialog);
                    }
                }
            });
        } else {
            DialogClass.dialogDismiss(dialog);
            ((MovimentoFragment) fragment).atualizaLista();
        }
    }

    private void showToast(final String msg) {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(fragment.getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizaSaldo(List<MaterialSaldo> materialSaldo){

        for (int i = 0; i < materialSaldo.size(); i++) {
            MaterialSaldo m = MaterialSaldoDAO.getInstance(fragment.getActivity()).findByIdAuxiliar("codigomaterial", materialSaldo.get(i).codigomaterial);

            if (m != null){
                m.saldo = materialSaldo.get(i).saldo;

                MaterialSaldoDAO.getInstance(fragment.getActivity()).createOrUpdate(m);
            }
        }

    }

    private void setDataParcialAtualizacao(Date data){
        Atualizacao atualizacao = AtualizacaoDAO.getInstance(fragment.getActivity()).findByNomeTabela("MATERIALSALDO");
        atualizacao.datasincparcial = data;

        AtualizacaoDAO.getInstance(fragment.getActivity()).createOrUpdate(atualizacao);
    }

    private Movimento getMovimentoAtual(Integer id){
        return MovimentoDAO.getInstance(fragment.getActivity()).findById(id);
    }

    private  void atualizaStatusMovimento(Movimento mov, String status){
        mov.sincronizado = status;
        MovimentoDAO.getInstance(fragment.getActivity()).createOrUpdate(mov);
    }
}
