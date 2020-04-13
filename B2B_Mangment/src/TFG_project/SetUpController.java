package TFG_project;

import TFG_project.Entities.Entity;
import TFG_project.Entities.MainData;
import TFG_project.Entities.Sessio;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import javax.tools.Tool;
import java.util.LinkedList;

public class SetUpController {

    @FXML
    private GridPane sessionsGridPane;

    private ObservableList<String> arrayOfEnd = FXCollections.observableArrayList( "00:30", "01:00", "01:30","02:00","02:30",
            "03:00","03:30", "04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00", "09:30", "10:00", "10:30",
            "11:00","11:30", "12:00","12:30", "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30", "18:00","18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00","21:30", "22:00","22:30", "23:00", "23:30","23:59");

    private ObservableList<String> arrayOfStart = FXCollections.observableArrayList("00:00", "00:30", "01:00", "01:30","02:00","02:30",
            "03:00","03:30", "04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30","08:00","08:30","09:00", "09:30", "10:00", "10:30",
            "11:00","11:30", "12:00","12:30", "13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30", "18:00","18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00","21:30", "22:00","22:30", "23:00", "23:30");

    class SessionColumn
    {
        public Label title;
        public ChoiceBox<String> startHour;
        public ChoiceBox<String> endHour;

        private Boolean didChange = false;
        private Sessio sessio;

        public SessionColumn(int i, Sessio ses)
        {
            sessio = ses;
            title = new Label("Session " + i);

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
        }

        public Sessio getSessio()
        {
            return sessio;
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

       // sessionsGridPane.getColumnConstraints().add(new ColumnConstraints(800));

        for(int i = 1; i <= days; i++)
        {
            Sessio ses = llistatSessions.get(i-1);
            SessionColumn auxiliar = new SessionColumn(i, ses);
            sessionsGridPane.add(auxiliar.title, i, 0);
            sessionsGridPane.add(auxiliar.startHour, i, 1);
            sessionsGridPane.add(auxiliar.endHour, i, 2);
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
    }
}
