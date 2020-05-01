package TFG_project.Entities;

import java.util.List;

public class MeetingJson {
    public String sessio;
    public List<String> listOfParticipants;

    public MeetingJson(Meeting ent)
    {
        sessio = ent.getSessio();
        listOfParticipants = ent.getListOfParticipants();
    }
    public MeetingJson(String s, List<String> meetings)
    {
        sessio = s;
        listOfParticipants = meetings;
    }
}
