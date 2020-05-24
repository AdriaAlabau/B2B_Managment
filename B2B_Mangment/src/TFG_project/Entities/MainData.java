package TFG_project.Entities;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainData {

    private static MainData mainData;
    private String eventName;
    private String eventLocation;
    private int meetingsDuration;
    private int nSessions;
    private LinkedList<Sessio> sessions;
    private LinkedList<EntityJson> entities;
    private LinkedList<MeetingJson> meetingJsons;

    public static MainData SharedInstance()
    {
        if(mainData == null)
            mainData = new MainData();

        return mainData;
    }

    public MainData()
    {
        //inicialitzem
        nSessions = -1;
        eventName = "";
        eventLocation = "";
        entities = new LinkedList<>();
        meetingJsons = new LinkedList<>();
    }

    public void replaceInfo(MainData newMain)
    {
        mainData = newMain;
    }

    public String getEventName()
    {
        return  eventName;
    }

    public String getEventLocation()
    {
        return eventLocation;
    }

    public void setEventName(String newName)
    {
        eventName = newName;
    }

    public void setEventLocation(String newLocation)
    {
        eventLocation = newLocation;
    }

    public void setMeetingsDuration(int newDuration)
    {
        meetingsDuration = newDuration;
    }

    public int getMeetingsDuration() { return meetingsDuration; }

    public void setNSessions(int nSes)
    {
        nSessions = nSes;
        if(sessions != null && sessions.size() > nSes) {
            for (int i = sessions.size(); i > nSes; i--) {
                sessions.removeLast();
            }
        }
        else if(sessions != null && sessions.size() < nSes)
        {
            for(int i = sessions.size(); i<nSes; i++)
            {
                sessions.add(new Sessio());
            }
        }
    }

    public int GetMaxNTables()
    {
        int res = -1;
        for (var ses: sessions) {
            for(var tab : ses.getListOfTables().entrySet())
            {
                if(tab.getValue().nUnits > res)
                {
                    res = tab.getValue().nUnits;
                }
            };
        }
        return res;
    }

    public int GetMaxSlots()
    {
        int res = -1;
        for (var ses: sessions) {
            int size = ses.getSlots().size();
            res = size > res ? size : res;
        }
        return res;
    }

    public int getNSessions()
    {
        return nSessions;
    }

    public LinkedList<Sessio> getSessions()
    {
        if(sessions == null)
        {
            sessions = new LinkedList<>();
            for(int i = 0; i<nSessions; i++)
            {
                sessions.add(new Sessio());
            }
        }
        return sessions;
    }

    public void setSessions(LinkedList<Sessio> ss)
    {
        sessions = ss;
    }

    public void setSession(int d, Sessio ses)
    {
        sessions.set(d, ses);
    }

    public void setEntities(ObservableList<Entity> newEnt)
    {
        entities.clear();
        newEnt.forEach(e -> entities.add(new EntityJson(e)));
    }

    public void setMeetingJson(ObservableList<Meeting> newMet)
    {
        meetingJsons.clear();
        newMet.forEach(e -> meetingJsons.add(new MeetingJson(e)));
    }

    public LinkedList<Entity> getConvertedEntities()
    {
        LinkedList<Entity> lRet = new LinkedList<>();
        entities.forEach(e -> lRet.add(new Entity(e)));
        return lRet;
    }

    public LinkedList<Meeting> getConvertedMeetings()
    {
        LinkedList<Meeting> lRet = new LinkedList<>();
        meetingJsons.forEach(e -> lRet.add(new Meeting(e)));
        return lRet;
    }

    public LinkedList<EntityJson> getEntities() {
        return entities;
    }

    public void setNewEntity(EntityJson newEntity)
    {
        entities.add(newEntity);
    }

    public LinkedList<MeetingJson> getMeetings() {return meetingJsons;}

    public void setNewMeeting(MeetingJson newMeeting)
    {
        meetingJsons.add(newMeeting);
    }
}
