package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Sessio {

    private String horaInici;
    private String horaFi;

    public Sessio()
    {
        horaInici = "08:00";
        horaFi = "20:00";
    }

    public String getHoraInici() {
        return horaInici;
    }

    public void setHoraInici(String hora) {

        this.horaInici = hora;
    }

    public String getHoraFis() {
        return horaFi;
    }

    public void setHoraFi(String horaFi) {

        this.horaFi = horaFi;
    }
}

