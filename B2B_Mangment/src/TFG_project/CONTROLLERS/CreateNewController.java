package TFG_project.CONTROLLERS;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import TFG_project.HELPERS.Constants;
import com.google.gson.Gson;
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


    @FXML
    private TextField eventName;
    @FXML
    private TextField eventLocation;
    @FXML
    private TextField numberOfSessions;

    @FXML
    private Button setUpSessions;


    @FXML
    private TextField newEntityId;
    @FXML
    private TextField newEntityName;
    @FXML
    private TextField newEntityNumber;
    @FXML
    private ChoiceBox meetingDurationChoiceBox;

    @FXML
    private GridPane newMettingsGroup;

    private LinkedList<TextField> extraMeetings;
    private LinkedList<Label> extraLabels;

    private LinkedList<Integer> sessionsInfo;


    @FXML
    private TableView entityTable;

    private ObservableList<Entity> arrayEntity= FXCollections.observableArrayList();

    private Entity currentEntity;

    public CreateNewController()
    {
        //
        extraMeetings = new LinkedList<>();
        extraLabels = new LinkedList<>();
    }

    @FXML
    protected void initialize() {
        addNewMeeting();

        eventName.textProperty().addListener((observable, oldValue, newValue) -> {
            MainData.SharedInstance().setEventName(newValue);
        });

        eventLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            MainData.SharedInstance().setEventLocation(newValue);
        });

        numberOfSessions.textProperty().addListener((observable, oldValue, newValue) -> {

            try
            {
                MainData.SharedInstance().setNSessions(Integer.parseInt(newValue));
            }
            catch (Exception e)
            {
                int x = 0;
                //SHOW alert
            }
        });
        meetingDurationChoiceBox.setItems(Constants.MEETINGDURATIONARRAY);
        meetingDurationChoiceBox.setValue(Constants.MEETINGDURATIONARRAY.get(1));
        meetingDurationChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new
          ChangeListener<Number>() {
              @Override
              public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                  String newValueStr = Constants.MEETINGDURATIONARRAY.get(newValue.intValue());

                  //sessio.setHoraInici(newValueStr);
                  var arry = newValueStr.split(" ");
                  MainData.SharedInstance().setMeetingsDuration(Integer.parseInt(arry[0]));
              }
          });

        //Menu menu = new Menu("File");
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
                setUp = FXMLLoader.load(getClass().getResource("../FXML/set_up.fxml"));
                setUpSessions.setTitle("Set Up Sessions");
                int width = MainData.SharedInstance().getNSessions() * 110+120;
                int height = 345 + ((MainData.SharedInstance().getSessions().getFirst().getListOfTables().size() - 2)*40);
                setUpSessions.setScene(new Scene(setUp, Math.min(Math.max(width,350), 1700),height ));

                setUpSessions.show();
            } catch (IOException e) {
                int x = 0;
            }
        }
        else
        {
            //TODO Show alert
        }
    }

    public void loadFile() throws Exception
    {
        try{

            FileChooser fileChooser = new FileChooser();
            File file  = fileChooser.showOpenDialog(eventName.getScene().getWindow());

            if (file != null) {

                //This is where a real application would open the file.
                try {

                    Scanner myReader = new Scanner(file);
                    String data = "";
                    while (myReader.hasNextLine()) {
                        data += myReader.nextLine();
                    }
                    myReader.close();

                    Gson gson = new Gson();
                    MainData.SharedInstance().replaceInfo(gson.fromJson(data, MainData.class));
                    eventName.setText(MainData.SharedInstance().getEventName());
                    eventLocation.setText(MainData.SharedInstance().getEventLocation());
                    numberOfSessions.setText(String.valueOf(MainData.SharedInstance().getNSessions()));
                    meetingDurationChoiceBox.setValue(String.valueOf(MainData.SharedInstance().getMeetingsDuration()+ " minutes"));

                    arrayEntity.clear();
                    arrayEntity.addAll(MainData.SharedInstance().getConvertedEntities());
    
                    entityTable.setItems(arrayEntity);
                }
                catch(Exception e)
                {
                    //BAD FORMAT FILE
                }
            }

        }
        catch(Exception e)
        {
            int x = 0;
        }
    }

    public void saveFile() throws Exception
    {
        //Obteim les dades i les guardem en un fitxer


        try {
            DirectoryChooser fileChooser = new DirectoryChooser();
            File file  = fileChooser.showDialog(eventName.getScene().getWindow());

            if (file != null) {

                String dir = file.getAbsolutePath();
                //This is where a real application would open the file.

                MainData.SharedInstance().setEntities(arrayEntity);

                var path = Paths.get(dir, eventName.getText() + "_save" + ".json");
                FileWriter myWriter = new FileWriter(path.toString());

                Gson gson = new Gson();
                var str = gson.toJson(MainData.SharedInstance());
                myWriter.write(str);
                myWriter.close();
            }

        } catch (Exception e) {
            int x = 0;

        }
    }

    public void generateSchedule() throws Exception
    {
        //CREEM OBJECTE SCALA I GUARDEM TOT
    }

    public void addNewMeeting()
    {
        TextField newTF = new TextField("");
        newTF.setPromptText("Set Entity Id...");
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/set_arribal_info.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                SetArribalInfo controller = fxmlLoader.<SetArribalInfo>getController();
                controller.setEntity(currentEntity, MainData.SharedInstance().getSessions());


                int width = MainData.SharedInstance().getNSessions() * 110+120;
                int height = 260;
                Scene scene = new Scene(root, Math.min(Math.max(width,350), 1700),height );

                setUpAttendance.setScene(scene);

                setUpAttendance.show();

            } catch (IOException e) {
                int x = 0;
            }
        }
        else
        {
            //TODO Show alert
        }
    }

    public void addEntityToTable()
    {
        if(MainData.SharedInstance().getNSessions()> 0) {

            if(currentEntity == null)
                currentEntity= new Entity(MainData.SharedInstance().getSessions());
            else
                currentEntity.checkSessionsIntegrity(MainData.SharedInstance().getSessions());

            currentEntity.setId(newEntityId.getText());
            newEntityId.setText("");

            currentEntity.setName(newEntityName.getText());
            newEntityName.setText("");

            currentEntity.setAttendees(newEntityNumber.getText());
            newEntityNumber.setText("");

            //get

            for (int i = extraMeetings.size() - 1; i > 0; i--) {
                if(!extraMeetings.get(i).getText().isEmpty()) {
                    currentEntity.addMetting(extraMeetings.get(i).getText());
                }
                newMettingsGroup.getChildren().remove(extraMeetings.get(i));
                newMettingsGroup.getChildren().remove(extraLabels.get(i));
            }

            extraMeetings.subList(1, extraMeetings.size()).clear();
            extraLabels.subList(1, extraLabels.size()).clear();
            extraMeetings.get(0).setText("");

            arrayEntity.add(currentEntity);
            entityTable.setItems(arrayEntity);
            currentEntity = null;
        }
        else
        {
           // show Alert
        }
    }
}
