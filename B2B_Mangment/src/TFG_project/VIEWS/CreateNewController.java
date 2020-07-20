package TFG_project.VIEWS;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import TFG_project.Entities.Meeting;
import TFG_project.HELPERS.AlertDialog;
import TFG_project.HELPERS.Constants;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;


import java.io.IOException;
import java.util.Scanner;

public class CreateNewController extends JFrame {

    private boolean isReseting;

    //MAIN PART
    @FXML
    private TextField eventName;
    @FXML
    private TextField eventLocation;
    @FXML
    private TextField numberOfSessions;
    @FXML
    private ChoiceBox meetingDurationChoiceBox;
    @FXML
    private Button setUpSessions;

    //ENTITIES
    @FXML
    private TextField newEntityId;
    @FXML
    private TextField newEntityName;
    @FXML
    private TextField newEntityNumber;

    //MEETINGS
    @FXML
    private ComboBox preferedSessionChoiceBox;
    @FXML
    private GridPane newMettingsGroup;

    private LinkedList<TextField> extraMeetings;
    private LinkedList<Label> extraLabels;

    private LinkedList<Integer> sessionsInfo;

    public static ObservableList<String> ListOfSessions = FXCollections.observableArrayList("All");

    @FXML
    private TableView entityTable;

    @FXML
    private TableView meetingsTableView;

    private ObservableList<Entity> arrayEntity= FXCollections.observableArrayList();
    private ObservableList<Meeting> arrayMeetings= FXCollections.observableArrayList();

    private Entity currentEntity;
    private Meeting currentMeeting;

    private boolean hideAlerts = false;
    private boolean avoidChanges = false;

    public CreateNewController()
    {
        //
        extraMeetings = new LinkedList<>();
        extraLabels = new LinkedList<>();
    }

