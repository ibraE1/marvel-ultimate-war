package views.gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainMenu {
   public static Scene createMain() {
       Image logo = new Image("views/assets/logo.png");
       ImageView logo_view = new ImageView(logo);
       logo_view.setX(440);
       logo_view.setY(20);
       logo_view.setFitWidth(400);
       logo_view.setPreserveRatio(true);


       Button start = new Button("Start");
       start.setPrefSize(300,50);
       start.setFont(Font.font("Georgia" , 26));

       start.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Control.onStart();
       });

       start.requestFocus();

       Button quit = new Button("Quit");
       quit.setPrefSize(300,50);
       quit.setFont(Font.font("Georgia" , 26));

       quit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Control.onQuit();
       });

       VBox buttons = new VBox();
       buttons.getChildren().add(start);
       buttons.getChildren().add(quit);
       buttons.setLayoutX(490);
       buttons.setLayoutY(300);
       buttons.setSpacing(20);


       Group root = new Group(logo_view);
       root.getChildren().add(buttons);

       return new Scene(root, 1680,1050, Color.rgb(33,41,50));
   }
}
