package TFG_project.Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.LinkedList;
import java.util.List;

public class Meeting {

        private SimpleStringProperty sessio = new SimpleStringProperty("");
        private SimpleStringProperty entities= new SimpleStringProperty("");

        private LinkedList<String> listOfParticipants;
        private int nSessio = -1;
        private int nSlot = -1;
        private int nTaula = -1;


        public Meeting()
        {
            listOfParticipants = new LinkedList<>();
        }

        public Meeting(MeetingJson copy)
        {

            setSessio(copy.sessio);
            listOfParticipants = new LinkedList<>();

            nSessio = copy.nSessio;
            nSlot = copy.nSlot;
            nTaula = copy.nTaula;

            copy.listOfParticipants.forEach(u ->
            {
                try {
                    addMetting(u);
                } catch (Exception e) {

                }
            });

        }

        public String getSessio() {
            return sessio.get();
        }

        public void setSessio(String name) {
            this.sessio.set(name);
        }

        public StringProperty sessioProperty() {
            return sessio;
        }

        public String getEntities() {return entities.get();}

        public void setEntities(String meetingsString) {
            this.entities.set(meetingsString);
        }

        public StringProperty entitiesProperty() {
            return entities;
        }

        public LinkedList<String> getListOfParticipants(){
            return listOfParticipants;
        }

        public void cleanListOfParticipants()
        {
            listOfParticipants.clear();
            setEntities("");
        }

        public void addMetting(String met) throws Exception
        {
            if(listOfParticipants.contains(met))
                throw new Exception("Repeated");
            if(listOfParticipants.size() == 0)
                setEntities(met);
            else
                setEntities(getEntities()  + ", " +met);
            listOfParticipants.add(met);
        }

        public int getNSessio(){
            return nSessio;
        }

        public int getNSlot() {
            return nSlot;
        }

        public int getNTaula(){
            return nTaula;
        }

        public void saveTableInfo(int ses, int slot, int taula)
        {
            nSessio = ses;
            nTaula = taula;
            nSlot = slot;
        }

        public void resetSessio(int ses)
        {
            if(!sessio.get().equals("All"))
            {
                var list = sessio.get().split(" ");
                if(Integer.parseInt(list[1]) > ses)
                {
                    sessio.setValue("All");
                }
            }

            if(nSessio+1 > ses)
            {
                nSessio = -1;
                nTaula = -1;
                nSlot = -1;
            }
        }

        public void resetTable(int ses)
        {
            if(ses == nSessio || ses == -1)
            {
                nSessio = -1;
                nTaula = -1;
                nSlot = -1;
            }
        }


    }
