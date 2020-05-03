package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Entity {
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty attendees= new SimpleStringProperty("");
    private SimpleStringProperty entrance= new SimpleStringProperty("");
    private SimpleStringProperty attendingSessionsString= new SimpleStringProperty("");
    private LinkedList<SessioAttending> listOfSessions;

    public Entity(List<Sessio> days)
    {
        listOfSessions = new LinkedList<>();
        for(Sessio unit: days)
        {
            listOfSessions.add(new SessioAttending(unit.getSlots()));
        }
        redoSessionsString();
    }

    public Entity(EntityJson copy)
    {
        setId(copy.id);
        setName(copy.name);
        setAttendees(copy.attendees);
        setEntrance(copy.entrance);
        listOfSessions = new LinkedList<>();

        setAttendingSessions(copy.listOfSessions);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAttendees() {
        return attendees.get();
    }

    public void setAttendees(String attendees) {
        this.attendees.set(attendees);
    }

    public String getEntrance() {
        return entrance.get();
    }

    public void setEntrance(String entrance) {
        this.entrance.set(entrance);
    }

    public void setAttendingSessionsString(String s)
    {
        attendingSessionsString.set(s);
    }

    public String getAttendingSessionsString()
    {
        return attendingSessionsString.getValue();
    }

    public LinkedList<SessioAttending> getListOfSessions(){
        return listOfSessions;
    }

    public void setAttendingSessions(List<SessioAttending> sessions)
    {
        listOfSessions = new LinkedList<>();
        listOfSessions.addAll(sessions);
        redoSessionsString();
    }

    public void redoSessionsString()
    {
        attendingSessionsString.set("");

        for (int i = 0; i< listOfSessions.size(); i++){
            if(listOfSessions.get(i).getAttending())
                attendingSessionsString.set(attendingSessionsString.getValue().equals("") ? "Session " + i : attendingSessionsString.getValue() + ", Session " + i);

        }

    }

    public void checkSessionsIntegrity(LinkedList<Sessio> sessions)
    {
        int i = 0;
        for(Sessio ses : sessions)
        {
            if(listOfSessions.size() >i)
            {
                var current = listOfSessions.get(i);
                current.compareStart(ses.getHoraInici());
                current.compareEnd(ses.getHoraFis());
            }
            else
            {
                listOfSessions.add(new SessioAttending(ses.getSlots()));
            }
            i++;
        }
        if(listOfSessions.size() >i) {
            for (int j = listOfSessions.size(); j > i; j--) {
                listOfSessions.removeLast();
            }
        }
    }
}
