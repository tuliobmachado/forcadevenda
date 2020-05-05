package br.com.informsistemas.forcadevenda.model.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.helper.Constants;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;

public class MaterialSaldoDAO extends BaseDAO<MaterialSaldo> {

    private static MaterialSaldoDAO dao;

    public static MaterialSaldoDAO getInstance(Context context){
        if (dao == null){
            dao = new MaterialSaldoDAO(context);
        }

        return dao;
    }

    private MaterialSaldoDAO(Context context){
        super();
        super.ctx = context;
    }

    public List<MaterialSaldo> pesquisaLista(String s){
        List<MaterialSaldo> materialSaldo = new ArrayList<>();

        for (MaterialSaldo m : Constants.DTO.listMaterialSaldo){

            if (m.codigomaterial.toUpperCase().contains(s.toUpperCase()) || m.codigoauxiliar.toUpperCase().contains(s.toUpperCase()) || m.descricao.toUpperCase().contains(s.toUpperCase()) || m.unidade.toUpperCase().contains(s.toUpperCase()) || m.precovenda1.toString().toUpperCase().contains(s.toUpperCase())){
                materialSaldo.add(m);
            }
        }

        return materialSaldo;
    }

}
