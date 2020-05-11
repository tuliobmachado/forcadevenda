package br.com.informsistemas.forcadevenda.model.helper;

import android.content.Context;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.dao.MaterialEstadoDAO;
import br.com.informsistemas.forcadevenda.model.dao.MovimentoDAO;
import br.com.informsistemas.forcadevenda.model.dao.TabelaPrecoItemDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.TabelaPrecoItem;

public class CalculoClass {

    private Context context;
    private Material material;

    public CalculoClass(Context context, Material m) {
        this.context = context;
        this.material = m;
    }

    public void setTributos() {
        CalculaTributos();
    }

    public void setTotal() {
        CalculaPrecoVenda();
        CalculaTributos();
    }

    public BigDecimal getPrecoFinal(BigDecimal precoFinal, BigDecimal quantidade){
        Material material_final = null;
        Material material_diferenca = null;
        BigDecimal custo_final;
        BigDecimal valor_final;
        BigDecimal valor_base;

        try {
            material_final = Misc.cloneMaterial(material);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        material_final.custo = precoFinal;
        material_final.precocalculado = material_final.custo.multiply(quantidade);

        if (material_final.custo.floatValue() == 0){
            material_final.custo = material_final.custooriginal;
        }

        material_final.precovendafinal = material_final.custo;

        material_final.percdesconto = new BigDecimal("0");
        material_final.percacrescimo = new BigDecimal("0");
        material_final.valordesconto = new BigDecimal("0");
        material_final.valoracrescimo = new BigDecimal("0");

        CalculoClass calculoClassFinal = new CalculoClass(this.context, material_final);
        calculoClassFinal.setTributos();

        valor_base = material_final.custo.multiply(quantidade).divide(material_final.totalliquido, 8, BigDecimal.ROUND_HALF_EVEN);
        valor_base = Misc.fRound(true, valor_base, 6);
        valor_final = valor_base.multiply(material_final.custo);
        custo_final = Misc.fRound (false, valor_final, 4) ;

//        try {
//            material_diferenca = Misc.cloneMaterial(material_final);
//            material_diferenca.custo = custo_final;
//            material_diferenca.precocalculado = material_final.custo.multiply(quantidade);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//
//        CalculoClass calculoClass = new CalculoClass(context, material_diferenca);
//        calculoClass.setTributos();
//
//        while ((material_diferenca.totalliquido.floatValue() != material_final.totalliquido.floatValue())) {
//
//            material_diferenca.custo = custo_final;
//            material_diferenca.precocalculado = material_final.custo.multiply(quantidade);
//            calculoClass.setTributos();
//
//            if (material_diferenca.totalliquido.floatValue() == material_final.custo.floatValue()){
//                break;
//            }
//
//            if (material_final.custo.floatValue() < material_diferenca.totalliquido.floatValue()){
//                custo_final = custo_final.subtract(new BigDecimal("0.0001"));
//            }else{
//                custo_final = custo_final.add(new BigDecimal("0.0001"));
//            }
//
//            custo_final = Misc.fRound(false, custo_final, 4);
//        }

        return custo_final;
    }

    public BigDecimal getTotalLiquido(){
        return material.totalliquido;
    }

    private void CalculaTributos() {
        MaterialEstado materialEstado = null;
        materialEstado = MaterialEstadoDAO.getInstance(context).getTributacoes(Constants.MOVIMENTO.estadoParceiro, material.codigomaterial);

        if (materialEstado == null){
            materialEstado = new MaterialEstado(material.codigomaterial, Constants.MOVIMENTO.estadoParceiro, new Date(), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"));
            materialEstado.mva = new BigDecimal("0");
            materialEstado.pautafiscal = new BigDecimal("0");
        }

        material.margemsubstituicao = materialEstado.mva;
        material.pautafiscal = materialEstado.pautafiscal;
        CalculaIPI();
        CalculaICMS(materialEstado);
        CalculaFecoep(materialEstado);
        CalculaTotalLiquido();
    }

    private void CalculaIPI() {
        material.ipi = material.percipi;

        if (material.ipi.floatValue() > 0) {
            material.valoripi = material.precocalculado.multiply(material.percipi.divide(new BigDecimal("100"), 8 , BigDecimal.ROUND_HALF_EVEN));
            material.valoripi = Misc.fRound(true, material.valoripi, 2);
        }

        if (material.valoripi.floatValue() < 0) {
            material.valoripi = new BigDecimal("0");
        }
    }

    private void CalculaFecoep(MaterialEstado materialEstado){
        material.icmsfecoep = materialEstado.fecoep;
        material.icmsfecoepst = materialEstado.fecoep;

        if (material.icmsfecoep.floatValue() > 0) {
            if (material.cst_csosn.equals("00") || material.cst_csosn.equals("10") ||
                    material.cst_csosn.equals("20") || material.cst_csosn.equals("51") ||
                    material.cst_csosn.equals("70") || material.cst_csosn.equals("90")){
                material.valoricmsfecoep = material.baseicms.multiply(material.icmsfecoep.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN));
                material.valoricmsfecoep = Misc.fRound(false, material.valoricmsfecoep, 2);
            }else{
                material.valoricmsfecoep = new BigDecimal("0");
            }
        }

        if (material.icmsfecoepst.floatValue() > 0) {
            if (material.cst_csosn.equals("10") || material.cst_csosn.equals("30") ||
                    material.cst_csosn.equals("70") || material.cst_csosn.equals("90") ||
                    material.cst_csosn.equals("201") || material.cst_csosn.equals("202") ||
                    material.cst_csosn.equals("203") || material.cst_csosn.equals("900"))    {
                material.valoricmsfecoepst = material.baseicmssubst.multiply(material.icmsfecoepst.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN)).subtract(material.valoricmsfecoep);
                material.valoricmsfecoepst = Misc.fRound(false, material.valoricmsfecoepst, 2);
            }else{
                material.valoricmsfecoepst = new BigDecimal("0");
            }
        }
    }

