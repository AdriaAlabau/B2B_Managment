package TFG_project.Entities;

import java.util.LinkedList;
import java.util.List;

public class MainData {

    private static MainData mainData;
    private String eventName;
    private String eventLocation;
    private int nSessions;
    private List<Sessio> sessions;

    public static MainData SharedInstance()
    {
        if(mainData == null)
            mainData = new MainData();

        return mainData;
    }

    private MainData()
    {
        //inicialitzem
        sessions = new LinkedList<>();
        nSessions = -1;
        eventName = "";
        eventLocation = "";
    }

    public void setEventName(String newName)
    {
        eventName = newName;
    }

    public void setEventLocation(String newLocation)
    {
        eventLocation = newLocation;
    }

    public void setNumberOfSessions(int nSes)
    {
        nSessions = nSes;
    }

    public int getNSessions()
    {
        return nSessions;
    }
}
