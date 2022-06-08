package views.gui;

import engine.Player;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameOver {
    public static Scene create(Player winner) {
        Label winnerName = new Label("The winner is: " + winner.getName() + "!");
        winnerName.setStyle("    -fx-font-size: 32px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #333333;\n" +
                "    -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");
        Button quit = new Button("Quit");
        quit.setPrefSize(300, 75);
        quit.setFont(Font.font("Arial", 26));
        quit.styleProperty().bind(Bindings.when(quit.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        quit.setOnAction(e -> GameApp.onQuit());
        VBox box = new VBox();
        box.getChildren().add(winnerName);
        box.getChildren().add(quit);
        box.setAlignment(Pos.BASELINE_CENTER);
        box.setLayoutX(650);
        box.setLayoutY(400);
        box.setSpacing(20);


        Pane root = new Pane();
        Image img = new Image("views/assets/bg.jpeg");
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        root.getChildren().add(box);
        return new Scene(root, 1600, 900, Color.rgb(33, 41, 50));
    }
}
