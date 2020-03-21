package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;

import java.util.LinkedList;
import java.util.List;

public class Main extends Application {
    private GridPane grid;


    public class ShapeHelp extends Rectangle {

        public ShapeHelp(boolean color) {
            setWidth(50);
            setHeight(30);

            Color col = color ? Color.BLUE.deriveColor(0, 1.2, 1, 0.6) : Color.DARKBLUE.deriveColor(0, 1.2, 1, 0.6);
            Color secCol = color ? Color.BLUE : Color.DARKBLUE;
            setFill(col);
            setStroke(secCol);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
