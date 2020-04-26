package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.Constants;
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
    private GridPane sessionsGridPane;

    private LinkedList<Sessio> currentSessions;

    class SessionColumn
    {
        public Label title;
        public CheckBox attendingPicker;
        public ChoiceBox<String> startHour;
        public ChoiceBox<String> endHour;


        private SessioAttending sessio;
        private Sessio mainSessio;

        public SessionColumn(int i, SessioAttending entitySes, Sessio mSes)
        {
            sessio = entitySes;
            mainSessio = mSes;
            title = new Label("Session " + i);

            attendingPicker = new CheckBox();
            attendingPicker.setSelected(sessio.getAttending());

            attendingPicker.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    startHour.setDisable(!newValue);
                    endHour.setDisable(!newValue);
                }
            });
            var init = Constants.ARRAYOFSTART.indexOf(mainSessio.getHoraInici());
            var end = Constants.ARRAYOFSTART.indexOf(mainSessio.getHoraFis());

            var arrayStartMod = FXCollections.observableArrayList(Constants.ARRAYOFSTART.subList(init, end > -1 ? end : Constants.ARRAYOFSTART.size()));

            init = Constants.ARRAYOFEND.indexOf(mainSessio.getHoraInici());
            end = Constants.ARRAYOFEND.indexOf(mainSessio.getHoraFis());

            var arrayEndMod = FXCollections.observableArrayList(Constants.ARRAYOFEND.subList(init > -1 ? init+1 : 0, end+1));

            startHour = new ChoiceBox<>(arrayStartMod);
            startHour.setTooltip(new Tooltip("Select Start Hour"));

            startHour.setValue(arrayStartMod.indexOf(sessio.getHoraInici()) == -1 ? mainSessio.getHoraInici() : sessio.getHoraInici());
            startHour.getSelectionModel().selectedIndexProperty().addListener(new
              ChangeListener<Number>() {
                  @Override
                  public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                      /*String newValueStr = arrayOfStart.get(newValue.intValue());

                      //sessio.setHoraInici(newValueStr);
                      int index = arrayOfStart.indexOf(newValueStr);
                      int indexLast = arrayOfEnd.indexOf(sessio.getHoraFis());
                      if(index > indexLast)
                      {
                          endHour.setValue(arrayOfEnd.get(index));
                      }*/
                  }
              });

            endHour = new ChoiceBox<>(arrayEndMod);
            endHour.setTooltip(new Tooltip("Select End Hour"));
            endHour.setValue(arrayEndMod.indexOf(sessio.getHoraFis()) == -1 ? mainSessio.getHoraFis() : sessio.getHoraFis());
            endHour.getSelectionModel().selectedIndexProperty().addListener(new
                ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                        /*String newValueStr = arrayOfEnd.get(newValue.intValue());
                        //sessio.setHoraFi(newValueStr);

                        //sessio.setHoraInici(newValueStr);
                        int indexLast = arrayOfEnd.indexOf(newValueStr);
                        int index = arrayOfStart.indexOf(sessio.getHoraInici());
                        if(index > indexLast)
                        {
                            startHour.setValue(arrayOfStart.get(indexLast));
                        }*/
                    }
                });
        }

        public SessioAttending saveAndGetSessio()
        {
            sessio.setAttending(attendingPicker.isSelected());
            sessio.setHoraFi(endHour.getValue());
            sessio.setHoraInici(startHour.getValue());

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
            for(Sessio ses : currentSessions)
            {

                SessionColumn auxiliar = new SessionColumn(i, currentEntity.getListOfSessions().get(i-1), ses);
                sessionsGridPane.add(auxiliar.title, i, 0);
                sessionsGridPane.add(auxiliar.attendingPicker, i, 1);
                sessionsGridPane.add(auxiliar.startHour, i, 2);
                sessionsGridPane.add(auxiliar.endHour, i, 3);

                ColumnConstraints constr = new ColumnConstraints(110);
                constr.setHalignment(HPos.CENTER);

                sessionsGridPane.getColumnConstraints().add(constr);

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

        Stage stage = (Stage) sessionsGridPane.getScene().getWindow();

        stage.close();
    }
}