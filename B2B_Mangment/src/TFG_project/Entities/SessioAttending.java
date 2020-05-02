package TFG_project.Entities;

import TFG_project.HELPERS.Constants;
import TFG_project.HELPERS.Pair;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class SessioAttending {

    private boolean attending;
    private LinkedList<Pair<String,Boolean>> attendingSes;

    public SessioAttending(String start, String end, int sep)
    {
        attending = true;
        attendingSes = new LinkedList<>();

        var strStart = start.split(":");
        int startHour = Integer.parseInt(strStart[0]);
        int startMinute = Integer.parseInt(strStart[1]);

        var strEnd = end.split(":");
        int endHour = Integer.parseInt(strEnd[0]);
        int endMinute = Integer.parseInt(strEnd[1]);

        while(startHour < endHour || startMinute < endMinute)
        {
            attendingSes.add(new Pair(intToStrTime(startHour)+ ":" + intToStrTime(startMinute), true));
            startMinute = startMinute + sep;
            double res = ((double)startMinute) / 60;
            if(res >= 1)
            {
                startHour++;
                res = res-1;
                startMinute = (int)(res*60);
            }
        }
    }

    private String intToStrTime(int time)
    {
        if(time <10)
            return "0" + time;
        else
            return String.valueOf(time);
    }

    public SessioAttending(LinkedList<Pair<String,Boolean>> attendigSet)
    {
        attending = true;
        attendingSes = attendigSet;
    }

    public boolean getAttending() {return attending;}

    public void setAttending(boolean value)
    {
        attending = value;
    }

    public void compareStart(String date)
    {
        while(!attendingSes.getFirst().getL().equals(date))
        {
            attendingSes.remove(0);
        }
    }

    public void compareEnd(String date)
    {
        //TODO
        /*while(!attendingSes.getLast().getL().equals(date) && !attendingSes.get(attendingSes.size()-2).getL().equals(date))
        {
            attendingSes.remove(attendingSes.size()-1);
        }*/
    }

   public void setAttendingSes(LinkedList<Pair<String,Boolean>> set)
   {
       attendingSes = set;
   }

   public void setWillAttend(int pos, boolean value)
   {
       attendingSes.get(pos).setR(value);
   }

    public LinkedList<Pair<String,Boolean>> getAttendingSet()
    {
        return attendingSes;
    }
}
