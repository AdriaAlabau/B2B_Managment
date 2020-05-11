package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.SCALA.Encoding;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.*;

public class ScheduleController {

    @FXML
    private TabPane tabPane;

    @FXML
    private VBox entitiesVBox;

    @FXML
    private VBox meetingsVBox;

    private LinkedList<CustomTab> listOfTabs;

    private class CustomTab
    {
        public Tab tab;
        private ScrollPane scrollPane;
        private GridPane gridPane;
        private Sessio sessio;

        public CustomTab(Sessio ses, int i)
        {
            sessio = ses;
            tab = new Tab("Session " + i);

            scrollPane = new ScrollPane();
            tab.setContent(scrollPane);

            gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);

            ColumnConstraints column = new ColumnConstraints(70);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);

            gridPane.getRowConstraints().add(new RowConstraints(30));

            sessio.getSlots().forEach(s -> {
                gridPane.getRowConstraints().add(new RowConstraints(40));
                Label label = new Label(s);

                gridPane.add(label, 0, gridPane.getRowCount()-1);
            });

            sessio.getListOfTables().forEach((k,v) -> {
                ColumnConstraints auxiliarColumn = new ColumnConstraints(100);
                column.setHalignment(HPos.CENTER);

                gridPane.getColumnConstraints().add(column);
                Label label = new Label("Table " + k);

                gridPane.add(label, gridPane.getColumnCount()-1, 0);
            });
            scrollPane.setContent(gridPane);
        }
    }

    @FXML
    protected void initialize() {

        Platform.runLater(() -> {

            //SET ENTITIES AND MEETINGS TO VBOX
            //mediumHBOX.setSpacing(30);
            //mediumHBOX.setPadding(new Insets(10,10,10,10));
            listOfTabs = new LinkedList<>();
            int i = 1;

            //SET SESSIONS
            for(Sessio ses : MainData.SharedInstance().getSessions())
            {

                CustomTab auxiliar = new CustomTab(ses,i);
                tabPane.getTabs().add(auxiliar.tab);
                listOfTabs.add(auxiliar);
                i++;
            }

            for(EntityJson ent : MainData.SharedInstance().getEntities())
            {
                StackPane aux = new StackPane();
                aux.setStyle("-fx-background-color: darkGrey" );
                Label label = new Label(ent.name);
                aux.getChildren().add(label);
                aux.setPadding(new Insets(5,5,5,5));
                aux.setAlignment(Pos.CENTER);
                entitiesVBox.getChildren().add(aux);
            }
            int j = 0;
            for(MeetingJson meet : MainData.SharedInstance().getMeetings())
            {
                StackPane aux = new StackPane();
                aux.setStyle("-fx-background-color: darkGrey" );
                Label label = new Label("Meeting " + j);
                aux.getChildren().add(label);
                aux.setPadding(new Insets(5,5,5,5));
                aux.setAlignment(Pos.CENTER);
                meetingsVBox.getChildren().add(aux);
                j++;
            }
        });
    }

    public void computeSchedule()
    {

        ///nMeetings : Int, nSlots: Int, nSessions : Int, nAttendeesParticipant : Array[Int], taulesXSessio : Array[Array[Int]], mettingXParticipant : Array[Array[Int]],  forbidden: Array[Array[Int]], gapSlots: Array[Array[Int]] , gapMeetings: Array[Array[Int]], participants : Array[Array[Int]]

        HashMap<String, Integer> entitiesIdToPos = new HashMap<>();
        int posEntity = 0;
        int posSlot = 0;

        Vector<Integer> nAttendeesParticipant = new Vector<>();
        Vector<Integer> taulesXSessio = new Vector<>();
        Vector<Vector<Integer>> meetings = new Vector<>();
        Vector<Vector<Integer>> forbidden = new Vector<>();
        Vector<Vector<Integer>> gapSlots = new Vector<>();
        Vector<Vector<Integer>> gapMeetings = new Vector<>(); // fer al reves???
        Vector<Vector<Integer>> participants = new Vector<>();

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
            posEntity++;

            forbidden.add( x.getForbbiden());
            meetings.add(new Vector<>());
        }

        for(var x : MainData.SharedInstance().getSessions())
        {
            taulesXSessio.add(x.getListOfTables().size());
            Vector<Integer> listOfSlots = new Vector<>();
            for(var slot : x.getSlots())
            {
                listOfSlots.add(posSlot);
                posSlot++;
            }
            gapSlots.add(listOfSlots);
        }

        int meetingCounter = 0;
        for(var x : MainData.SharedInstance().getMeetings())
        {
            var set = new Vector<Integer>();
            for(var j : x.listOfParticipants)
            {
                var entityPos =entitiesIdToPos.get(x);
                set.add(entityPos);
                meetings.get(entityPos).add(meetingCounter);
            }
            participants.add(set);
            meetingCounter++;
        }
        
        Encoding.codificar(MainData.SharedInstance().getMeetings().size(),0, MainData.SharedInstance().getNSessions(), (Integer[]) nAttendeesParticipant.toArray(), (Integer[][])taulesXSessio.toArray(),
                (Integer[][])meetings.toArray(), (Integer[][])forbidden.toArray(),  (Integer[][])gapSlots.toArray(), new Integer[2][], (Integer[][])participants.toArray() );
    }
}
