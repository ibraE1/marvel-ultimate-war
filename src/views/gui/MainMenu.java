package views.gui;

import javafx.beans.binding.Bindings;
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
       logo_view.setX(540);
       logo_view.setY(20);
       logo_view.setFitWidth(600);
       logo_view.setPreserveRatio(true);


       Button start = new Button("Start");
       start.setPrefSize(300,75);
       start.setFont(Font.font("Arial" , 26));
       start.styleProperty().bind(Bindings.when(start.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                       " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                       " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                       " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                       "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
               .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                       ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
       start.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Control.onStart();
       });

       start.requestFocus();

       Button quit = new Button("Quit");
       quit.setPrefSize(300,75);
       quit.setFont(Font.font("Arial" , 26));
       quit.styleProperty().bind(Bindings.when(quit.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                       " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                       " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                       " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                       "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
               .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                       ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));

       quit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Control.onQuit();
       });

       VBox buttons = new VBox();
       buttons.getChildren().add(start);
       buttons.getChildren().add(quit);
       buttons.setLayoutX(690);
       buttons.setLayoutY(300);
       buttons.setSpacing(20);


       Group root = new Group(logo_view);
       root.getChildren().add(buttons);

       return new Scene(root, 1680,1050, Color.rgb(33,41,50));
   }
}
