package TFG_project.CONTROLLERS;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import TFG_project.Entities.Sessio;
import TFG_project.Entities.TableForSession;
import TFG_project.HELPERS.Constants;
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

public class SetUpController {

    @FXML
    private VBox mainLayout;

    @FXML
    private GridPane sessionsGridPane;

    @FXML
    private TextField newTableValue;

    private int lastGridPos = 3;

    private LinkedList<Integer> tableValues;

    class SessionColumn
    {
        public Label title;
        public DatePicker datePicker;
        public ChoiceBox<String> startHour;
        public ChoiceBox<String> endHour;
        public List<TextField> tablesConfig;

        private Boolean didChange = false;
        private Sessio sessio;

        public SessionColumn(int i, Sessio ses)
        {
            sessio = ses;
            title = new Label("Session " + i);

            tablesConfig = new LinkedList<>();

            datePicker = new DatePicker();
            datePicker.setValue(sessio.getDate());

            startHour = new ChoiceBox<>(Constants.ARRAYOFSTART);
            startHour.setTooltip(new Tooltip("Select Start Hour"));
            startHour.setValue(sessio.getHoraInici());
            startHour.getSelectionModel().selectedIndexProperty().addListener(new
              ChangeListener<Number>() {
                  @Override
                  public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                      String newValueStr = Constants.ARRAYOFSTART.get(newValue.intValue());

                      //sessio.setHoraInici(newValueStr);
                      int index = Constants.ARRAYOFSTART.indexOf(newValueStr);
                      int indexLast = Constants.ARRAYOFEND.indexOf(sessio.getHoraFis());
                      if(index > indexLast)
                      {
                          endHour.setValue(Constants.ARRAYOFEND.get(index));
                      }
                  }
              });

