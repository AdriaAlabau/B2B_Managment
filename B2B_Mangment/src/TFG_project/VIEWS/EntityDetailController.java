package TFG_project.VIEWS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.AlertDialog;
import TFG_project.HELPERS.Pair;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.LinkedList;

public class EntityDetailController {

    @FXML
    private HBox mediumHBOX;

    class SessionColumn
    {
        public VBox mainVBox;

        private CheckBox attendingPicker;
        private Label title;

        private GridPane gridPane;
        private LinkedList<CheckBox> hours;

        private SessioAttending sessio;

        private boolean didChange = false;

        public SessionColumn(int i, SessioAttending entitySes)
        {

            sessio = entitySes;
            mainVBox = new VBox();

            //HBOX amb label i checkbox (main atending)
            Label title = new Label("Session " + i);
            title.setStyle("-fx-font-style: title2");

            attendingPicker = new CheckBox();
            attendingPicker.setSelected(sessio.getAttending());
            attendingPicker.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    hours.forEach(ch -> ch.setDisable(!newValue));
                    didChange = true;
                }
            });

            //set gridPane de 2xN amb temps i check bindejat
            gridPane = new GridPane();
            gridPane.setGridLinesVisible(true);
            ColumnConstraints column1 = new ColumnConstraints(80);
            column1.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column1);
            ColumnConstraints column2 = new ColumnConstraints(40);
            column2.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column2);

            gridPane.getRowConstraints().add(new RowConstraints(40));
            gridPane.setAlignment(Pos.CENTER);

            gridPane.add(title, 0,0);
            gridPane.add(attendingPicker, 1, 0);

            hours = new LinkedList<>();

            int pos = 1;
            for (Pair<String, Boolean> pair :entitySes.getAttendingSet()) {
                gridPane.getRowConstraints().add(new RowConstraints(30));
                Label date = new Label(pair.getL());
                gridPane.add(date, 0, pos);

                CheckBox auxiliarCheckBox = new CheckBox();
                hours.add(auxiliarCheckBox);
                auxiliarCheckBox.setSelected(pair.getR());
                auxiliarCheckBox.setDisable(!sessio.getAttending());
                auxiliarCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        didChange = true;
                    }
                });
                gridPane.add(auxiliarCheckBox, 1, pos);
                pos++;
            }
            mainVBox.getChildren().add(gridPane);


        }

        public SessioAttending saveAndGetSessio()
        {
            sessio.setAttending(attendingPicker.isSelected());
            if(attendingPicker.isSelected() && didChange)
                for(int i = 0; i< hours.size(); i++)
                    sessio.setWillAttend(i, hours.get(i).isSelected());
            return sessio;
        }

        public SessioAttending getSessio()
        {
            return sessio;
        }
    };

    private Entity currentEntity;
    private ObservableList<Entity> arrayEntity;

    @FXML
    private TextField newEntityId;
    @FXML
    private TextField newEntityName;
    @FXML
    private TextField newEntityNumber;

    LinkedList<SessionColumn> columns = new LinkedList<>();

    @FXML
    protected void initialize() {

        Platform.runLater(() -> {
            int i = 1;
            newEntityNumber.setText(currentEntity.getAttendees());
            newEntityId.setText(currentEntity.getId());
            newEntityName.setText(currentEntity.getName());
            mediumHBOX.setSpacing(30);
            mediumHBOX.setPadding(new Insets(10,10,10,10));
            for(SessioAttending ses : currentEntity.getListOfSessions())
            {
                SessionColumn auxiliar = new SessionColumn(i, ses);
                mediumHBOX.getChildren().add(auxiliar.mainVBox);
                columns.add(auxiliar);
                i++;
            }
        });
    }

    public void setEntity(Entity ent, ObservableList<Entity> privateList)
    {
        currentEntity = ent;
        arrayEntity = privateList;
    }

    public void eraseAction()
    {
        if(AlertDialog.askQuestion(Alert.AlertType.WARNING, null, "Are you sure you want to erase this entity?").get() == ButtonType.YES) {
            arrayEntity.remove(currentEntity);

            Stage stage = (Stage) mediumHBOX.getScene().getWindow();

            stage.close();
        }
    }

    public void saveInfoAction()
    {
        try{
            Integer.parseInt(newEntityNumber.getText());

            var name = newEntityName.getText();
            var id = newEntityId.getText();

            if(!currentEntity.getName().equals(name) || !currentEntity.getId().equals(id)) {
                for (var e : arrayEntity) {
                    if(e != currentEntity)
                        if (e.getName().equals(name) || e.getId().equals(id))
                            throw new Exception("Repeated");
                }
            }

            currentEntity.setName(name);
            currentEntity.setId(id);

            currentEntity.setAttendees(newEntityNumber.getText());

            LinkedList<SessioAttending> attendingSesions = new LinkedList<>();
            for(int i = 0; i<MainData.SharedInstance().getNSessions(); i++)
            {
                attendingSesions.add(columns.get(i).saveAndGetSessio());
            }

            currentEntity.setAttendingSessions(attendingSesions);

            Stage stage = (Stage) mediumHBOX.getScene().getWindow();

            stage.close();
        }
        catch (Exception e)
        {
            if(e.getMessage().equals("Repeated"))
                AlertDialog.showMessage(Alert.AlertType.WARNING, null, "The entity is already on the list");
        }
    }
}