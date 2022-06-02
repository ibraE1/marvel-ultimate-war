package views.gui;

import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EnterPlayerNames {
    private static TextField p1;
    private static TextField p2;

    public static Scene create() {
        ImageView logo = new ImageView(new Image("views/assets/logo.png"));
        logo.setX(500);
        logo.setY(15);
        logo.setFitWidth(600);
        logo.setPreserveRatio(true);

        Button play = new Button("Play");
        play.setPrefSize(350, 75);
        play.setFont(Font.font("Arial", 26));
        play.styleProperty().bind(Bindings.when(play.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        play.setOnAction(e -> {
            try {
                if (p1.getLength() >= 2 && p1.getLength() <= 26 && p2.getLength() >= 2 && p2.getLength() <= 26) {
                    GameApp.onPlay(p1.getText(), p2.getText());
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Button quit = new Button("Quit");
        quit.setPrefSize(350, 75);
        quit.setFont(Font.font("Arial", 26));
        quit.styleProperty().bind(Bindings.when(quit.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        quit.setOnAction(e -> {
            GameApp.onQuit();
        });

        p1 = new TextField();
        p2 = new TextField();
        p1.setPrefSize(280, 50);
        p2.setPrefSize(280, 50);
        p1.setFont(Font.font("Arial", 18));
        p2.setFont(Font.font("Arial", 18));
        p1.setPromptText("Player 1");
        p2.setPromptText("Player 2");

        VBox vbox = new VBox();
        vbox.getChildren().add(p1);
        vbox.getChildren().add(p2);
        vbox.setLayoutX(660);
        vbox.setLayoutY(270);
        vbox.setSpacing(20);

        VBox buttons = new VBox();
        buttons.getChildren().add(play);
        buttons.getChildren().add(quit);
        buttons.setLayoutX(625);
        buttons.setLayoutY(470);
        buttons.setSpacing(20);

        Group root = new Group(logo);
        root.getChildren().add(vbox);
        root.getChildren().add(buttons);

        Scene scene = new Scene(root, 1600, 900, Color.rgb(33, 41, 50));

        buttons.requestFocus();

        return scene;
    }
}
