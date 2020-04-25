package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Entity {
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty attendees= new SimpleStringProperty("");
    private SimpleStringProperty entrance= new SimpleStringProperty("");
    private SimpleStringProperty meetingsString= new SimpleStringProperty("");
    //private SimpleStringProperty attendingSessionsString= new SimpleStringProperty("");
    private LinkedList<String> listOfMeetings;
    private LinkedList<SessioAttending> listOfSessions;

    public Entity(List<Sessio> days)
    {
        listOfMeetings = new LinkedList<>();
        listOfSessions = new LinkedList<>();
        for(Sessio unit: days)
        {
            listOfSessions.add(new SessioAttending(unit.getHoraInici(), unit.getHoraFis()));
        }
    }

    public Entity(EntityJson copy)
    {
        setId(copy.id);
        setName(copy.name);
        setAttendees(copy.attendees);
        setEntrance(copy.entrance);
        listOfMeetings = new LinkedList<>();
        listOfSessions = new LinkedList<>();

        copy.listOfMeetings.forEach(u -> addMetting(u));

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

    public String getMeetingsString() {
        return meetingsString.get();
    }

    public void setMeetingsString(String meetingsString) {
        this.meetingsString.set(meetingsString);
    }

    public LinkedList<SessioAttending> getListOfSessions(){
        return listOfSessions;
    }

    public LinkedList<String> getListOfMeetings(){
        return listOfMeetings;
    }

    public void setAttendingSessions(List<SessioAttending> sessions)
    {
        listOfSessions = new LinkedList<>();
        listOfSessions.addAll(sessions);
    }

    public void addMetting(String met)
    {

        if(listOfMeetings.size() == 0)
            setMeetingsString(met);
        else
            setMeetingsString(getMeetingsString()  + ", " +met);
        listOfMeetings.add(met);
    }
}
