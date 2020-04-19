package TFG_project;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import TFG_project.Entities.Sessio;
import TFG_project.Entities.TableForSession;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.tools.Tool;
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

    private ObservableList<String> arrayOfEnd = FXCollections.observableArrayList( "00:30", "01:00", "01:30","02:00","02:30",
            "03:00","03:30", "04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00", "09:30", "10:00", "10:30",
            "11:00","11:30", "12:00","12:30", "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30", "18:00","18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00","21:30", "22:00","22:30", "23:00", "23:30","23:59");

    private ObservableList<String> arrayOfStart = FXCollections.observableArrayList("00:00", "00:30", "01:00", "01:30","02:00","02:30",
            "03:00","03:30", "04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00", "09:30", "10:00", "10:30",
            "11:00","11:30", "12:00","12:30", "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30", "18:00","18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00","21:30", "22:00","22:30", "23:00", "23:30");

    private int lastGridPos = 3;

    private LinkedList<Integer> tableValues;

    class SessionColumn
    {
        public Label title;
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

            startHour = new ChoiceBox<>(arrayOfStart);
            startHour.setTooltip(new Tooltip("Select Start Hour"));
            startHour.setValue(sessio.getHoraInici());
            startHour.getSelectionModel().selectedIndexProperty().addListener(new
              ChangeListener<Number>() {
                  @Override
                  public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                      String newValueStr = arrayOfStart.get(newValue.intValue());

                      sessio.setHoraInici(newValueStr);
                      int index = arrayOfStart.indexOf(newValueStr);
                      int indexLast = arrayOfEnd.indexOf(sessio.getHoraFis());
                      if(index > indexLast)
                      {
                          endHour.setValue(arrayOfEnd.get(index));
                      }
                  }
              });

            endHour = new ChoiceBox<>(arrayOfEnd);
            endHour.setTooltip(new Tooltip("Select End Hour"));
            endHour.setValue(sessio.getHoraFis());
            endHour.getSelectionModel().selectedIndexProperty().addListener(new
             ChangeListener<Number>() {
                 @Override
                 public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                     String newValueStr = arrayOfEnd.get(newValue.intValue());
                     sessio.setHoraFi(newValueStr);

                     sessio.setHoraInici(newValueStr);
                     int indexLast = arrayOfEnd.indexOf(newValueStr);
                     int index = arrayOfStart.indexOf(sessio.getHoraInici());
                     if(index > indexLast)
                     {
                         startHour.setValue(arrayOfStart.get(indexLast));
                     }
                 }
             });

            for (Map.Entry me : sessio.getListOfTables().entrySet()) {
                setNewTableConfig(((int)me.getKey()), ((TableForSession)me.getValue()).nUnits);
            }

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
                        int pos = Integer.parseInt(tf.getId());
                        sessio.setNTables(pos, new TableForSession(pos, value));
                    }
                    catch (Exception e)
                    {
                        //TODO SHOW SOME ALERT MAYBE PUT RED AND UNDO WHEN VALUE CHECK TRUE
                    }
                }
            });
            tablesConfig.add(tf);
            sessio.setNTables(nSeats, new TableForSession(nSeats, value));
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
            sessionsGridPane.add(auxiliar.startHour, i, 1);
            sessionsGridPane.add(auxiliar.endHour, i, 2);

            int j = 3;
            for (TextField t :  auxiliar.tablesConfig) {
                if(i == 1)
                {
                    setNewRowStuff(j,Integer.parseInt(t.getId()));
                }
                sessionsGridPane.add(t, i, j);
                j++;
            }
            lastGridPos = j;
            ColumnConstraints constr = new ColumnConstraints(80);
            constr.setHalignment(HPos.CENTER);

            sessionsGridPane.getColumnConstraints().add(constr);


            columns.add(auxiliar);
        }
    }

    public void saveInfoAction()
    {
        for(int i = 0; i<MainData.SharedInstance().getNSessions(); i++)
        {
            MainData.SharedInstance().setSession(i, columns.get(i).getSessio());
        }
        Stage stage = (Stage) sessionsGridPane.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void setNewTableValue()
    {
        try{
            int value = Integer.parseInt(newTableValue.getText());
            if(!tableValues.contains(value))
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

        }
        catch(Exception e)
        {
            //TODO SET ALERT
        }

    }

    private void setNewRowStuff(int gridPos, int seats)
    {
        tableValues.add(seats);
        Label label = new Label("N Tables of " + seats);
        label.setFont(Font.font ("System", FontWeight.BOLD, 14));

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