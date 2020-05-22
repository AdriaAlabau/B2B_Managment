package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class EntityJson {

    public String id;
    public String name;
    public String attendees;
    public String entrance;
    public List<SessioAttending> listOfSessions;

    public EntityJson(Entity ent)
    {
        id = ent.getId();
        name = ent.getName();
        attendees = ent.getAttendees();
        entrance = ent.getEntrance();
        listOfSessions = ent.getListOfSessions();
    }
    public EntityJson(String i, String n, String a, String e, List<SessioAttending> sessions)
    {
        id = i;
        name = n;
        attendees = a;
        entrance = e;
        listOfSessions = sessions;
    }

    public ArrayList<Object> getForbbiden()
    {
        int mainCounter = 0;
        ArrayList<Object> lRet = new ArrayList<>();

        for (var session: listOfSessions) {
            boolean attending= session.getAttending();
            for(var slot : session.getAttendingSet())
            {
                if(!attending || !slot.getR())
                    lRet.add(mainCounter);

                mainCounter++;
            }
        }

        return lRet;
    }


    public ArrayList<Object> getMeetings()
    {
        int mainCounter = 0;
        ArrayList<Object> lRet = new ArrayList<>();
        for (var session: listOfSessions) {
            if(session.getAttending())
                lRet.add(mainCounter);

            mainCounter++;
        }
        return lRet;
    }
}
