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

public class MeetingDetailController {

    @FXML
    private VBox mainVBox;

    public static ObservableList<String> ListOfSessions;

    private Meeting currentMeeting;
    private ObservableList<Meeting> arrayEntity;

    @FXML
    private GridPane newMettingsGroup;

    private LinkedList<TextField> extraMeetings;
    private LinkedList<Label> extraLabels;

    @FXML
    private ChoiceBox preferedSessionChoiceBox;

    @FXML
    protected void initialize() {

        extraMeetings = new LinkedList<>();
        extraLabels = new LinkedList<>();
        Platform.runLater(() -> {
            preferedSessionChoiceBox.setItems(ListOfSessions);
            preferedSessionChoiceBox.setValue(currentMeeting.getSessio());

            currentMeeting.getListOfParticipants().forEach(e -> addNewMeeting(e));
        });
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

    public void addNewMeeting(String participant)
    {
        TextField newTF = new TextField("");
        newTF.setPromptText("Set Entity Id...");
        newTF.setText(participant);
        var i = newMettingsGroup.getRowCount() == 0 ? newMettingsGroup.getRowCount() : 0;
        Label newLabel = new Label(String.valueOf(newMettingsGroup.getRowCount() +1 ) + ".");
        extraMeetings.add(newTF);
        extraLabels.add(newLabel);
        int rowCount = newMettingsGroup.getRowCount();
        newMettingsGroup.add(newLabel, 0, rowCount);
        newMettingsGroup.add(newTF, 1, rowCount);
    }

    public void setMeeting(Meeting meet, ObservableList<Meeting> privateList, ObservableList<String> ses)
    {
        currentMeeting = meet;
        arrayEntity = privateList;
        ListOfSessions = ses;
    }

    public void eraseAction()
    {
        if(AlertDialog.askQuestion(Alert.AlertType.CONFIRMATION, null, "Are you sure you want to erase this meeting?").get() == ButtonType.OK) {
            arrayEntity.remove(currentMeeting);
        }

       closeView();
    }

    public void saveInfoAction()
    {

        int correct = 0;
        int pos = 0;
        while(correct < 2 && pos < extraMeetings.size())
        {
            if(!extraMeetings.get(pos).getText().isEmpty())
                correct++;
            pos++;
        }
        if(correct>= 2) {
            if (currentMeeting == null)
                currentMeeting = new Meeting();

            currentMeeting.setSessio((String) preferedSessionChoiceBox.getValue());

            currentMeeting.cleanListOfParticipants();

            for (int i = extraMeetings.size() - 1; i >= 0; i--) {
                if (!extraMeetings.get(i).getText().isEmpty()) {
                    currentMeeting.addMetting(extraMeetings.get(i).getText());
                }

            }
            closeView();
        }
        else
            AlertDialog.showMessage(Alert.AlertType.WARNING, null, "You need at least two entities in a meeting");



    }

    private void closeView()
    {
        Stage stage = (Stage) mainVBox.getScene().getWindow();

        stage.close();
    }
}