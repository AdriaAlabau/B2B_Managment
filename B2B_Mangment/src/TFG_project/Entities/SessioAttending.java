package TFG_project.Entities;

import TFG_project.HELPERS.Constants;
import TFG_project.HELPERS.Pair;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

public class SessioAttending {

    private boolean attending;
    private LinkedList<Pair<String,Boolean>> attendingSes;

    public SessioAttending(List<String> slots)
    {
        attending = true;
        attendingSes = new LinkedList<>();

        slots.forEach(s -> attendingSes.add(new Pair(s, true)));
    }


    /*public SessioAttending(LinkedList<Pair<String,Boolean>> attendigSet)
    {
        attending = true;
        attendingSes = attendigSet;
    }*/

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

    public void resetAttendingSet(List<String> slots)
    {
        attendingSes.clear();
        slots.forEach(s -> attendingSes.add(new Pair(s, true)));
    }
}
