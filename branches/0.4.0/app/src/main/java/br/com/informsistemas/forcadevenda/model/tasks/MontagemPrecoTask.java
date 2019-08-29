package br.com.informsistemas.forcadevenda.model.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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

            CalculoClass calculoClass = new CalculoClass(context, Constants.DTO.listMaterialPreco.get(i));
            calculoClass.setTotal();

            MaterialSaldo materialSaldo = MaterialSaldoDAO.getInstance(context).findByIdAuxiliar("codigomaterial",
                    Constants.DTO.listMaterialPreco.get(i).codigomaterial);

            Constants.DTO.listMaterialPreco.get(i).saldomaterial =
                    (materialSaldo.saldo / Constants.DTO.listMaterialPreco.get(i).fator);


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
