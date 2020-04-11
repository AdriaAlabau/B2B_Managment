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

/*primaryStage.setTitle("TFG PRIMERA PROVA");


        Text text = new Text();
        text.setText("Click ME");

        //setting the position of the text
        text.setX(50);
        text.setY(50);
        text.setFill(Color.BROWN);


        //button.setBackground(Color.BROWN);

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                List<Object> value =  Helper.getValues();
                int i = 0;
                int j = 0;
                int x = 0;
                for (Object obj : value) {
                    Text t = new Text(obj.toString());
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(new ShapeHelp((i == 0 && j %2 == 0) || (i == 1 && j %2 != 0)), t);
                    grid.add(stack, i, j);
                    if(i== 1)
                    {
                        i=-1;
                        j++;
                    }
                    i++;
                }
            }
        };

        text.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        //Creating a Grid Pane
        grid = new GridPane();

        //Setting size for the pane
        grid.setMinSize(400, 200);

        //Setting the padding
        grid.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns

        //Setting the Grid alignment
        grid.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid


        //Creating a Group object
        Group root = new Group(grid,text);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();*/
