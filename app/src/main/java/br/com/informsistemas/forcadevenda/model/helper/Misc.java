package br.com.informsistemas.forcadevenda.model.helper;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.informsistemas.forcadevenda.model.dao.AtualizacaoDAO;
import br.com.informsistemas.forcadevenda.model.pojo.Atualizacao;
import br.com.informsistemas.forcadevenda.model.pojo.Categoria;
import br.com.informsistemas.forcadevenda.model.pojo.Material;
import br.com.informsistemas.forcadevenda.model.utils.DateDeserializer;

public class Misc {

    public List<Material> cloneMaterialPesquisa(List<Material> original) throws CloneNotSupportedException {
        List<Material> result = new ArrayList<>(original.size());
        for (Material o : original) {
            result.add((Material) o.clone());
        }
        return result;
    }

    public List<Categoria> cloneCategoriaPesquisa(List<Categoria> original) throws CloneNotSupportedException {
        List<Categoria> result = new ArrayList<>(original.size());
        for (Categoria o : original) {
            result.add((Categoria) o.clone());
        }
        return result;
    }

    public static Material cloneMaterial(Material original) throws CloneNotSupportedException {
        Material m = null;
        m = (Material) original.clone();

        return m;
    }

    public static void setTabelasPadrao() {
        Constants.MOVIMENTO.percdescontopadrao = new BigDecimal("0");
        Constants.MOVIMENTO.codigotabelapreco = Constants.DTO.registro.codigotabelapreco;
        Constants.MOVIMENTO.codigoformapagamento = "";
        Constants.MOVIMENTO.codigoalmoxarifado = Constants.DTO.registro.codigoalmoxarifado;
        Constants.MOVIMENTO.codigoempresa = Constants.DTO.registro.codigoempresa;
        Constants.MOVIMENTO.codigofilialcontabil = Constants.DTO.registro.codigofilialcontabil;
        Constants.MOVIMENTO.codigooperacao = Constants.DTO.registro.codigooperacao;
        Constants.MOVIMENTO.estadoParceiro = "";

    }

    public static Locale getLocale(){
        return new Locale("pt", "BR");
    }

    public static float parseStringToFloat(String value){
        if (value.equals("")){
            return 0;
        }

        String replaceable = String.format("[%s.\\s]", NumberFormat.getCurrencyInstance(Misc.getLocale()).getCurrency().getSymbol());
        String cleanString = value.replaceAll(replaceable, "");
        cleanString = cleanString.replaceAll(",", ".");

        return Float.parseFloat(cleanString);
    }

    public static String parseFloatToWatcher(BigDecimal value, int casasDecimais){
        String result;
        float multiplicacao = 0;
        BigDecimal valorBig;
        String casas = "1";
        String reverseValue = new StringBuilder(String.valueOf(value)).reverse().toString();
        int posicao = reverseValue.indexOf(".");

        for (int i = 0; i < casasDecimais - 1; i++) {
            casas = casas + "0";
        }

        multiplicacao = Float.valueOf(casas);

        if (posicao < casasDecimais){
            posicao = casasDecimais--;
            valorBig = value.setScale(posicao, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(multiplicacao));
            result = String.valueOf(valorBig.floatValue());
        }else{
            result = value.toString();
        }

        return result;
    }

    public static <T> String getJsonString(T object, Boolean excludeExpose) {
        Gson gson = null;

        if (excludeExpose) {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(Date.class, new DateDeserializer()).create();
        } else {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").registerTypeAdapter(Date.class, new DateDeserializer()).create();
        }

        return gson.toJson(object);
    }

