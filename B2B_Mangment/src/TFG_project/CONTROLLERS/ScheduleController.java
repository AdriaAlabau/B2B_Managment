package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.AlertDialog;
import TFG_project.HELPERS.SimpleClass;
import TFG_project.HELPERS.WorkIndicatorDialog;
import TFG_project.SCALA.Encoding;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ScheduleController {

    private final DataFormat stackPaneFormat = new DataFormat("STACKPANE");

    private final String bruh = "WILDACARD";

    private MeetingScheduled draggingMeeting;

    private class MeetingScheduled
    {
        public MeetingJson meeting;
        public StackPane parent;
        public StackPane stackPane;
        public int id = 0;
        public int sessio = -1;
        public int slot = -1;
        public String hour = null;
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
        private int sessioIndex;
        private ScrollPane scrollPane;
        private GridPane gridPane;
        private Sessio sessio;
        private int nTaules = 0;
        private HashMap<Integer, ArrayList<MeetingScheduled>> meetingsInSlots;

        public CustomTab(Sessio ses, int i)
        {
            sessioIndex = i-1;
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
                    ColumnConstraints auxiliarColumn = new ColumnConstraints(170);
                    auxiliarColumn.setHalignment(HPos.CENTER);

                    gridPane.getColumnConstraints().add(auxiliarColumn);
                    Label label = new Label("Table " + mainTableCounter);

                    mainTableCounter++;
                    gridPane.add(label, gridPane.getColumnCount()-1, 0);
                    nTaules++;

                    for(int wild = 0; wild <sessio.getSlots().size(); wild++)
                    {
                        StackPane pane = new StackPane();
                        pane.setPadding(new Insets(5,5,5,5));
                        pane.setId(bruh);
                        gridPane.add(pane, gridPane.getColumnCount()-1, wild+1);
                    }
                }
            }


            scrollPane.setContent(gridPane);
        }

        public void addMeeting(MeetingScheduled meet, int slot)
        {
            int i = 1;
            boolean cont = true;
            while(i<= nTaules && cont)
            {

                Node n = getNodeFromGridPane(gridPane, i,slot+1);
                if(n.getId().equals(bruh)) {
                    var node = (StackPane)n;
                    cont = node.getChildren().size() != 0;
                    meet.taula = i;
                    node.getChildren().add(meet.stackPane);
                    meet.parent = node;
                    meet.slot = slot;
                    meet.sessio = sessioIndex;
                }
                i++;
            }
        }

        public boolean addAtTable(CustomTab tab, MeetingScheduled meeting, int x, int y)
        {

            Node n = getNodeFromGridPane(gridPane, x,y);
            if(n.getId().equals(bruh) && ((StackPane)n).getChildren().size() == 0)
            {
                StackPane pane = (StackPane)n;
                pane.getChildren().add(meeting.stackPane);
                meeting.parent = pane;
                moveMeeting(tab, draggingMeeting, tabPane.getSelectionModel().getSelectedIndex(), y, false);
                draggingMeeting.taula = x;
                return true;
            }
            return false;
        }

        public boolean canGoThere(int columna, int row)
        {
            return getNodeFromGridPane(gridPane, columna,row).getId().equals(bruh);
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

            listOfTabs = new ArrayList<>();

            listOfScheduledMeetings = new ArrayList<>();
            int i = 1;

            //SET SESSIONS
            for(Sessio ses : MainData.SharedInstance().getSessions())
            {
                CustomTab auxiliar = new CustomTab(ses,i);
                tabPane.getTabs().add(auxiliar.tab);
                listOfTabs.add(auxiliar);

                var list = auxiliar.gridPane.getChildren();
                for(int pos = 1; pos <list.size(); pos++) {
                    Node node = list.get(pos);


                    node.setOnDragOver(new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            //data is dragged over to target
                            //accept it only if it is not dragged from the same node
                            //and if it has image data

                            //allow for moving

                            event.acceptTransferModes(TransferMode.MOVE);

                            event.consume();
                        }
                    });

                    node.setOnDragDropped(new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {

                            Dragboard db = event.getDragboard();

                            boolean success = false;
                            if (db.hasContent(stackPaneFormat)) {
                                //Node node = event.getPickResult().getIntersectedNode();
                                CustomTab tab = listOfTabs.get(tabPane.getSelectionModel().getSelectedIndex());

                                Integer cIndex = GridPane.getColumnIndex(node);
                                Integer rIndex = GridPane.getRowIndex(node);
                                int x = cIndex == null ? 0 : cIndex;
                                int y = rIndex == null ? 0 : rIndex;

                                success = tab.addAtTable(tab, draggingMeeting, x, y);

                            }

                            event.setDropCompleted(success);

                            event.consume();
                        }
                    });
                }

                i++;
            }

            int j = 0;
            for(MeetingJson meet : MainData.SharedInstance().getMeetings())
            {
                MeetingScheduled meetingScheduled = new MeetingScheduled();
                meetingScheduled.stackPane= new StackPane();
                meetingScheduled.stackPane.setStyle("-fx-background-color: darkGrey" );
                meetingScheduled.stackPane.setId(String.valueOf(j));

                meetingScheduled.stackPane.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2) {

                        String pos = ((StackPane) event.getSource()).getId();
                        MeetingScheduled meeting = listOfScheduledMeetings.get(Integer.parseInt(pos));

                        Stage meetingDetail = new Stage();
                        meetingDetail.initModality(Modality.APPLICATION_MODAL);
                        meetingDetail.initOwner(tabPane.getScene().getWindow());

                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/meeting_scheduled_detail.fxml"));
                            Parent root = (Parent) fxmlLoader.load();
                            MeetingScheduledDetailController controller = fxmlLoader.<MeetingScheduledDetailController>getController();
                            controller.setMeeting(meeting.meeting.sessio, meeting.sessio, meeting.hour, meeting.taula, meeting.meeting.listOfParticipants);


                            Scene scene = new Scene(root);
                            meetingDetail.setMinWidth(root.minWidth(-1));
                            meetingDetail.setMinHeight(root.minHeight(-1));
                            meetingDetail.setScene(scene);

                            meetingDetail.show();

                        } catch (IOException e) {
                            int x = 0;
                        }
                    }
                });

                String meetingName = meet.listOfParticipants.get(0) + " - " + meet.listOfParticipants.get(1) + ( meet.listOfParticipants.size() > 2 ?  "..." : "");
                Label label = new Label(meetingName);

                meetingScheduled.stackPane.getChildren().add(label);
                meetingScheduled.stackPane.setPadding(new Insets(5,5,5,5));
                GridPane.setMargin(meetingScheduled.stackPane, new Insets(5,5,5,5));

                meetingScheduled.stackPane.setAlignment(Pos.CENTER);

                meetingsVBox.getChildren().add(meetingScheduled.stackPane);

                meetingScheduled.id = j;
                meetingScheduled.meeting = meet;

                listOfScheduledMeetings.add(meetingScheduled);

                meetingScheduled.stackPane.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        //Drag was detected, start drap-and-drop gesture
                        //Allow any transfer node
                        Dragboard db = meetingScheduled.stackPane.startDragAndDrop(TransferMode.MOVE);

                        db.setDragView(meetingScheduled.stackPane.snapshot(null, null));
                        ClipboardContent cc = new ClipboardContent();
                        cc.put(stackPaneFormat, " ");
                        db.setContent(cc);
                        draggingMeeting = meetingScheduled;

                        meetingScheduled.stackPane.setVisible(false);

                        event.consume();
                    }
                });

                meetingScheduled.stackPane.setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event){
                        draggingMeeting.stackPane.setVisible(true);
                        draggingMeeting = null;
                        event.consume();
                    }
                });

                //Drag over event handler is used for the receiving node to allow movement

                j++;
            }


            meetingsVBox.setOnDragOver(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    //data is dragged over to target
                    //accept it only if it is not dragged from the same node
                    //and if it has image data

                    //allow for moving

                    event.acceptTransferModes(TransferMode.MOVE);

                    event.consume();
                }
            });

            meetingsVBox.setOnDragDropped(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (draggingMeeting.taula != -1) {
                        if (db.hasContent(stackPaneFormat)) {
                            success = true;
                            moveMeeting(meetingsVBox, draggingMeeting, 0,0, true);
                        }
                    }

                    //draggingMeeting.stackPane.setVisible(true);
                    //draggingMeeting = null;

                    //let the source know whether the image was successfully transferred and used
                    event.setDropCompleted(success);

                    event.consume();
                }
            });
        });
    }



    public void computeSchedule()
    {

        WorkIndicatorDialog wd = new WorkIndicatorDialog(meetingsVBox.getScene().getWindow(), "Computing schedule...");

        wd.addTaskEndNotification(result -> {
             // don't keep the object, cleanup
        });

        wd.exec("", inputParam -> {

            HashMap<String, Integer> entitiesIdToPos = new HashMap<>();
            ArrayList<Boolean> predefiniedMeet = new ArrayList<>();
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
            ArrayList<SimpleClass> predefinedMeetings = new ArrayList<>();

            for(var x : MainData.SharedInstance().getSessions())
            {
                taulesXSessio.add(x.getListOfTables().get(0).nUnits);
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
                entitiesIdToPos.put(x.name, posEntity);

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

            int counter = 0;
            for(var meet : listOfScheduledMeetings)
            {
                if(meet.taula != -1) {
                    predefinedMeetings.add(new SimpleClass(counter, meet.sessio, meet.slot));
                    predefiniedMeet.add(true);
                }
                else
                    predefiniedMeet.add(false);
                counter++;
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
                        meetingEntities,
                        predefinedMeetings);  // meetingEntities: Array[Array[Int]]


                Platform.runLater(() -> {
                    for (int i = 0; i < result.size(); i++) {
                        var tab = listOfTabs.get(i);
                        var sessioArray = result.get(i);
                        for (int j = 0; j < sessioArray.size(); j++) {
                            for (var meetPos : sessioArray.get(j)) {
                                if(!predefiniedMeet.get((int) meetPos)) {
                                    var meeting = listOfScheduledMeetings.get((int) meetPos);
                                    moveMeeting(tab, meeting, i, j, true);
                                }
                            }
                        }
                    }
                });
            }
            catch(Exception e)
            {
                int x = 0;
            }
            return 1;
        });
    }

    private void moveMeeting(CustomTab currentTab, MeetingScheduled meet, int newSes, int newSlot, boolean add)
    {
        if(meet.sessio == -1)
            meetingsVBox.getChildren().remove(meet.stackPane);
        else {
            meet.parent.getChildren().remove(meet.stackPane);
            meet.parent = null;
        }

        if(add) {
            currentTab.addMeeting(meet, newSlot);
        }
        meet.hour = MainData.SharedInstance().getSessions().get(newSes).getSlots().get(newSlot);
        meet.sessio = newSes;
        meet.slot = newSlot-1;
    }

    private void moveMeeting(VBox list, MeetingScheduled meet, int newSes, int newSlot, boolean add)
    {
        if(meet.sessio == -1)
            meetingsVBox.getChildren().remove(meet.stackPane);
        else
            listOfTabs.get(meet.sessio).gridPane.getChildren().remove(meet.stackPane);

        if(add) {
            list.getChildren().add(meet.stackPane);
            meet.sessio = -1;
            meet.taula = -1;
            meet.hour = null;
        }
    }

    public void savePartialSolution()
    {

    }

    public void goBackAction()
    {
        try {
            if(AlertDialog.askQuestion(Alert.AlertType.CONFIRMATION, null, "Are you sure you want to go back? The current schedule won't be saved").get() == ButtonType.OK) {
                Stage stage = new Stage();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/create_new.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                stage.setTitle("B2B_Managment");
                CreateNewController controller = fxmlLoader.<CreateNewController>getController();
                controller.resetInformation();
                var scene = new Scene(root, 1300, 900);

                stage.setMinWidth(root.minWidth(-1));
                stage.setMinHeight(root.minHeight(-1));
                stage.setScene(scene);
                stage.show();

                ((Stage)tabPane.getScene().getWindow()).close();
            }
        }
        catch(Exception e)
        {
            int x = 0;
        }
    }
}
