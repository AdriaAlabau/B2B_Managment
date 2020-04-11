package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Sessio {
    private List<SimpleStringProperty> taulesConfig;
    private SimpleStringProperty horaInici= new SimpleStringProperty("");
    private SimpleStringProperty horaFi= new SimpleStringProperty("");

    public Sessio()
    {
        taulesConfig = new LinkedList<>();
    }

    public List<SimpleStringProperty> getTaulesConfig() {
        return taulesConfig;
    }

    public void addTaulaConfig(String taula) {
        taulesConfig.add(new SimpleStringProperty(taula));
    }


    public String getHoraInici() {
        return horaInici.get();
    }

    public void setHoraInici(String hora) {
        this.horaInici.set(hora);
    }

    public String getHoraFis() {
        return horaFi.get();
    }

    public void setHoraFi(String horaFi) {
        this.horaFi.set(horaFi);
    }
}