    public static boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null && conectivtyManager.getActiveNetworkInfo().isAvailable() && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }

        return conectado;
    }

    public static BigDecimal fRound(boolean truncar, BigDecimal value, int casasDecimais) {
        BigDecimal result = value;

        if (truncar) {
            String arg = "" + value;
            int idx = arg.indexOf('.');
            if (idx != -1) {
                if (arg.length() > idx + casasDecimais) {
                    arg = arg.substring(0, idx + casasDecimais + 1);
                    result = new BigDecimal(String.valueOf(Float.parseFloat(arg)));
                }
            }
        } else {
            result = value.setScale(casasDecimais, BigDecimal.ROUND_HALF_EVEN);
        }

        return result;
    }

    public static void requestFocus(FragmentActivity frag, View view){
        if  (view.requestFocus()){
            frag.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static Date GetDateAtual(){
        Date data = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            data = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static Boolean CompareDate(Date dataInicial, Date dataFinal){
        Boolean value = false;
        Date dtInicio, dtFinal;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            dtInicio = simpleDateFormat.parse(simpleDateFormat.format(dataInicial));
            dtFinal = simpleDateFormat.parse(simpleDateFormat.format(dataFinal));

            if (dtInicio.compareTo(dtFinal) > 0){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static Boolean CompareTime(Date dataInicial, Date dataFinal){
        Boolean value = false;
        String dtInicio, dtFinal;
        Integer hrInicio, minInicio, hrFinal, minFinal;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        dtInicio = simpleDateFormat.format(dataInicial);
        dtFinal = simpleDateFormat.format(dataFinal);

        hrInicio = Integer.parseInt(dtInicio.substring(0, 2));
        minInicio = Integer.parseInt(dtInicio.substring(3, 5));

        hrFinal = Integer.parseInt(dtFinal.substring(0, 2));
        minFinal = Integer.parseInt(dtFinal.substring(3, 5));

        int difHoras = hrInicio - hrFinal;
        int difMinutos = minInicio - minFinal;

        while (difMinutos < 0) {
            difMinutos += 60;
            difHoras--;
        }

        while (difHoras < 0) {
            difHoras += 24;
        }

        if ((difHoras >= 1) || (difMinutos > 15)){
            value = true;
        }

        return value;
    }


    public static String gerarMD5(){
        String chave = Constants.DTO.registro.codigofuncionario + formatDate(new Date(), "yyyyMMddHHmmss");
        String value = DigestUtils.md5Hex(chave);

        return value;
    }

    public static String formatDate(Date data, String formate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);

        return simpleDateFormat.format(data);
    }

    public static String getPeriodoMeta(Date data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");

        return simpleDateFormat.format(data);
    }

    public static Date getDataPadrao() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "30-12-1899";

        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getStringToDate(String data, String formate){
        Date dt = null;
        SimpleDateFormat format = new SimpleDateFormat(formate);

        try {
            dt = format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dt;
    }

    public static String formatMoeda(float value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        String valorFormatado = numberFormat.format(value);
        valorFormatado = valorFormatado.replace("R$", "");

        return valorFormatado;
    }

    public static int GetReturnPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    public static void SolicitaPermissao(Activity activity, String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void onRequestLocation(Activity activity){
        //LocationRequest locationRequest = new LocationRequest();
        //locationRequest.setInterval(1000);
        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //Constants.LOCATION.fusedLocationProviderClient.requestLocationUpdates(locationRequest, Constants.LOCATION.locationCallback, Looper.getMainLooper());
        Constants.LOCATION.fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    Constants.LOCATION.LATITUDE = location.getLatitude();
                    Constants.LOCATION.LONGITUDE = location.getLongitude();
                }
            }
        });
    }

    public static Date getMaiorDataAtualizacao(Context context, String nometabela) {
        Atualizacao atualizacao = AtualizacaoDAO.getInstance(context).findByNomeTabela(nometabela);
        Date maiorData = null;

        if ((atualizacao.datasincmarcado.compareTo(atualizacao.datasinctotal) < 0) ||
                (atualizacao.datasincmarcado.compareTo(atualizacao.datasincparcial) < 0)) {
            maiorData = atualizacao.datasincmarcado;
        } else if ((atualizacao.datasincparcial.compareTo(atualizacao.datasinctotal) < 0) ||
                (atualizacao.datasincparcial.compareTo(atualizacao.datasincmarcado) < 0)) {
            maiorData = atualizacao.datasincparcial;
        } else if ((atualizacao.datasinctotal.compareTo(atualizacao.datasincparcial) < 0) ||
                (atualizacao.datasinctotal.compareTo(atualizacao.datasincmarcado) < 0)) {
            maiorData = atualizacao.datasinctotal;
        }

        return maiorData;

    }
}
