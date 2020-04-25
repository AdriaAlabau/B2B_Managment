package TFG_project.Entities;

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
}
