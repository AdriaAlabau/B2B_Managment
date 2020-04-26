package TFG_project;

import TFG_project.Entities.Entity;
import TFG_project.Entities.EntityJson;
import TFG_project.Entities.MainData;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;


import java.io.IOException;
import java.util.Scanner;
import java.util.jar.JarFile;
import javax.swing.JFileChooser;

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
    private GridPane newMettingsGroup;

    private LinkedList<TextField> extraMeetings;
    private LinkedList<Label> extraLabels;

    private LinkedList<Integer> sessionsInfo;


    @FXML
    private TableView entityTable;

    private ObservableList<Entity> arrayEntity= FXCollections.observableArrayList();

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
                //SHOW alert
            }
        });

        Menu menu = new Menu("File");



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
                setUp = FXMLLoader.load(getClass().getResource("set_up.fxml"));
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

    public void addEntityToTable()
    {
        if(MainData.SharedInstance().getNSessions()> 0) {
            Entity ent = new Entity(MainData.SharedInstance().getSessions());

            ent.setId(newEntityId.getText());
            newEntityId.setText("");

            ent.setName(newEntityName.getText());
            newEntityName.setText("");

            ent.setAttendees(newEntityNumber.getText());
            newEntityNumber.setText("");

            //get

            for (int i = extraMeetings.size() - 1; i > 0; i--) {
                if(!extraMeetings.get(i).getText().isEmpty()) {
                    ent.addMetting(extraMeetings.get(i).getText());
                }
                newMettingsGroup.getChildren().remove(extraMeetings.get(i));
                newMettingsGroup.getChildren().remove(extraLabels.get(i));
            }

            extraMeetings.subList(1, extraMeetings.size()).clear();
            extraLabels.subList(1, extraLabels.size()).clear();
            extraMeetings.get(0).setText("");

            arrayEntity.add(ent);
            entityTable.setItems(arrayEntity);

        }
        else
        {
           // show Alert
        }
    }
}
