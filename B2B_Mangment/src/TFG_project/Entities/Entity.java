package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
        listOfSessions = new LinkedList<>();

        setAttendingSessions(copy.listOfSessions);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAttendees() {
        return attendees.get();
    }

    public void setAttendees(String attendees) {
        this.attendees.set(attendees);
    }

    public StringProperty attendeesProperty() {
        return attendees;
    }

    public String getEntrance() {
        return entrance.get();
    }

    public void setEntrance(String entrance) {
        this.entrance.set(entrance);
    }

    public StringProperty entranceProperty() {
        return entrance;
    }

    public void setAttendingSessionsString(String s)
    {
        attendingSessionsString.set(s);
    }

    public String getAttendingSessionsString()
    {
        return attendingSessionsString.getValue();
    }

    public StringProperty attendingSessionsStringProperty() {
        return attendingSessionsString;
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
                attendingSessionsString.set(attendingSessionsString.getValue().equals("") ? "Session " + (i+1) : attendingSessionsString.getValue() + ", Session " + (i+1));

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

    public void cantAttend(int posicio)
    {
        var size = listOfSessions.get(0).getAttendingSet().size();
        if(listOfSessions.get(0).getAttendingSet().size() < posicio)
        {
            listOfSessions.get(1).getAttendingSet().get(posicio-size-1).setR(false);
        }
        else
        {
            listOfSessions.get(0).getAttendingSet().get(posicio-1).setR(false);
        }
    }
}
