package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.Constants;
import TFG_project.HELPERS.Pair;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SetArribalInfo {

    @FXML
    private HBox mediumHBOX;

    private LinkedList<Sessio> currentSessions;

    class SessionColumn
    {

        public VBox mainVBox;

        private CheckBox attendingPicker;
        private Label title;

        private GridPane gridPane;
        private LinkedList<CheckBox> hours;

        private SessioAttending sessio;
        private Sessio mainSessio;

        private boolean didChange = false;

        public SessionColumn(int i, SessioAttending entitySes, Sessio mSes)
        {

            sessio = entitySes;
            mainSessio = mSes;
            mainVBox = new VBox();

            //HBOX amb label i checkbox (main atending)
            title = new Label("Session " + i);
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

    LinkedList<SessionColumn> columns = new LinkedList<>();

    @FXML
    protected void initialize() {

        Platform.runLater(() -> {
            int i = 1;
            mediumHBOX.setSpacing(30);
            mediumHBOX.setPadding(new Insets(10,10,10,10));
            for(Sessio ses : currentSessions)
            {

                SessionColumn auxiliar = new SessionColumn(i, currentEntity.getListOfSessions().get(i-1), ses);
                mediumHBOX.getChildren().add(auxiliar.mainVBox);
                columns.add(auxiliar);
                i++;
            }
        });
    }

    public void setEntity(Entity ent, LinkedList<Sessio> sessions)
    {
        currentEntity = ent;
        currentSessions = sessions;
    }

    public void saveInfoAction()
    {
        LinkedList<SessioAttending> attendingSesions = new LinkedList<>();
        for(int i = 0; i<MainData.SharedInstance().getNSessions(); i++)
        {
            attendingSesions.add(columns.get(i).saveAndGetSessio());
        }
        currentEntity.setAttendingSessions(attendingSesions);

        Stage stage = (Stage) mediumHBOX.getScene().getWindow();

        stage.close();
    }
}