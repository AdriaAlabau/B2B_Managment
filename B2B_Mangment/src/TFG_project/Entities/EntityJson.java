package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class EntityJson {

    public String id;
    public String name;
    public String attendees;
    public String entrance;
    public List<String> listOfMeetings;
    public List<SessioAttending> listOfSessions;

    public EntityJson(Entity ent)
    {
        id = ent.getId();
        name = ent.getName();
        attendees = ent.getAttendees();
        entrance = ent.getEntrance();
        listOfSessions = ent.getListOfSessions();
        listOfMeetings = ent.getListOfMeetings();
    }
    public EntityJson(String i, String n, String a, String e, List<String> meetings, List<SessioAttending> sessions)
    {
        id = i;
        name = n;
        attendees = a;
        entrance = e;
        listOfSessions = sessions;
        listOfMeetings = meetings;
    }
}
