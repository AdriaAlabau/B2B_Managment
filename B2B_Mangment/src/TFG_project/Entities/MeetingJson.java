package TFG_project.Entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MeetingJson {
    public String sessio;
    public LinkedList<String> listOfParticipants;

    public MeetingJson(Meeting ent)
    {
        sessio = ent.getSessio();
        listOfParticipants = ent.getListOfParticipants();
    }
    public MeetingJson(String s, LinkedList<String> meetings)
    {
        sessio = s;
        listOfParticipants = meetings;
    }

    public ArrayList<Object> getSessions()
    {
        var lRet = new ArrayList<>();
        if(sessio.equals("All"))
        {
            for(int i = 0; i<MainData.SharedInstance().getNSessions(); i++)
                lRet.add(i);
        }
        else
        {
            var str = sessio.split(" ");
            var value = Integer.parseInt(str[1]) -1;
            lRet.add(value);
        }
        return lRet;
    }

    public boolean containsEntity(String name)
    {
        boolean bRet = false;
        int i = 0;
        while(i<listOfParticipants.size() && !bRet)
        {
            bRet = listOfParticipants.get(i).equals(name);
            i++;
        }
        return bRet;
    }
}
