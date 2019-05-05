package br.com.informsistemas.forcadevenda.model.helper;

import java.util.Date;
import java.util.List;

import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialEstado;
import br.com.informsistemas.forcadevenda.model.pojo.MaterialSaldo;
import br.com.informsistemas.forcadevenda.model.pojo.MetaFuncionario;
import br.com.informsistemas.forcadevenda.model.pojo.Movimento;
import br.com.informsistemas.forcadevenda.model.pojo.FormaPagamento;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoItem;
import br.com.informsistemas.forcadevenda.model.pojo.MovimentoParcela;
import br.com.informsistemas.forcadevenda.model.pojo.Parceiro;
import br.com.informsistemas.forcadevenda.model.pojo.Registro;

public class Constants {

    public static final class URL {

        public static final String BASE = "http://informnfe.com/NFeEmitir/ServicoApp.dll/api/";
//        public static final String BASE = "http://192.168.0.190:8089/api/";
//        public static final String BASE = "http://192.168.0.91:8089/api/";
        public static final String LOGIN_REGISTRAR = "Login/Registrar";
        public static final String DEVICE_CONSULTAR = "Device/Consultar";
        public static final String SINCRONIA_CONSULTAR = "Sincronia/Consultar";
        public static final String SINCRONIA_PARCEIRO = "Sincronia/Parceiro";
        public static final String SINCRONIA_PARCEIROVENCIMENTO = "Sincronia/ParceiroVencimento";
        public static final String SINCRONIA_MATERIAL = "Sincronia/Material";
        public static final String SINCRONIA_MATERIALESTADO = "Sincronia/MaterialEstado";
        public static final String SINCRONIA_MATERIALSALDO = "Sincronia/MaterialSaldo";
        public static final String SINCRONIA_FORMAPAGAMENTO = "Sincronia/FormaPagamento";
        public static final String SINCRONIA_TABELAPRECOITEM = "Sincronia/TabelaPrecoItem";
        public static final String SINCRONIA_CATEGORIA = "Sincronia/Categoria";
        public static final String SINCRONIA_CATEGORIAMATERIAL = "Sincronia/CategoriaMaterial";
        public static final String SINCRONIA_METAFUNCIONARIO = "Sincronia/MetaFuncionario";
        public static final String PEDIDO_GERAR = "Pedido/Gerar";

    }

    public static final class APP {
        public static final String VERSAO = "0.2.3";
    }

    public static final class REQUEST_LOGIN {

        public static final int REGISTRO_PENDENTE = 0;
        public static final int ACESSO_PENDENTE = 1;

    }

    public static class MOVIMENTO {
        public static Movimento movimento = null;
        public static Boolean enviarPedido = false;
        public static float percdescontopadrao = 0;
        public static String codigotabelapreco = "";
        public static String codigoformapagamento = "";
        public static String codigoempresa = "";
        public static String codigofilialcontabil = "";
        public static String codigooperacao = "";
        public static String codigoalmoxarifado = "";
        public static String estadoParceiro = "";
    }

    public static class PEDIDO {
        public static Movimento movimento = null;
        public static List<MovimentoItem> movimentoItems = null;
        public static List<MovimentoParcela> movimentoParcelas = null;
        public static int PEDIDOATUAL = 0;
        public static List<Integer> listPedidos;
    }

    public static class DTO {
        public static Registro registro = null;
        public static List<Atualizacao> listAtualizacaoServidor = null;
        public static List<Parceiro> listPesquisaParceiro = null;
        public static List<Material> listPesquisaMaterial = null;
        public static List<Material> listMaterialPreco = null;
        public static List<FormaPagamento> listPesquisaPagamento = null;
        public static List<Categoria> listPesquisaCategoria = null;
        public static List<MaterialSaldo> listMaterialSaldo = null;
        public static MetaFuncionario metaFuncionario = null;
    }

    public static class SINCRONIA {
        public static boolean CARREGA_TABELAS = true;
        public static int TABELA_ATUAL = 0;
        public static List<String> listTabelaPreco;
        public static List<String> listEstados;
        public static boolean SolicitaMetaFuncionario = false;
        public static Date dataProximaSincronia = null;
    }

    public static class PERMISSION {
        public static int READ_PHONE_STATE;
    }

    public static class PERMISSION_REQUESTCODE {
        public static final int DENY = -99;
        public static final int READ_PHONE_STATE = 0;
    }
}
