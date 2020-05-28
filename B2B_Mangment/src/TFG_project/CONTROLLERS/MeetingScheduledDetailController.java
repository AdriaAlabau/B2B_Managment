package TFG_project.CONTROLLERS;

import TFG_project.Entities.*;
import TFG_project.HELPERS.AlertDialog;
import TFG_project.HELPERS.Constants;
import TFG_project.HELPERS.Pair;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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