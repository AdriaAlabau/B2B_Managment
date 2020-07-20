package TFG_project.Entities;

import TFG_project.HELPERS.DateConverter;
import TFG_project.HELPERS.Pair;
import TFG_project.Main;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.*;

public class Sessio {

    private LocalDate date;
    private String horaInici;
    private String horaFi;
    private LinkedList<String> slots;
    private HashMap<Integer,TableForSession> listOfTables;

    public Sessio()
    {
        date = LocalDate.now();
        horaInici = "08:00";
        horaFi = "20:00";
        listOfTables = new HashMap<Integer,TableForSession>();
        listOfTables.put(0,new TableForSession(0,1));
        //listOfTables.put(2,new TableForSession(2,1));
        //listOfTables.put(3,new TableForSession(3,1));

        slots = new LinkedList<>();
        computeSlots(MainData.SharedInstance().getMeetingsDuration());
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
        computeSlots(MainData.SharedInstance().getMeetingsDuration());
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

    public LinkedList<String> getSlots(){ return slots; }

    private void computeSlots(int meetingsDuration)
    {
        slots.clear();
        var strStart = horaInici.split(":");
        int startHour = Integer.parseInt(strStart[0]);
        int startMinute = Integer.parseInt(strStart[1]);

        var strEnd = horaFi.split(":");
        int endHour = Integer.parseInt(strEnd[0]);
        int endMinute = Integer.parseInt(strEnd[1]);


        while(startHour < endHour || startMinute < endMinute)
        {
            slots.add(DateConverter.intToStrTime(startHour)+ ":" + DateConverter.intToStrTime(startMinute));
            startMinute = startMinute + meetingsDuration;
            double res = ((double)startMinute) / 60;
            if(res >= 1)
            {
                var exces = startMinute-60;
                startHour++;
                startMinute = exces;
            }
        }
    }

    public void resetSlots(int duration)
    {
        computeSlots(duration);
    }

    public void setDebugInfo(int nTotalSlots, int duration, int nTables)
    {
        listOfTables.get(0).nUnits = nTables;
        horaInici = "07:00";
        var hFi = duration*nTotalSlots;
        var m = (hFi % 60) ;
        int h = ((int)(hFi / 60)  + 7 );

        horaFi = (h> 9 ? "" : "0") + h +":" + (m>9 ? "" : "0" ) + m;
        computeSlots(duration);


    }
}

