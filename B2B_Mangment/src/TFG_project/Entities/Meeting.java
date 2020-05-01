package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Meeting {

        private SimpleStringProperty sessio = new SimpleStringProperty("");
        private SimpleStringProperty entities= new SimpleStringProperty("");

        private LinkedList<String> listOfParticipants;


        public Meeting()
        {
            listOfParticipants = new LinkedList<>();
        }

        public Meeting(MeetingJson copy)
        {

            setSessio(copy.sessio);
            listOfParticipants = new LinkedList<>();

            copy.listOfParticipants.forEach(u -> addMetting(u));

        }

        public String getSessio() {
            return sessio.get();
        }

        public void setSessio(String name) {
            this.sessio.set(name);
        }

        public String getEntities() {return entities.get();}

        public void setEntities(String meetingsString) {
            this.entities.set(meetingsString);
        }

        public LinkedList<String> getListOfParticipants(){
            return listOfParticipants;
        }

        public void addMetting(String met)
        {
            if(listOfParticipants.size() == 0)
                setEntities(met);
            else
                setEntities(getEntities()  + ", " +met);
            listOfParticipants.add(met);
        }

    }