    private void CalculaICMS(MaterialEstado materialEstado){
        if (Constants.MOVIMENTO.estadoParceiro.equals(Constants.DTO.registro.estado)) {
            material.icms = materialEstado.icms_interno;
            material.icmssubst = materialEstado.icms_interno;
        } else {
            material.icms = materialEstado.icms_interestadual;
            material.icmssubst = materialEstado.icms_interno;
        }

        if (material.icms.floatValue() > 0) {
            material.valoricms = material.precocalculado.multiply(material.icms.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN));
            material.valoricms = Misc.fRound(false, material.valoricms, 2);
        }

        if (material.valoricms.floatValue() < 0){
            material.valoricms = new BigDecimal("0");
        }

        material.baseicms = (material.precocalculado);

        if ((Constants.DTO.registro.utilizapauta) && (material.pautafiscal.floatValue() > 0)) {

            if (material.quantidade.floatValue() == 0){
                material.baseicmssubst = material.pautafiscal;
            }else {
                material.baseicmssubst = material.pautafiscal.multiply(material.quantidade);
            }

            if (Constants.DTO.registro.utilizafatorpauta){
                material.baseicmssubst = material.baseicmssubst.multiply(material.fator);
            }

            material.margemsubstituicao = new BigDecimal("0");
        }else {
            material.baseicmssubst = material.baseicms.add(material.valoripi);
            material.baseicmssubst = material.baseicmssubst.add(material.baseicmssubst.multiply(material.margemsubstituicao.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN)));

            material.pautafiscal = new BigDecimal("0");

