package TFG_project.VIEWS;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MeetingScheduledDetailController {

    @FXML
    private VBox mainVBox;

    @FXML
    private Label currentSessionLabel;

    @FXML
    private Label hourLabel;

    @FXML
    private Label tableLabel;

    @FXML
    private VBox participantsVBox;

    private String pSession;
    private String h;
    private String taula;
    private LinkedList<String> entities;

    @FXML
    protected void initialize() {

        Platform.runLater(() -> {
            currentSessionLabel.setText(pSession);
            hourLabel.setText(h);
            tableLabel.setText(taula);

            entities.forEach(e -> addNewMeeting(e));

        });
    }

    public void addNewMeeting(String value)
    {
        Label newLabel = new Label(value);
        participantsVBox.getChildren().add(newLabel);
    }

    public void setMeeting(String preferredSession, int sessio, String hour, int tb, LinkedList<String> participants)
    {
        pSession = sessio == -1 ? preferredSession : String.valueOf(sessio + 1 );
        h = hour == null ? "--:--" : hour;
        taula = tb == -1 ? "--" : String.valueOf(tb+1);
        entities = participants;
    }

    private void closeView()
    {
        Stage stage = (Stage) mainVBox.getScene().getWindow();

        stage.close();
    }
}