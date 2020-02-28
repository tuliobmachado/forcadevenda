package br.com.informsistemas.forcadevenda.model.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import br.com.informsistemas.forcadevenda.model.utils.IEntidade;

@DatabaseTable(tableName = "parametro")
public class Parametro implements IEntidade {

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = "registro_id")
    public Registro registro;

    @DatabaseField
    public Boolean bloquearvendas;

    @DatabaseField
    public String horainiciosemana;

    @DatabaseField
    public String horafimsemana;

    @DatabaseField
    public String horainiciosabado;

    @DatabaseField
    public String horafimsabado;

    @DatabaseField
    public String horainiciodomingo;

    @DatabaseField
    public String horafimdomingo;

    public Parametro(){}
    public Parametro(Registro registro, Boolean bloquearvendas, String horainiciosemana, String horafimsemana,
                     String horainiciosabado, String horafimsabado, String horainiciodomingo, String horafimdomingo){
        this.registro = registro;
        this.bloquearvendas = bloquearvendas;
        this.horainiciosemana = horainiciosemana;
        this.horafimsemana = horafimsemana;
        this.horainiciosabado = horainiciosabado;
        this.horafimsabado = horafimsabado;
        this.horainiciodomingo = horainiciodomingo;
        this.horafimdomingo = horafimdomingo;
    }
}
