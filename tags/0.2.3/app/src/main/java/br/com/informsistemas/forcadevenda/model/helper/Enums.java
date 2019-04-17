package br.com.informsistemas.forcadevenda.model.helper;

public class Enums {

    public enum TIPO_SINCRONIA{
        NENHUMA(""), PARCIAL("Parcial"), MARCADOS("Marcados"), TOTAL("Total");

        private final String value;

        TIPO_SINCRONIA(String s) {
            value = s;
        }

        public String getString(){
            return value;
        }
    }
}
