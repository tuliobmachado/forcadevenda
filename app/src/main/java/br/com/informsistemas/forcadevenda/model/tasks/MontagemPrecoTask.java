package br.com.informsistemas.forcadevenda.model.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.math.BigDecimal;

import br.com.informsistemas.forcadevenda.R;
import br.com.informsistemas.forcadevenda.controller.ParceiroActivity;
import br.com.informsistemas.forcadevenda.model.dao.MaterialDAO;
import br.com.informsistemas.forcadevenda.model.dao.MaterialSaldoDAO;
import br.com.informsistemas.forcadevenda.model.helper.CalculoClass;
import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.utils.DialogClass;

public class MontagemPrecoTask extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog dialog;

    public MontagemPrecoTask(Context c) {
        this.context = c;

        dialog = new ProgressDialog(c, R.style.DialogDefault);
        dialog.setCancelable(false);

        Constants.DTO.listMaterialPreco = MaterialDAO.getInstance(context).getListMaterial();
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Corregando lista de materiais");
        dialog.setMax(Constants.DTO.listMaterialPreco.size());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setProgress(0);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        for (int i = 0; i < Constants.DTO.listMaterialPreco.size(); i++) {

            Constants.DTO.listMaterialPreco.get(i).custo = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).custooriginal = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).quantidade = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).baseicms = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).icms = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoricms = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).baseicmssubst = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).icmssubst = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoricmssubst = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).ipi = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoripi = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).margemsubstituicao = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).pautafiscal = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).icmsfecoep = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoricmsfecoep = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).icmsfecoepst = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoricmsfecoepst = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).totalliquido = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).totalliquidooriginal = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).saldomaterial = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoracrescimo = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).percacrescimo = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valordesconto = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).percdesconto = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valoracrescimoant = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).valordescontoant = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).percacrescimoant = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).percdescontoant = new BigDecimal("0");
            Constants.DTO.listMaterialPreco.get(i).precocalculado = new BigDecimal("0");

            CalculoClass calculoClass = new CalculoClass(context, Constants.DTO.listMaterialPreco.get(i));
            calculoClass.setTotal();

            MaterialSaldo materialSaldo = MaterialSaldoDAO.getInstance(context).findByIdAuxiliar("codigomaterial",
                    Constants.DTO.listMaterialPreco.get(i).codigomaterial);

            Constants.DTO.listMaterialPreco.get(i).saldomaterial =
                    materialSaldo.saldo.divide(Constants.DTO.listMaterialPreco.get(i).fator, 4, BigDecimal.ROUND_HALF_EVEN);


            dialog.setProgress(i + 1);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        DialogClass.dialogDismiss(dialog);
        ((ParceiroActivity) context).onExibeMovimentoItem();
    }
}
