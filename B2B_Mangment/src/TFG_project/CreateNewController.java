package TFG_project;

import TFG_project.Entities.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class CreateNewController {

    @FXML
    private TextField nTables;
    @FXML
    private TextField nDays;
    @FXML
    private ChoiceBox hourIniciPicker;
    @FXML
    private ChoiceBox hourFiPicker;
    @FXML
    private TextField numberOfEntities;
    @FXML
    private Button generateButton;

    @FXML
    private Button saveButton;
    @FXML
    private Button endButton;

    @FXML
    private TableView entityTable;


    @FXML
    private TextField newEntityId;
    @FXML
    private TextField newEntityName;
    @FXML
    private TextField newEntityNumber;
    @FXML
    private ChoiceBox newEntityEntrance;
    @FXML
    private TextField newEntityMeeting1;
    @FXML
    private HBox newMettingsGroup;

    private LinkedList<TextField> extraMeetings;


    private ObservableList<Entity> arrayEntity= FXCollections.observableArrayList();

    public CreateNewController()
    {
        //
        extraMeetings = new LinkedList<>();
    }

    public void generateTable() throws Exception
    {
        //Bloquejem el nombre d'entitats i generem el llistat
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
        newTF.setPromptText("Meeting Entity Id...");
        extraMeetings.add(newTF);
        newMettingsGroup.getChildren().add(extraMeetings.size(),newTF);
    }

    public void addEntityToTable()
    {
        Entity ent = new Entity();

        ent.setId(newEntityId.getText());
        newEntityId.setText("");

        ent.setName(newEntityName.getText());
        newEntityName.setText("");

        ent.setAttendees(newEntityNumber.getText());
        newEntityNumber.setText("");

        ent.setEntrance((String) newEntityEntrance.getValue());
        newEntityEntrance.setValue("00:00");

        ent.addMetting(newEntityMeeting1.getText());
        newEntityMeeting1.setText("");

        for (TextField ttt : extraMeetings) {
            ent.addMetting(ttt.getText());
            newMettingsGroup.getChildren().remove(ttt);
        }

        extraMeetings.clear();

        arrayEntity.add(ent);
        entityTable.setItems(arrayEntity);
    }
}
