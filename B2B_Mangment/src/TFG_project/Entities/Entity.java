package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Entity {
    private SimpleStringProperty id = new SimpleStringProperty("");;
    private SimpleStringProperty name = new SimpleStringProperty("");;
    private SimpleStringProperty attendees= new SimpleStringProperty("");;
    private SimpleStringProperty entrance= new SimpleStringProperty("");;
    private SimpleStringProperty meetingsString= new SimpleStringProperty("");;
    private List<String> listOfMeetings;

    public Entity()
    {
        listOfMeetings = new LinkedList<>();
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
}
