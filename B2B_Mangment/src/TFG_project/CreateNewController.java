package TFG_project;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class CreateNewController {


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
                MainData.SharedInstance().setNumberOfSessions(Integer.parseInt(newValue));
            }
            catch (Exception e)
            {
                //SHOW alert
            }
        });
    }

    public void openSetUpSessions()
    {
        if(MainData.SharedInstance().getNSessions() >0)
        {

        }
        else
        {
            //TODO Show alert
        }
    }


    public void saveFile() throws Exception
    {
        //Obteim les dades i les guardem en un fitxer
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
            Entity ent = new Entity(MainData.SharedInstance().getNSessions());

            ent.setId(newEntityId.getText());
            newEntityId.setText("");

            ent.setName(newEntityName.getText());
            newEntityName.setText("");

            ent.setAttendees(newEntityNumber.getText());
            newEntityNumber.setText("");

            //get

            for (int i = extraMeetings.size() - 1; i > 0; i--) {
                ent.addMetting(extraMeetings.get(i).getText());
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
