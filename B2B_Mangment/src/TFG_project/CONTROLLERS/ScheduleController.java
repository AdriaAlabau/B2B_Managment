package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.SCALA.Encoding;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.*;

public class ScheduleController {

    private class MeetingScheduled
    {
        public MeetingJson meeting;
        public StackPane stackPane;
        public int id = 0;
        public int sessio = -1;
        public int slot = -1;
        public int taula = -1;
    }

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox meetingsVBox;

    private ArrayList<CustomTab> listOfTabs;

    private ArrayList<MeetingScheduled> listOfScheduledMeetings;

    private class CustomTab
    {
        public Tab tab;
        private ScrollPane scrollPane;
        private GridPane gridPane;
        private Sessio sessio;
        private int nTaules = 0;
        private HashMap<Integer, ArrayList<MeetingScheduled>> meetingsInSlots;

        public CustomTab(Sessio ses, int i)
        {
            sessio = ses;
            tab = new Tab("Session " + i);

            meetingsInSlots = new HashMap<>();

            scrollPane = new ScrollPane();
            tab.setContent(scrollPane);

            gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);

            ColumnConstraints column = new ColumnConstraints(70);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);

            gridPane.getRowConstraints().add(new RowConstraints(50));

            sessio.getSlots().forEach(s -> {
                gridPane.getRowConstraints().add(new RowConstraints(40));

                Label label = new Label(s);

                gridPane.add(label, 0, gridPane.getRowCount()-1);
                meetingsInSlots.put(gridPane.getRowCount()-2, new ArrayList<>());
            });

            int mainTableCounter = 1;


            for(Map.Entry<Integer,TableForSession> entry : sessio.getListOfTables().entrySet())
            {
                for(int counter = 0 ; counter< entry.getValue().nUnits; counter++)
                {
                    ColumnConstraints auxiliarColumn = new ColumnConstraints(100);
                    auxiliarColumn.setHalignment(HPos.CENTER);

                    gridPane.getColumnConstraints().add(auxiliarColumn);
                    Label label = new Label("Table " + mainTableCounter + "\nSeats " +  entry.getKey());

                    mainTableCounter++;
                    gridPane.add(label, gridPane.getColumnCount()-1, 0);
                    nTaules++;
                }
            }

