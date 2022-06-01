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
    private static String player1;
    private static String player2;

    public static Scene createStart() {
        Image logo = new Image("views/assets/logo.png");
        ImageView logo_view = new ImageView(logo);
        logo_view.setX(640);
        logo_view.setY(20);
        logo_view.setFitWidth(400);
        logo_view.setPreserveRatio(true);

        Button quit = new Button("Quit");
        quit.setPrefSize(100, 25);
        quit.setLayoutX(1550);
        quit.setLayoutY(1010);
        quit.setFont(Font.font("Georgia"));
        quit.styleProperty().bind(Bindings.when(quit.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));


        quit.setOnAction(e -> {
            Control.onQuit();
        });

        Button mainMenuBtn = new Button("Main Menu");
        mainMenuBtn.setPrefSize(350, 75);
        mainMenuBtn.setFont(Font.font("Arial", 26));
        mainMenuBtn.styleProperty().bind(Bindings.when(mainMenuBtn.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));

        mainMenuBtn.setOnAction(e -> {
            try {
                Control.onMainMenu();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

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
                    player1 = p1.getText();
                    player2 = p2.getText();
                    Control.onPlay();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
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
        vbox.setLayoutX(700);
        vbox.setLayoutY(250);
        vbox.setSpacing(20);

        VBox buttons = new VBox();
        buttons.getChildren().add(play);
        buttons.getChildren().add(mainMenuBtn);
        buttons.setLayoutX(665);
        buttons.setLayoutY(450);
        buttons.setSpacing(20);

        Group root = new Group(logo_view);
        root.getChildren().add(vbox);
        root.getChildren().add(buttons);
        root.getChildren().add(quit);

        Scene scene = new Scene(root, 1600, 900, Color.rgb(33, 41, 50));
        buttons.requestFocus();
        return scene;
    }

    public static TextField getP1() {
        return p1;
    }

    public static TextField getP2() {
        return p2;
    }

    public static String getPlayer1() {
        return player1;
    }

    public static String getPlayer2() {
        return player2;
    }
}

