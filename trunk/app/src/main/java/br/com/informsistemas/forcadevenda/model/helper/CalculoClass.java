package br.com.informsistemas.forcadevenda.model.helper;

import android.content.Context;

import br.com.informsistemas.forcadevenda.model.dao.MaterialEstadoDAO;
import br.com.informsistemas.forcadevenda.model.dao.TabelaPrecoItemDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.TabelaPrecoItem;

public class CalculoClass {

    public static void getTributos(Context context, Material material){
        MaterialEstado materialEstado = MaterialEstadoDAO.getInstance(context).getTributacoes(Constants.MOVIMENTO.estadoParceiro, material.codigomaterial);

        material.margemsubstituicao = materialEstado.mva;
        material.pautafiscal = materialEstado.pautafiscal;
        getIPI(material);
        getICMS(material, materialEstado);
        getFecoep(material, materialEstado);
        material.totalliquido = getTotalLiquido(material);
    }

    public static void getIPI(Material material){
        material.ipi = material.percipi;

        if (material.ipi > 0) {
            material.valoripi = Misc.fRound(true,material.precovenda1 * (material.percipi / 100), 2);
        }

        if (material.valoripi < 0){
            material.valoripi = 0;
        }
    }

    private static void getFecoep(Material material, MaterialEstado materialEstado){
        material.icmsfecoep = materialEstado.fecoep;
        material.icmsfecoepst = materialEstado.fecoep;

        if (material.icmsfecoep > 0) {
            if (material.cst_csosn.equals("00") || material.cst_csosn.equals("10") ||
                    material.cst_csosn.equals("20") || material.cst_csosn.equals("51") ||
                    material.cst_csosn.equals("70") || material.cst_csosn.equals("90")){
                material.valoricmsfecoep = Misc.fRound(false, (material.baseicms * ( material.icmsfecoep / 100 )), 2);
            }else{
                material.valoricmsfecoep = 0;
            }
        }

        if (material.icmsfecoepst > 0) {
            if (material.cst_csosn.equals("10") || material.cst_csosn.equals("30") ||
                    material.cst_csosn.equals("70") || material.cst_csosn.equals("90") ||
                    material.cst_csosn.equals("201") || material.cst_csosn.equals("202") ||
                    material.cst_csosn.equals("203") || material.cst_csosn.equals("900"))    {
                material.valoricmsfecoepst = Misc.fRound(false, (material.baseicmssubst * ( material.icmsfecoepst / 100 ) - material.valoricmsfecoep), 2);
            }else{
                material.valoricmsfecoepst = 0;
            }
        }
    }

    private static void getICMS(Material material, MaterialEstado materialEstado){
        if (Constants.MOVIMENTO.estadoParceiro.equals(Constants.DTO.registro.estado)) {
            material.icms = materialEstado.icms_interno;
            material.icmssubst = materialEstado.icms_interno;
        } else {
            material.icms = materialEstado.icms_interestadual;
            material.icmssubst = materialEstado.icms_interno;
        }

        if (material.icms > 0) {
            material.valoricms = Misc.fRound(true,material.precovenda1 * (material.icms / 100), 2);
        }

        if (material.valoricms < 0){
            material.valoricms = 0;
        }

        material.baseicms = (material.precovenda1);

        if ((Constants.DTO.registro.utilizapauta) && (material.pautafiscal > 0)) {

            if (material.quantidade == 0){
                material.baseicmssubst = material.pautafiscal;
            }else {
                material.baseicmssubst = (material.pautafiscal * material.quantidade);
            }

            if (Constants.DTO.registro.utilizafatorpauta){
                material.baseicmssubst = material.baseicmssubst * material.fator;
            }

            material.margemsubstituicao = 0;
        }else {
            material.baseicmssubst = (material.baseicms + material.valoripi);
            material.baseicmssubst = material.baseicmssubst + (material.baseicmssubst * material.margemsubstituicao / 100);

            material.pautafiscal = 0;
        }

        material.valoricmssubst = Misc.fRound(false,(material.baseicmssubst * material.icmssubst), 2) / 100;
        material.valoricmssubst = (material.valoricmssubst - material.valoricms);

        if (material.valoricmssubst < 0){
            material.valoricmssubst = 0;
        }

        material.baseicmssubst = Misc.fRound(false, material.baseicmssubst, 2);
        material.valoricmssubst = Misc.fRound(false, material.valoricmssubst, 2);
    }

    private static float getTotalLiquido(Material m){
        m.totalliquido = 0;
        return m.precovenda1 + m.valoricmsfecoepst + m.valoricmssubst + m.valoripi;
    }

    public static float getPrecoVenda(Context context, Material material){
        TabelaPrecoItem tabelaPrecoItem = null;
        float precovenda1 = 0;

        if (Constants.MOVIMENTO.codigotabelapreco != Constants.DTO.registro.codigotabelapreco){
            tabelaPrecoItem = TabelaPrecoItemDAO.getInstance(context).getTabelaPrecoItem(Constants.MOVIMENTO.codigotabelapreco, material.codigotabelaprecoitem);

            if (tabelaPrecoItem != null){
                precovenda1 = tabelaPrecoItem.precovenda1;
            }else{
                precovenda1 = material.precovenda1;
            }
        }else{
            precovenda1 = material.precovenda1;
        }

        material.custo = precovenda1;

        return getDesconto(precovenda1, tabelaPrecoItem);
    }

    public static float getDesconto(float precovenda1, TabelaPrecoItem tbItem) {
        float value = 0;
        float percdesconto = 0;
        float valorDesconto = 0;

        if (tbItem != null) {
            if (tbItem.desconto > 0) {
                percdesconto = tbItem.desconto;
            }
        }

        if (Constants.MOVIMENTO.percdescontopadrao > 0) {
            percdesconto = Constants.MOVIMENTO.percdescontopadrao;
        }

        valorDesconto = (precovenda1 * (percdesconto / 100));
        value = precovenda1 - valorDesconto;

        return value;
    }
}
