package TFG_project.Entities;

import java.util.LinkedList;
import java.util.List;

public class MainData {

    private static MainData mainData;
    private String eventName;
    private String eventLocation;
    private int nSessions;
    private LinkedList<Sessio> sessions;
    private LinkedList<Entity> entities;

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

    public void setNSessions(int nSes)
    {
        nSessions = nSes;
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

    public void setEntities(LinkedList<Entity> newEnt)
    {
        entities = newEnt;
    }

    public LinkedList<Entity> getEntities() {return entities;}

    public void setNewEntity(Entity newEntity)
    {
        entities.add(newEntity);
    }
}