            scrollPane.setContent(gridPane);
        }

        public void addMeeting(MeetingScheduled meet, int slot)
        {
            int i = 1;
            while(i<= nTaules && getNodeFromGridPane(gridPane, i,slot+1) != null)
            {
                i++;
            }

            gridPane.add(meet.stackPane, i, slot+1);
            GridPane.setMargin(meet.stackPane, new Insets(5, 5, 5, 5));
        }

        private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
            for (Node node : gridPane.getChildren()) {

                var colu = GridPane.getColumnIndex(node);
                if(colu != null) {
                    if (GridPane.getColumnIndex(node) == col && gridPane.getRowIndex(node) == row) {
                        return node;
                    }
                }
            }
            return null;
        }

    }

    @FXML
    protected void initialize() {

        Platform.runLater(() -> {

            //SET ENTITIES AND MEETINGS TO VBOX
            //mediumHBOX.setSpacing(30);
            //mediumHBOX.setPadding(new Insets(10,10,10,10));
            listOfTabs = new ArrayList<>();

            listOfScheduledMeetings = new ArrayList<>();
            int i = 1;

            //SET SESSIONS
            for(Sessio ses : MainData.SharedInstance().getSessions())
            {
                CustomTab auxiliar = new CustomTab(ses,i);
                tabPane.getTabs().add(auxiliar.tab);
                listOfTabs.add(auxiliar);
                i++;
            }

            int j = 0;
            for(MeetingJson meet : MainData.SharedInstance().getMeetings())
            {
                MeetingScheduled meetingScheduled = new MeetingScheduled();
                meetingScheduled.stackPane= new StackPane();
                meetingScheduled.stackPane.setStyle("-fx-background-color: darkGrey" );

                Label label = new Label("Meeting " + j);

                meetingScheduled.stackPane.getChildren().add(label);
                meetingScheduled.stackPane.setPadding(new Insets(5,5,5,5));

                meetingScheduled.stackPane.setAlignment(Pos.CENTER);

                meetingsVBox.getChildren().add(meetingScheduled.stackPane);

                meetingScheduled.id = j;
                meetingScheduled.meeting = meet;

                listOfScheduledMeetings.add(meetingScheduled);

                j++;
            }
        });
    }

    public void computeSchedule()
    {
        ///nMeetings : Int, nSlots: Int, nSessions : Int, nAttendeesParticipant : Array[Int], taulesXSessio : Array[Array[Int]], mettingXParticipant : Array[Array[Int]],  forbidden: Array[Array[Int]], sessioSlots: Array[Array[Int]] , sessionMeetings: Array[Array[Int]], meetingSessions: Array[Array[Int]], participants : Array[Array[Int]]

        HashMap<String, Integer> entitiesIdToPos = new HashMap<>();
        int posEntity = 0;
        int posSlot = 0;
        int mainCounterSlots = 0;

        ArrayList<Object> nAttendeesParticipant = new ArrayList<>();
        ArrayList<Object> taulesXSessio = new ArrayList<>();
        ArrayList<ArrayList<Object>> entityMeetings = new ArrayList<>();
        ArrayList<ArrayList<Object>> meetingEntities = new ArrayList<>();
        ArrayList<ArrayList<Object>> forbidden = new ArrayList<>();
        ArrayList<ArrayList<Object>> sessioSlots = new ArrayList<>();
        ArrayList<ArrayList<Object>> sessionMeetings = new ArrayList<>(); // Per cada sessio, quines reunions poden tenir lloc
        ArrayList<ArrayList<Object>> meetingSessions = new ArrayList<>(); // Per cada reunio, a quines sessions pot tenir lloc

        for(var x : MainData.SharedInstance().getSessions())
        {
            taulesXSessio.add(x.getListOfTables().size());
            ArrayList<Object> listOfSlots = new ArrayList<>();
            for(var slot : x.getSlots())
            {
                listOfSlots.add(posSlot);
                posSlot++;
            }
            sessioSlots.add(listOfSlots);
            mainCounterSlots += listOfSlots.size();
            sessionMeetings.add(new ArrayList<>());
        }

        for(var x : MainData.SharedInstance().getEntities())
        {
            try {
                nAttendeesParticipant.add(Integer.parseInt(x.attendees));
            }
            catch (Exception e)
            {
                nAttendeesParticipant.add(1);
            }
            entitiesIdToPos.put(x.id, posEntity);

            forbidden.add( x.getForbbiden());

            entityMeetings.add(new ArrayList<>());

            posEntity++;
        }

        int meetingCounter = 0;
        for(var x : MainData.SharedInstance().getMeetings())
        {
            var set = new ArrayList<Object>();

            for(var j : x.listOfParticipants)
            {
                var entityPos =entitiesIdToPos.get(j);
                set.add(entityPos);
                entityMeetings.get(entityPos).add(meetingCounter);
            }

            meetingEntities.add(set);

            meetingSessions.add(x.getSessions());

            int finalMeetingCounter = meetingCounter;
            x.getSessions().forEach(ses -> sessionMeetings.get((int)ses).add(finalMeetingCounter));

            meetingCounter++;
        }

        try {
            var result = Encoding.codificar(MainData.SharedInstance().getMeetings().size(), // nMeetings : Int
                    mainCounterSlots, // nSlots:Int
                    MainData.SharedInstance().getNSessions(),  //nSessions : Int
                    nAttendeesParticipant, // nAttendeesParticipant : Array[Int]
                    taulesXSessio, // taulesXSessio : Array[Array[Int]]
                    entityMeetings,  // mettingXParticipant : Array[Array[Int]]
                    forbidden,  //  forbidden: Array[Array[Int]]
                    sessioSlots, // sessioSlots: Array[Array[Int]]
                    sessionMeetings, // sessionMeetings: Array[Array[Int]]
                    meetingSessions,// meetingSessions: Array[Array[Int]]
                    meetingEntities);  // meetingEntities: Array[Array[Int]]


            for (int i = 0; i< result.size(); i++)  {
                var tab = listOfTabs.get(i);
                var sessioArray = result.get(i);
                for(int j = 0; j < sessioArray.size(); j++ )
                {
                    for(var meetPos : sessioArray.get(j))
                    {
                        var meeting = listOfScheduledMeetings.get((int)meetPos);
                        meetingsVBox.getChildren().remove(meeting.stackPane);
                        tab.addMeeting(meeting, j);
                    }
                }

            }
        }
        catch(Exception e)
        {
            int x = 0;
        }
    }
}
