package TFG_project.CONTROLLERS;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class FirstSceenController {

    @FXML
    private javafx.scene.control.Button buttonCreate;

    public void buttonCreateNew() throws Exception
    {
        Stage CreateNew = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../FXML/create_new.fxml"));
        CreateNew.setTitle("Creating New Schedule");
        CreateNew.setScene(new Scene(root, 800, 800));

        ((Stage)buttonCreate.getScene().getWindow()).close();

        CreateNew.show();
    }

}
