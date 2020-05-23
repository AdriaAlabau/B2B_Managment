package TFG_project.HELPERS;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
        Alert alert = new Alert(type);
        if(title != null)
            alert.setTitle(title);
        if(message != null)
            alert.setContentText(message);
        var result = alert.showAndWait();
        return result;
    }
}
