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
    private SimpleStringProperty attendingSessionsString= new SimpleStringProperty("");
    private List<String> listOfMeetings;
    private List<Boolean> listOfSessions;

    public Entity(int nDays)
    {
        listOfMeetings = new LinkedList<>();
        listOfSessions = new LinkedList<>();
        for(int i = 0; i<nDays; i++)
        {
            listOfSessions.add(false);
        }
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

    public void addMetting(String met)
    {

        if(listOfMeetings.size() == 0)
            setMeetingsString(met);
        else
            setMeetingsString(getMeetingsString()  + ", " +met);
        listOfMeetings.add(met);
    }

    public String getAttendingSessionsString() {
        return attendingSessionsString.get();
    }

    public void setAttendingSessionsString(String meetingsString) {
        this.attendingSessionsString.set(meetingsString);
    }

    public void addAttendingDay(int day)
    {

        if(listOfSessions.size() == 0)
            setAttendingSessionsString(String.valueOf(day+1));
        else
            setAttendingSessionsString(getMeetingsString()  + ", " +String.valueOf(day+1));
        listOfSessions.set(day, Boolean.TRUE);
    }
}