            endHour = new ChoiceBox<>(Constants.ARRAYOFEND);
            endHour.setTooltip(new Tooltip("Select End Hour"));
            endHour.setValue(sessio.getHoraFis());
            endHour.getSelectionModel().selectedIndexProperty().addListener(new
             ChangeListener<Number>() {
                 @Override
                 public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                     String newValueStr = Constants.ARRAYOFEND.get(newValue.intValue());
                     //sessio.setHoraFi(newValueStr);

                     //sessio.setHoraInici(newValueStr);
                     int indexLast = Constants.ARRAYOFEND.indexOf(newValueStr);
                     int index = Constants.ARRAYOFSTART.indexOf(sessio.getHoraInici());
                     if(index > indexLast)
                     {
                         startHour.setValue(Constants.ARRAYOFSTART.get(indexLast));
                     }
                 }
             });
            for (Map.Entry me : sessio.getListOfTables().entrySet()) {
                setNewTableConfig(((int)me.getKey()), ((TableForSession)me.getValue()).nUnits);
            }

        }

        public Sessio saveAndGetSessio()
        {
            sessio.setDate(datePicker.getValue());
            sessio.setHoraInici(startHour.getValue());
            sessio.setHoraFi(endHour.getValue());
            for (TextField tf: tablesConfig) {
                try{
                    int value = Integer.parseInt(tf.getText());
                    int pos = Integer.parseInt(tf.getId());
                    sessio.setNTables(pos, new TableForSession(pos, value));
                }
                catch (Exception e)
                {
                    //TODO SHOW SOME ALERT MAYBE PUT RED AND UNDO WHEN VALUE CHECK TRUE
                }
            }
            return sessio;
        }

        public Sessio getSessio()
        {
            return sessio;
        }

        public void setNewTableConfig(int nSeats, int value)
        {
            TextField tf = new TextField();
            tf.setText(String.valueOf(value));
            tf.setId(String.valueOf(nSeats));
            tf.setMaxWidth(60);
            tf.setAlignment(Pos.CENTER_RIGHT);
            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {

                    try{
                        int value = Integer.parseInt(newValue);
                        //tf.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, tf.getBackground().getOutsets())));
                        //sessio.setNTables(pos, new TableForSession(pos, value));
                    }
                    catch (Exception e)
                    {
                        //tf.setBackground(new Background(new BackgroundFill(Color.rgb(250,128,114), CornerRadii.EMPTY, tf.getBackground().getOutsets())));
                    }
                }
            });
            tablesConfig.add(tf);
            //sessio.setNTables(nSeats, new TableForSession(nSeats, value));
        }

        public TextField getLastTableConfig()
        {
           return tablesConfig.get(tablesConfig.size()-1);
        }
    };

    LinkedList<SessionColumn> columns = new LinkedList<>();

    public SetUpController()
    {

    }

    @FXML
    protected void initialize() {
        int days = MainData.SharedInstance().getNSessions();
        LinkedList<Sessio> llistatSessions= MainData.SharedInstance().getSessions();

        tableValues = new LinkedList<>();

        for(int i = 1; i <= days; i++)
        {
            Sessio ses = llistatSessions.get(i-1);
            if(i == 1)
            {
                tableValues.addAll(ses.getTableValues());
            }
            SessionColumn auxiliar = new SessionColumn(i, ses);
            sessionsGridPane.add(auxiliar.title, i, 0);
            sessionsGridPane.add(auxiliar.datePicker, i, 1);
            sessionsGridPane.add(auxiliar.startHour, i, 2);
            sessionsGridPane.add(auxiliar.endHour, i, 3);

            int j = 4;
            for (TextField t :  auxiliar.tablesConfig) {
                if(i == 1)
                {
                    setNewRowStuff(j,Integer.parseInt(t.getId()));
                }
                sessionsGridPane.add(t, i, j);
                j++;
            }
            lastGridPos = j;
            ColumnConstraints constr = new ColumnConstraints(110);
            constr.setHalignment(HPos.CENTER);

            sessionsGridPane.getColumnConstraints().add(constr);


            columns.add(auxiliar);
        }
    }

    public void saveInfoAction()
    {
        for(int i = 0; i<MainData.SharedInstance().getNSessions(); i++)
        {
            MainData.SharedInstance().setSession(i, columns.get(i).saveAndGetSessio());
        }
        Stage stage = (Stage) sessionsGridPane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void setNewTableValue()
    {
        try{
            int value = Integer.parseInt(newTableValue.getText());
            if(value > 0 && !tableValues.contains(value))
            {
                setNewRowStuff(lastGridPos, value);

                int i = 1;
                for(SessionColumn aux : columns)
                {
                    aux.setNewTableConfig(value,0);
                    sessionsGridPane.add(aux.getLastTableConfig(),i,lastGridPos);
                    i++;
                }

                lastGridPos++;
            }
            newTableValue.setText("");

        }
        catch(Exception e)
        {
            if(newTableValue.getText().equals("*") && !tableValues.contains(0)) {
                setNewRowStuff(lastGridPos, 0);

                int i = 1;
                for(SessionColumn aux : columns)
                {
                    aux.setNewTableConfig(0,0);
                    sessionsGridPane.add(aux.getLastTableConfig(),i,lastGridPos);
                    i++;
                }

                lastGridPos++;
                newTableValue.setText("");
            }
        }

    }

    private void setNewRowStuff(int gridPos, int seats)
    {
        tableValues.add(seats);
        //Label label = new Label("N Tables of " + (seats == 0 ? "N" : seats));
        Label label = new Label("Tables");

        sessionsGridPane.add(label, 0,  gridPos);
        RowConstraints constr = new RowConstraints(40);
        sessionsGridPane.getRowConstraints().add(constr);

        try {
            Stage thisStage = ((Stage) sessionsGridPane.getScene().getWindow());

            thisStage.setHeight(thisStage.getHeight() + 40);

        }
        catch(Exception e)
        {
            //Nothing to do
        }
    }
}
