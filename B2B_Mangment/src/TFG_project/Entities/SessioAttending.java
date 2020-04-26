package TFG_project.Entities;

import TFG_project.HELPERS.Constants;

public class SessioAttending {

    private boolean attending;
    private String horaInici;
    private String horaFi;

    public SessioAttending(String start, String endHour)
    {
        attending = true;
        horaInici = start;
        horaFi = endHour;
    }

    public boolean getAttending() {return attending;}

    public void setAttending(boolean value)
    {
        attending = value;
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

    public void compareStart(String date)
    {
        int mine = Constants.ARRAYOFSTART.indexOf(horaInici);
        int theirs = Constants.ARRAYOFSTART.indexOf(date);
        if(mine < theirs)
            horaInici = date;
    }

    public void compareEnd(String date)
    {
        int mine = Constants.ARRAYOFEND.indexOf(horaFi);
        int theirs = Constants.ARRAYOFEND.indexOf(date);
        if(mine > theirs)
            horaFi = date;
    }
}
