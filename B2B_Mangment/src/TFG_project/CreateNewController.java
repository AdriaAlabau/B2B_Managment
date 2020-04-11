package TFG_project;

import TFG_project.Entities.Entity;
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
    private Button addMeetingButton;

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
    private GridPane newMettingsGroup;

    private LinkedList<TextField> extraMeetings;
    private LinkedList<Label> extraLabels;


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
        /*Entity ent = new Entity();

        ent.setId(newEntityId.getText());
        newEntityId.setText("");

        ent.setName(newEntityName.getText());
        newEntityName.setText("");

        ent.setAttendees(newEntityNumber.getText());
        newEntityNumber.setText("");

        ent.setEntrance((String) newEntityEntrance.getValue());
        newEntityEntrance.setValue("00:00");*/

        for (int i = extraMeetings.size()-1; i>0; i--){
            //ent.addMetting(ttt.getText());
            newMettingsGroup.getChildren().remove(extraMeetings.get(i));
            newMettingsGroup.getChildren().remove(extraLabels.get(i));
        }

        extraMeetings.subList(1,extraMeetings.size()).clear();
        extraLabels.subList(1,extraLabels.size()).clear();

       // arrayEntity.add(ent);
        //entityTable.setItems(arrayEntity);
    }
}
