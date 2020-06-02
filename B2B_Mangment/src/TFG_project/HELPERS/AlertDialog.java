package TFG_project.HELPERS;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.awt.event.WindowEvent;
import java.util.Optional;

public class AlertDialog {
   public static void showMessage(Alert.AlertType type, String title,  String message)
   {
       Alert alert = new Alert(type);
       if(title != null)
        alert.setTitle(title);
       if(message != null)
        alert.setContentText(message);

       alert.showAndWait();
   }

    public static Optional<ButtonType> askQuestion(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if(title != null)
            alert.setTitle(title);
        if(message != null)
            alert.setContentText(message);
        var result = alert.showAndWait();
        return result;
    }

    public static Optional<ButtonType> askSave(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.NONE);
        if(title != null)
            alert.setTitle(title);
        if(message != null)
            alert.setContentText(message);
        ButtonType customResult = null;

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

        var result = alert.showAndWait();
        return result;
    }
}