            if (material.cst_csosn.equals("60") || material.cst_csosn.equals("00")){
                material.baseicmssubst = new BigDecimal("0");
            }
        }

        material.valoricmssubst = material.baseicmssubst.multiply(material.icmssubst);
        material.valoricmssubst = Misc.fRound(false, material.valoricmssubst, 2);
        material.valoricmssubst = material.valoricmssubst.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN);
        material.valoricmssubst = material.valoricmssubst.subtract(material.valoricms);

        if (material.valoricmssubst.floatValue() < 0){
            material.valoricmssubst = new BigDecimal("0");
        }

        material.baseicmssubst = Misc.fRound(false, material.baseicmssubst, 2);
        material.valoricmssubst = Misc.fRound(false, material.valoricmssubst, 2);
    }

    private void CalculaTotalLiquido(){
        material.totalliquido = material.precocalculado.add(material.valoricmsfecoepst).add(material.valoricmssubst).add(material.valoripi);

        material.totalliquido = Misc.fRound(false, material.totalliquido, 2);

        if (material.totalliquidooriginal.floatValue() == 0){
            material.totalliquidooriginal = material.totalliquido;
        }
    }

    private void CalculaPrecoVenda() {
        TabelaPrecoItem tabelaPrecoItem = null;
        BigDecimal precovenda1;

        if (!Constants.MOVIMENTO.codigotabelapreco.equals(Constants.DTO.registro.codigotabelapreco)) {
            tabelaPrecoItem = TabelaPrecoItemDAO.getInstance(context).getTabelaPrecoItem(Constants.MOVIMENTO.codigotabelapreco, material.codigotabelaprecoitem);

            if (tabelaPrecoItem != null) {
                precovenda1 = tabelaPrecoItem.precovenda1;
            } else {
                precovenda1 = material.precovenda1;
            }
        } else {
            precovenda1 = material.precovenda1;
        }

        if (material.custo.floatValue() == material.custooriginal.floatValue()) {
            material.custo = precovenda1;
        }

        if (material.custooriginal.floatValue() == 0) {
            material.custooriginal = material.custo;
        }

        material.precocalculado = material.custo.add(material.valoracrescimo).subtract(material.valordesconto);

        CalculaDesconto(tabelaPrecoItem);
    }

    private void CalculaDesconto(TabelaPrecoItem tbItem) {
        BigDecimal value;
        BigDecimal percdesconto = new BigDecimal("0");
        BigDecimal valorDesconto;

        if (tbItem != null) {
            if (tbItem.desconto.floatValue() > 0) {
                percdesconto = tbItem.desconto;
            }
        }

        if (Constants.MOVIMENTO.percdescontopadrao.floatValue() > 0) {
            percdesconto = Constants.MOVIMENTO.percdescontopadrao;
        }

        valorDesconto = material.precocalculado.multiply(percdesconto.divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_EVEN));
        value = material.precocalculado.subtract(valorDesconto);

        material.precocalculado = value;
    }

    public void recalcularMovimento(Movimento mov, List<MovimentoItem> listMovItem) {
        BigDecimal total_fecoepst = new BigDecimal("0");
        BigDecimal total_ipi = new BigDecimal("0");
        BigDecimal total_icmssubst = new BigDecimal("0");
        BigDecimal total_material = new BigDecimal("0");
        BigDecimal total_acrescimo = new BigDecimal("0");
        BigDecimal total_desconto = new BigDecimal("0");

        for (int i = 0; i < listMovItem.size(); i++) {
            total_fecoepst = total_fecoepst.add(listMovItem.get(i).valoricmsfecoepst);
            total_ipi = total_ipi.add(listMovItem.get(i).valoripi);
            total_icmssubst = total_icmssubst.add(listMovItem.get(i).valoricmssubst);
            total_acrescimo = total_acrescimo.add(listMovItem.get(i).valoracrescimoitem);
            total_desconto = total_desconto.add(listMovItem.get(i).valordescontoitem);

            total_material = total_material.add(listMovItem.get(i).custo.multiply(listMovItem.get(i).quantidade));
        }

        mov.totalliquido = total_material.add(total_fecoepst).add(total_ipi).add(total_icmssubst).add(total_acrescimo).subtract(total_desconto);
        MovimentoDAO.getInstance(context).createOrUpdate(mov);
    }
}