    @FXML
    protected void initialize() {

        addNewMeetingParticipant();
        addNewMeetingParticipant();

        meetingDurationChoiceBox.setItems(Constants.MEETINGDURATIONARRAY);
        preferedSessionChoiceBox.setItems(ListOfSessions);


        entityTable.setRowFactory(tv -> {
            TableRow<Entity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Entity clickedRow = row.getItem();

                    Stage entityDetail = new Stage();
                    entityDetail.initModality(Modality.APPLICATION_MODAL);
                    entityDetail.initOwner(eventName.getScene().getWindow());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("entity_detail.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        EntityDetailController controller = fxmlLoader.<EntityDetailController>getController();
                        controller.setEntity(clickedRow, arrayEntity);

                        int width = MainData.SharedInstance().getNSessions() * 150 + 50;
                        Scene scene = new Scene(root, Math.min(width, 1300), 750);
                        entityDetail.setMinWidth(root.minWidth(-1));
                        entityDetail.setMinHeight(root.minHeight(-1));
                        entityDetail.setScene(scene);

                        entityDetail.show();

                    } catch (IOException e) {
                        int x = 0;
                    }
                }
            });
            return row;
        });

        meetingsTableView.setRowFactory(tv -> {
            TableRow<Meeting> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Meeting clickedRow = row.getItem();

                    Stage meetingDetail = new Stage();
                    meetingDetail.initModality(Modality.APPLICATION_MODAL);
                    meetingDetail.initOwner(eventName.getScene().getWindow());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meeting_detail.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        MeetingDetailController controller = fxmlLoader.<MeetingDetailController>getController();
                        controller.setMeeting(clickedRow, arrayMeetings, ListOfSessions);


                        Scene scene = new Scene(root, 400, 450);
                        meetingDetail.setMinWidth(root.minWidth(-1));
                        meetingDetail.setMinHeight(root.minHeight(-1));
                        meetingDetail.setScene(scene);

                        meetingDetail.show();

                    } catch (IOException e) {
                        int x = 0;
                    }
                }
            });
            return row;
        });

        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            MainData.SharedInstance().setEventName(newValue);
        });

        eventLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            MainData.SharedInstance().setEventLocation(newValue);
        });



        numberOfSessions.textProperty().addListener((observable, oldValue, newValue) -> {

            try {
                if(!avoidChanges) {
                    int value = Integer.parseInt(newValue);
                    boolean cont = true;
                    if ((arrayEntity.size() > 0 || arrayMeetings.size() > 0) && !hideAlerts) {
                        cont = AlertDialog.askQuestion(Alert.AlertType.WARNING, null, "Updating the number of sessions will alter the attending sessions of the current entities").get() == ButtonType.YES;
                    }
                    if (cont) {
                        var previous = MainData.SharedInstance().getNSessions();
                        if (previous != value) {
                            MainData.SharedInstance().setNSessions(value);

                            int last = preferedSessionChoiceBox.getSelectionModel().getSelectedIndex();

                            ListOfSessions.subList(1, ListOfSessions.size()).clear();
                            for (int i = 0; i < Integer.parseInt(newValue); i++) {
                                ListOfSessions.add("Session " + String.valueOf(i + 1));
                            }
                            preferedSessionChoiceBox.setItems(ListOfSessions);
                            preferedSessionChoiceBox.setValue(ListOfSessions.get(last >= ListOfSessions.size() ? 0 : last));

                            arrayEntity.forEach(e -> e.updateNSessions(value));
                            arrayMeetings.forEach(m -> m.resetSessio(value));
                        }
                    } else {
                        avoidChanges = true;
                        numberOfSessions.setText(String.valueOf(MainData.SharedInstance().getNSessions()));
                    }
                }
                else
                {
                    avoidChanges = false;
                }
            } catch (Exception e) {
                int x = 0;
                //SHOW alert
            }
        });

        meetingDurationChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new
             ChangeListener<Number>() {
                 @Override
                 public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                     String newValueStr = Constants.MEETINGDURATIONARRAY.get(newValue.intValue());

                     //sessio.setHoraInici(newValueStr);
                     var arry = newValueStr.split(" ");

                     var previous = MainData.SharedInstance().getMeetingsDuration();
                     var nextValue = Integer.parseInt(arry[0]);
                     if(previous != nextValue)
                     {
                         boolean cont = true;
                         if((!arrayEntity.isEmpty() || !arrayMeetings.isEmpty()) && !hideAlerts)
                             cont = AlertDialog.askQuestion(Alert.AlertType.WARNING, "", "This will reset all attending information and any pre established meeting, do you want to continue?").get() == ButtonType.YES;
                         if(cont)
                         {
                             MainData.SharedInstance().setMeetingsDuration(nextValue);
                             MainData.SharedInstance().getSessions().forEach(ses -> ses.resetSlots(nextValue));
                             arrayEntity.forEach(e -> e.resetAllSessions());
                             arrayMeetings.forEach(m -> m.resetTable(-1));
                         }
                         else
                         {
                             meetingDurationChoiceBox.setValue(String.valueOf(previous) + " minutes");
                         }
                     }
                 }
             });

        preferedSessionChoiceBox.setValue(ListOfSessions.get(0));

        Platform.runLater(() -> {
            if (isReseting) {
                refillInformation();
                isReseting = false;
            } else {
                meetingDurationChoiceBox.setValue(Constants.MEETINGDURATIONARRAY.get(1));

            }
        });

    }

    public void openSetUpSessions()
    {
        if(MainData.SharedInstance().getNSessions() >0)
        {
            Stage setUpSessions = new Stage();
            setUpSessions.initModality(Modality.APPLICATION_MODAL);
            setUpSessions.initOwner(eventName.getScene().getWindow());
            Parent setUp = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("set_up.fxml"));
                setUp = (Parent) fxmlLoader.load();
                SetUpController controller = fxmlLoader.<SetUpController>getController();
                controller.setEntites(arrayEntity, arrayMeetings);

                int width = MainData.SharedInstance().getNSessions() * 110+120;
                int height = 345 + ((MainData.SharedInstance().getSessions().getFirst().getListOfTables().size() - 2)*40);
                setUpSessions.setScene(new Scene(setUp, Math.min(Math.max(width,350), 1400),height ));
                setUpSessions.setMinWidth(setUp.minWidth(-1));
                setUpSessions.setMinHeight(setUp.minHeight(-1));

                setUpSessions.show();
            } catch (IOException e) {
                int x = 0;
            }
        }
        else
        {
            AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You must enter the number of sessions before editing them");
        }
    }

    ///ONLY FOR DEBUG PURPOSE
    public void loadInstance()
    {
        try{

            FileChooser fileChooser = new FileChooser();
            File file  = fileChooser.showOpenDialog(eventName.getScene().getWindow());

            if (file != null) {
                try {

                    Scanner myReader = new Scanner(file);
                    String data = "";
                    MainData newMain = new MainData();

                    arrayEntity.clear();
                    arrayMeetings.clear();

                    //PRIMER APARTAT
                    data = myReader.nextLine();
                    var split = data.split(" = ");
                    var nEnitities = Integer.parseInt(split[1].substring(0,split[1].length()-1));

                    data = myReader.nextLine();
                    split = data.split(" = ");
                    var nMeetings = Integer.parseInt(split[1].substring(0,split[1].length()-1));

                    data = myReader.nextLine();
                    split = data.split(" = ");
                    var nTables = Integer.parseInt(split[1].substring(0,split[1].length()-1));

                    data = myReader.nextLine();
                    split = data.split(" = ");
                    var nTotalSlots = Integer.parseInt(split[1].substring(0,split[1].length()-1));

                    data = myReader.nextLine();
                    split = data.split(" = ");
                    var nSlotsFirst = Integer.parseInt(split[1].substring(0,split[1].length()-1));

                    //SEGON APARTAT
                    newMain.setMeetingsDuration(30);
                    if(nSlotsFirst == nTotalSlots) {
                        newMain.setNSessions(1);
                        newMain.getSessions().get(0).setDebugInfo(nTotalSlots, 30, nTables);
                    }
                    else
                    {
                        newMain.setNSessions(2);
                        newMain.getSessions().get(0).setDebugInfo(nSlotsFirst, 30, nTables);
                        newMain.getSessions().get(1).setDebugInfo(nTotalSlots - nSlotsFirst, 30, nTables);
                    }

                    myReader.nextLine();
                    data = myReader.nextLine();
                    data = data.substring(13);
                    while(!data.equals("|];"))
                    {
                        data =data.substring(1);
                        var list = data.split(", ");
                        Meeting met = new Meeting();
                        met.addMetting("Entity " +(list[0]));
                        met.addMetting("Entity " +(list[1]));
                        list[2] = list[2].substring(0,1);
                        met.setSessio(list[2].equals("3") ? "All" : ("Session " + list[2]));
                        arrayMeetings.add(met);
                        data = myReader.nextLine();
                    }
                    //N Meetings per entitat (innecessari)
                    data = myReader.nextLine();
                    while(data.equals("")) {
                        data = myReader.nextLine();
                    }

                    while(!data.equals("")) {
                        data = myReader.nextLine();
                    }

                    while(data.equals("")) {
                        data = myReader.nextLine();
                    }

                    while(!data.equals("")) {
                        data = myReader.nextLine();
                    }

                    while(data.equals("")) {
                        data = myReader.nextLine();
                    }

                    data = data.substring(13);
                    int entityCounter = 1;
                    while(!data.equals("];"))
                    {
                        Entity ent = new Entity(newMain.getSessions());
                        ent.setName("Entity " + entityCounter);
                        ent.setId(String.valueOf(entityCounter));
                        ent.setAttendees("1");

                        if(!data.equals("{0,},")) {
                            data = data.substring(1);
                            var list = data.split(",");
                            int i = 1;
                            while (!list[i].contains("}")) {
                                ent.cantAttend(Integer.valueOf(list[i])-1);
                                i++;
                            }
                        }

                        entityCounter++;
                        arrayEntity.add(ent);
                        data = myReader.nextLine();
                    }

                    myReader.close();

                    MainData.SharedInstance().replaceInfo(newMain);
                    restartFromDebug();
                }
                catch(Exception e)
                {
                    AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The selected file doesn't have the correct format");
                }
            }

        }
        catch(Exception e)
        {
            int x = 0;
        }
    }

    public void loadFile()
    {
        try{

            FileChooser fileChooser = new FileChooser();
            File file  = fileChooser.showOpenDialog(eventName.getScene().getWindow());

            if (file != null) {
                try {

                    Scanner myReader = new Scanner(file);
                    String data = "";
                    while (myReader.hasNextLine()) {
                        data += myReader.nextLine();
                    }
                    myReader.close();

                    Gson gson = new Gson();
                    MainData.SharedInstance().replaceInfo(gson.fromJson(data, MainData.class));
                    refillInformation();
                }
                catch(Exception e)
                {
                    AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The selected file doesn't have the correct format");
                }
            }

        }
        catch(Exception e)
        {
            int x = 0;
        }
    }

    private void refillInformation()
    {

        hideAlerts = true;
        arrayEntity.clear();
        arrayMeetings.clear();
        eventName.setText(MainData.SharedInstance().getEventName());
        eventLocation.setText(MainData.SharedInstance().getEventLocation());
        numberOfSessions.setText(String.valueOf(MainData.SharedInstance().getNSessions()));
        meetingDurationChoiceBox.setValue(String.valueOf(MainData.SharedInstance().getMeetingsDuration()+ " minutes"));

        arrayEntity.addAll(MainData.SharedInstance().getConvertedEntities());

        arrayMeetings.addAll(MainData.SharedInstance().getConvertedMeetings());

        entityTable.setItems(arrayEntity);
        meetingsTableView.setItems(arrayMeetings);
        hideAlerts = false;

        ListOfSessions.subList(1, ListOfSessions.size()).clear();
        for (int i = 0; i < MainData.SharedInstance().getNSessions(); i++) {
            ListOfSessions.add("Session " + String.valueOf(i + 1));
        }
    }

    private void restartFromDebug()
    {
        hideAlerts = true;

        eventName.setText(MainData.SharedInstance().getEventName());
        eventLocation.setText(MainData.SharedInstance().getEventLocation());
        numberOfSessions.setText(String.valueOf(MainData.SharedInstance().getNSessions()));
        meetingDurationChoiceBox.setValue(String.valueOf(MainData.SharedInstance().getMeetingsDuration()+ " minutes"));

        arrayEntity.addAll(MainData.SharedInstance().getConvertedEntities());
        arrayMeetings.addAll(MainData.SharedInstance().getConvertedMeetings());
        entityTable.setItems(arrayEntity);
        meetingsTableView.setItems(arrayMeetings);
        hideAlerts = false;

        ListOfSessions.subList(1, ListOfSessions.size()).clear();
        for (int i = 0; i < MainData.SharedInstance().getNSessions(); i++) {
            ListOfSessions.add("Session " + String.valueOf(i + 1));
        }
    }

    public void resetInformation()
    {
        isReseting = true;
    }

    public void saveFile()
    {
        //Obteim les dades i les guardem en un fitxer
        try {
            DirectoryChooser fileChooser = new DirectoryChooser();
            File file  = fileChooser.showDialog(eventName.getScene().getWindow());

            if (file != null) {

                String dir = file.getAbsolutePath();
                //This is where a real application would open the file.

                MainData.SharedInstance().setEntities(arrayEntity);
                MainData.SharedInstance().setMeetingJson(arrayMeetings);

                var path = Paths.get(dir, eventName.getText() + "_save" + ".json");
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

    public void accessSchedule()
    {

        if(arrayMeetings.size() > 0 && arrayEntity.size() > 1) {
            try {

                for(var meet : arrayMeetings)
                {
                    for(var ent : meet.getListOfParticipants())
                    {
                        boolean found = false;
                        for(var entity : arrayEntity)
                        {
                            if(entity.getName().equals(ent))
                            {
                                found = true;
                                break;
                            }
                        }
                        if(!found)
                            throw new Exception("NotFound");
                    }
                }

                var result = AlertDialog.askSave(Alert.AlertType.CONFIRMATION, null, "Do you want to save changes before moving forward?").get();
                if (result == ButtonType.YES) {
                    saveFile();
                }

                if(result == ButtonType.YES || result == ButtonType.NO) {
                    Stage scheduleStage = new Stage();

                    MainData.SharedInstance().setEntities(arrayEntity);
                    MainData.SharedInstance().setMeetingJson(arrayMeetings);

                    Parent schedule = null;
                    try {
                        schedule = FXMLLoader.load(getClass().getResource("schedule.fxml"));
                        scheduleStage.setTitle("Schedule");
                        int width = MainData.SharedInstance().GetMaxNTables() * 170 + 270 + 87;
                        int height = MainData.SharedInstance().GetMaxSlots() * 40 + 90 + 85;
                        scheduleStage.setScene(new Scene(schedule, width, height));

                        scheduleStage.show();
                        ((Stage) eventName.getScene().getWindow()).close();
                    } catch (IOException e) {
                        int x = 0;
                    }
                }
            }
            catch (Exception e)
            {
                if(e.getMessage().equals("NotFound"))
                    AlertDialog.showMessage(Alert.AlertType.WARNING, null, "At least one participant of one meeting isn't part of the entities list, make sure all the entities are loaded");
            }
        }
        else
            AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You need at least one meeting and two entites to move forward");

    }

    public void addNewMeetingParticipant()
    {
        TextField newTF = new TextField("");
        newTF.setPromptText("Set Entity Name...");
        Label newLabel = new Label(String.valueOf(newMettingsGroup.getRowCount()+1) + ".");
        extraMeetings.add(newTF);
        extraLabels.add(newLabel);
        int rowCount = newMettingsGroup.getRowCount();
        newMettingsGroup.add(newLabel, 0, rowCount);
        newMettingsGroup.add(newTF, 1, rowCount);
    }

    public void setArribalInfo()
    {
        if(MainData.SharedInstance().getNSessions() >0)
        {
            if(currentEntity == null)
                currentEntity= new Entity(MainData.SharedInstance().getSessions());
            else
                currentEntity.checkSessionsIntegrity(MainData.SharedInstance().getSessions());

            Stage setUpAttendance = new Stage();
            setUpAttendance.initModality(Modality.APPLICATION_MODAL);
            setUpAttendance.initOwner(eventName.getScene().getWindow());

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("set_arribal_info.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                SetArribalInfo controller = fxmlLoader.<SetArribalInfo>getController();
                controller.setEntity(currentEntity, MainData.SharedInstance().getSessions());


                int width = MainData.SharedInstance().getNSessions() * 150 + 20;
                Scene scene = new Scene(root, Math.min(width,1700), 900);
                setUpAttendance.setMinWidth(root.minWidth(-1));
                setUpAttendance.setMinHeight(root.minHeight(-1));
                setUpAttendance.setScene(scene);

                setUpAttendance.show();

            } catch (IOException e) {
                int x = 0;
            }
        }
        else
            AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You must enter the number of sessions before editing the entity schedule info");
    }

    public void addEntityToTable()
    {
        if(MainData.SharedInstance().getNSessions()> 0) {
            if(!newEntityId.getText().equals("") && !newEntityName.getText().equals("")) {
                try{

                    Integer.parseInt(newEntityNumber.getText());
                    var id = newEntityId.getText();
                    var name = newEntityName.getText();
                    for(var e : arrayEntity)
                    {
                        if(e.getName().equals(name) || e.getId().equals(id))
                            throw new Exception("Repeated");
                    };

                    if (currentEntity == null)
                        currentEntity = new Entity(MainData.SharedInstance().getSessions());
                    else
                        currentEntity.checkSessionsIntegrity(MainData.SharedInstance().getSessions());

                    currentEntity.setId(newEntityId.getText());
                    newEntityId.setText("");

                    currentEntity.setName(newEntityName.getText());
                    newEntityName.setText("");

                    currentEntity.setAttendees(newEntityNumber.getText());
                    newEntityNumber.setText("");

                    arrayEntity.add(currentEntity);
                    entityTable.setItems(arrayEntity);
                    currentEntity = null;
                }
                catch(Exception e)
                {
                    if(e.getMessage().equals("Repeated"))
                        AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The entity is already on the list");
                    else
                        AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The field number of attendees must be a number");
                }
            }
            else
                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You must enter valid entity data");
        }
        else
            AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You must enter the number of sessions before editing them");
    }

    public void addMeetingToTable()
    {
        try {
            int correct = 0;
            int pos = 0;
            while (correct < 2 && pos < extraMeetings.size()) {
                if (!extraMeetings.get(pos).getText().isEmpty())
                    correct++;
                pos++;
            }
            if (correct >= 2) {
                if (currentMeeting == null)
                    currentMeeting = new Meeting();

                currentMeeting.setSessio((String) preferedSessionChoiceBox.getValue());
                preferedSessionChoiceBox.setValue(ListOfSessions.get(0));

                //get

                for (int i = 0; i < extraMeetings.size(); i++) {
                    if (!extraMeetings.get(i).getText().isEmpty()) {
                        var text = extraMeetings.get(i).getText();
                        currentMeeting.addMetting(text);

                    }
                }

                int index = 0;
                int totalCorrectes = 0;
                while(index < arrayEntity.size() && totalCorrectes<currentMeeting.getListOfParticipants().size())
                {
                    int subIndex = 0;
                    boolean found = false;
                    while (subIndex < currentMeeting.getListOfParticipants().size() && !found)
                    {
                        if(arrayEntity.get(index).getName().contains(currentMeeting.getListOfParticipants().get(subIndex)))
                        {
                            found = true;
                            totalCorrectes++;
                        }
                        subIndex++;
                    }
                    index++;
                }

                if(totalCorrectes < currentMeeting.getListOfParticipants().size())
                {
                    if(AlertDialog.askQuestion(Alert.AlertType.WARNING, null, "You have entered a participant that is not present in the entities list, do you want to continue?").get() == ButtonType.CANCEL)
                    {
                        throw new Exception("Cancel");
                    }
                }

                newMettingsGroup.getChildren().removeAll(extraLabels.subList(2, extraLabels.size()));
                newMettingsGroup.getChildren().removeAll(extraMeetings.subList(2, extraMeetings.size()));

                extraMeetings.subList(2, extraMeetings.size()).clear();
                extraLabels.subList(2, extraLabels.size()).clear();
                extraMeetings.get(0).setText("");
                extraMeetings.get(1).setText("");

                arrayMeetings.add(currentMeeting);
                meetingsTableView.setItems(arrayMeetings);
                currentMeeting = null;
            } else
                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You need at least two entities to create a meeting");
        }
        catch (Exception e)
        {
            currentMeeting.getListOfParticipants().clear();
            if(e.getMessage().equals("Repeated"))
                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You entered the same participant twice");

        }
    }


}
