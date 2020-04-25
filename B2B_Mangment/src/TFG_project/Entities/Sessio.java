package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.*;

public class Sessio {

    private LocalDate date;
    private String horaInici;
    private String horaFi;
    private HashMap<Integer,TableForSession> listOfTables;

    public Sessio()
    {
        date = LocalDate.now();
        horaInici = "08:00";
        horaFi = "20:00";
        listOfTables = new HashMap<Integer,TableForSession>();
        listOfTables.put(2,new TableForSession(2,0));
        listOfTables.put(3,new TableForSession(3,0));
    }

    public String getHoraInici() {
        return horaInici;
    }

    public void setHoraInici(String hora) {

        this.horaInici = hora;
    }

    public LocalDate getDate (){return date;}

    public void setDate (LocalDate newDate) {
        this.date = newDate;
    }

    public String getHoraFis() {
        return horaFi;
    }

    public void setHoraFi(String horaFi) {

        this.horaFi = horaFi;
    }

    public void setNTables(int nSeats, TableForSession tbs)
    {
        if(listOfTables.containsKey(nSeats))
            listOfTables.replace(nSeats, tbs);
        else
            listOfTables.put(nSeats,tbs);
    }

    public HashMap<Integer,TableForSession> getListOfTables()
    {
        return listOfTables;
    }

    public Set<Integer> getTableValues()
    {
        return listOfTables.keySet();
    }
}

