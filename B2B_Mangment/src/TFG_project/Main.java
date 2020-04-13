package TFG_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("create_new.fxml"));
        primaryStage.setTitle("B2B_Managment");
        primaryStage.setScene(new Scene(root, 1300, 900));
        primaryStage.show();
    }
}

