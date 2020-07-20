package TFG_project.VIEWS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.AlertDialog;
import TFG_project.HELPERS.SimpleClass;
import TFG_project.HELPERS.WorkIndicatorDialog;
import TFG_project.NewMain;
import TFG_project.SCALA.Encoding;
import com.google.gson.Gson;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ScheduleController {

    private static final DataFormat stackPaneFormat = new DataFormat("STACKPANE");

    private static final String bruh = "WILDACARD";

    private MeetingScheduled draggingMeeting;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchButton;

    private ArrayList<MeetingScheduled> listOfSearched;

    private HashMap<String, EntityJson> entityDictionary;

    private boolean hasSearched;


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

    private class MeetingKeyPair
    {
        public MeetingScheduled meet = null;
        public StackPane parent = null;
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
        private HashMap<Integer, ArrayList<MeetingKeyPair>> meetingsInSlots;

        public CustomTab(Sessio ses, int i)
        {
            sessioIndex = i-1;
            sessio = ses;
            tab = new Tab("Session " + i);

            meetingsInSlots = new HashMap<>();

            scrollPane = new ScrollPane();
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
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
                        var keyValue = new MeetingKeyPair();
                        keyValue.parent = pane;
                        meetingsInSlots.get(wild).add(keyValue);
                    }
                }
            }

            scrollPane.setContent(gridPane);
        }

        public void addMeeting(MeetingScheduled meet, int slot)
        {
            int i = 0;
            boolean cont = false;

            var list = meetingsInSlots.get(slot);
            for(var entrance : list)
            {
                if(entrance.meet == null)
                {
                    meet.taula = i;
                    entrance.parent.getChildren().add(meet.stackPane);
                    meet.parent = entrance.parent;
                    meet.slot = slot;
                    meet.sessio = sessioIndex;
                    meet.stackPane.setVisible(true);
                    entrance.meet = meet;
                    break;
                }
                i++;
            }
        }

        public boolean addAtTable(CustomTab tab, MeetingScheduled meeting, int taula1, int slot1, boolean showAlerts)
        {
            try {
                var slotLits = meetingsInSlots.get(slot1 - 1);
                var position = slotLits.get(taula1 - 1);

                if (position.meet == null) {
                    for (var e : meeting.meeting.listOfParticipants) {
                        var entity = entityDictionary.get(e);
                        if (!entity.canAttend(tabPane.getSelectionModel().getSelectedIndex(), slot1 - 1)) {
                            if (showAlerts)
                                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The entity " + e + " won't be avalible at that time");
                            return false;
                        }
                        int totalCount = 0;
                        for (var table : slotLits) {
                            if (table.meet != null && table.meet != meeting && table.meet.meeting.listOfParticipants.contains(e))
                                totalCount++;
                        }
                        if (totalCount >= Integer.parseInt(entity.attendees)) {
                            if (showAlerts)
                                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The entity " + e + " won't be able to attend that meeting, all his attendees are already in a meeting");
                            return false;
                        }
                    }


                    moveMeeting(tab, meeting, tabPane.getSelectionModel().getSelectedIndex(), slot1 - 1, false);

                    position.parent.getChildren().add(meeting.stackPane);
                    position.meet = meeting;
                    meeting.parent = position.parent;
                    meeting.taula = taula1 - 1;
                    meeting.stackPane.setVisible(true);

                    return true;
                }
            }
            catch(Exception e)
            {
                int x = 0;
            }
            return false;
        }

        public void removeFromTable(int slot, int table)
        {
            meetingsInSlots.get(slot).get(table).meet = null;
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

            listOfTabs = new ArrayList<>();
            entityDictionary = new HashMap<>();
            MainData.SharedInstance().getEntities().forEach(e -> entityDictionary.put(e.name,e));

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
                                int taula1 = cIndex == null ? 0 : cIndex;
                                int slot1 = rIndex == null ? 0 : rIndex;

                                success = tab.addAtTable(tab, draggingMeeting, taula1, slot1, true);

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
                meetingScheduled.stackPane.setStyle("-fx-background-color: darkGrey; -fx-border-color: black;" );
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
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meeting_scheduled_detail.fxml"));
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

                String meetingName = meet.listOfParticipants.get(0) + " - " + meet.listOfParticipants.get(1) + ( meet.listOfParticipants.size() > 2 ?  " - ..." : "");
                Label label = new Label(meetingName);

                meetingScheduled.stackPane.getChildren().add(label);
                meetingScheduled.stackPane.setPadding(new Insets(5,5,5,5));
                //GridPane.setMargin(meetingScheduled.stackPane, new Insets(5,5,5,5));
                meetingScheduled.stackPane.managedProperty().bind(meetingScheduled.stackPane.visibleProperty());

                meetingScheduled.stackPane.setAlignment(Pos.CENTER);
                meetingScheduled.id = j;
                meetingScheduled.meeting = meet;

                if(meet.nSessio == -1)
                    meetingsVBox.getChildren().add(meetingScheduled.stackPane);
                else
                {
                    if(!listOfTabs.get(meet.nSessio).addAtTable(null, meetingScheduled, meet.nTaula+1, meet.nSlot+1,false))
                    {
                        meetingsVBox.getChildren().add(meetingScheduled.stackPane);
                    }
                }

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

            listOfSearched = new ArrayList<>();

            searchButton.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {

                    if(!hasSearched)
                    {
                        String entityName = searchField.getText();

                        listOfScheduledMeetings.forEach(meet -> {
                            if(meet.meeting.containsEntity(entityName))
                            {
                                meet.stackPane.setStyle("-fx-background-color: dodgerBlue; -fx-border-color: black;" );
                                listOfSearched.add(meet);
                            }
                        });

                        hasSearched = listOfSearched.size() > 0;
                    }
                }
            });

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if(hasSearched)
                {
                    listOfSearched.forEach(e->
                    {
                        e.stackPane.setStyle("-fx-background-color: darkGrey;  -fx-border-color: black;" );
                    });
                    listOfSearched.clear();
                    hasSearched = false;
                }
            });

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

            tabPane.getSelectionModel().selectedIndexProperty().addListener( (observable, oldValue, newValue) -> {
                int selectedIndex = newValue.intValue();
                //where index of the first tab is 0, while that of the second tab is 1 and so on.
                listOfScheduledMeetings.forEach(meet -> {
                    if(meet.taula == -1) {
                        if (meet.meeting.sessio.equals("All"))
                            meet.stackPane.setVisible(true);
                        else
                            meet.stackPane.setVisible(meet.meeting.sessio.contains(String.valueOf(selectedIndex +1)));
                    }
                });
            });

            listOfScheduledMeetings.forEach(meet -> {
                if(meet.taula == -1) {
                    if (meet.meeting.sessio.equals("All"))
                        meet.stackPane.setVisible(true);
                    else
                        meet.stackPane.setVisible(meet.meeting.sessio.contains(String.valueOf( 1)));

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

                var jarDir = new File(NewMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
                var path = jarDir.substring(0, jarDir.lastIndexOf(File.separator)) + File.separator;

                System.out.println(jarDir);

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
                        predefinedMeetings,
                        path);  // meetingEntities: Array[Array[Int]]

                if(result.isEmpty() && !meetingSessions.isEmpty())
                {
                    Platform.runLater(() -> {
                        AlertDialog.showMessage(Alert.AlertType.WARNING, null, "There is no solution to the corresponding input data");
                    });
                }
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
                Platform.runLater(() -> {
                    AlertDialog.showMessage(Alert.AlertType.WARNING, null, "Something went wrong, make sure the solver is in the correct path");
                });
            }
            return 1;
        });
    }

    private void moveMeeting(CustomTab currentTab, MeetingScheduled meet, int newSes, int newSlot, boolean add)
    {
        if(meet.sessio == -1)
            meetingsVBox.getChildren().remove(meet.stackPane);
        else {
            if(currentTab != null)
                currentTab.removeFromTable(meet.slot, meet.taula);
            if(meet.parent != null)
                meet.parent.getChildren().remove(meet.stackPane);
            meet.parent = null;
        }

        if(add && currentTab!=null) {
            currentTab.addMeeting(meet, newSlot);
        }
        meet.hour = MainData.SharedInstance().getSessions().get(newSes).getSlots().get(newSlot);
        meet.sessio = newSes;
        meet.slot = newSlot;
    }

    private void moveMeeting(VBox list, MeetingScheduled meet, int newSes, int newSlot, boolean add)
    {
        if(meet.sessio == -1)
            meetingsVBox.getChildren().remove(meet.stackPane);
        else {
            listOfTabs.get(tabPane.getSelectionModel().getSelectedIndex()).removeFromTable(meet.slot, meet.taula);
        }

        if(add) {
            list.getChildren().add(meet.stackPane);
            meet.sessio = -1;
            meet.taula = -1;
            meet.hour = null;
        }
    }

    public void savePartialSolution()
    {
        //Obteim les dades i les guardem en un fitxer
        try {
            DirectoryChooser fileChooser = new DirectoryChooser();
            File file  = fileChooser.showDialog(meetingsVBox.getScene().getWindow());

            if (file != null) {

                String dir = file.getAbsolutePath();
                //This is where a real application would open the file.

                for(int i = 0; i< listOfScheduledMeetings.size(); i++)
                {
                    var toSave = listOfScheduledMeetings.get(i);
                    var fromMain = MainData.SharedInstance().getMeetings().get(toSave.id);
                    fromMain.nSessio = toSave.sessio;
                    fromMain.nTaula = toSave.taula;
                    fromMain.nSlot = toSave.slot;
                }

                var path = Paths.get(dir, MainData.SharedInstance().getEventName() + "_save" + ".json");
                FileWriter myWriter = new FileWriter(path.toString());

                Gson gson = new Gson();
                var str = gson.toJson(MainData.SharedInstance());
                myWriter.write(str);
                myWriter.close();
            }

        } catch (Exception e) {
            AlertDialog.showMessage(Alert.AlertType.ERROR, null, "Error trying to save the file");

        }
    }

    public void goBackAction()
    {
        try {
            var result = AlertDialog.askSave(Alert.AlertType.CONFIRMATION, null, "Do you want to save the current schedule?").get();
            if(result == ButtonType.YES)
            {
                for(int i = 0; i< listOfScheduledMeetings.size(); i++)
                {
                    var toSave = listOfScheduledMeetings.get(i);
                    var fromMain = MainData.SharedInstance().getMeetings().get(toSave.id);
                    fromMain.nSessio = toSave.sessio;
                    fromMain.nTaula = toSave.taula;
                    fromMain.nSlot = toSave.slot;
                }
            }
            if(result == ButtonType.YES || result == ButtonType.NO)
            {
                Stage stage = new Stage();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create_new.fxml"));
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