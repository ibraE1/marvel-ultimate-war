package views.gui;

import engine.Player;
import exceptions.ShortNameException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
        logo_view.setX(440);
        logo_view.setY(20);
        logo_view.setFitWidth(400);
        logo_view.setPreserveRatio(true);

        Button quit = new Button("Quit");
        quit.setPrefSize(100,25);
        quit.setLayoutX(1150);
        quit.setLayoutY(680);
        quit.setFont(Font.font("Georgia"));


        quit.setOnAction(e -> {
            Control.onQuit();
        });

        Button mainMenuBtn = new Button("Main Menu");
        mainMenuBtn.setPrefSize(300,50);
        mainMenuBtn.setLayoutX(20);
        mainMenuBtn.setLayoutY(10);
        mainMenuBtn.setFont(Font.font("Georgia", 26));

        mainMenuBtn.setOnAction(e -> {
            try {
                Control.onMainMenu();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Button play = new Button("Play");
        play.setPrefSize(300,50);
        play.setLayoutX(540);
        play.setLayoutY(550);
        play.setFont(Font.font("Georgia" , 26));

        play.setOnAction(e -> {
            if (p1.getLength() >= 2 && p1.getLength() <= 26 && p2.getLength() >= 2 && p2.getLength() <= 26) {
                player1 = p1.getText();
                player2 = p2.getText();
                try {
                    Control.onPlay();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    throw new ShortNameException("Please enter names between 2 and 26 characters long");
                } catch (ShortNameException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        p1 = new TextField();
        p2 = new TextField();
        p1.setPrefSize(200,40);
        p2.setPrefSize(200,40);
        p1.setFont(Font.font("Georgia",18));
        p2.setFont(Font.font("Georgia",18));
        p1.setPromptText("Player 1");
        p2.setPromptText("Player 2");

        VBox vbox = new VBox();
        vbox.getChildren().add(p1);
        vbox.getChildren().add(p2);
        vbox.setLayoutX(540);
        vbox.setLayoutY(200);
        vbox.setSpacing(20);

        VBox buttons = new VBox();
        buttons.getChildren().add(play);
        buttons.getChildren().add(mainMenuBtn);
        buttons.setLayoutX(490);
        buttons.setLayoutY(400);
        buttons.setSpacing(20);

        Group root = new Group(logo_view);
        root.getChildren().add(vbox);
        root.getChildren().add(buttons);
        root.getChildren().add(quit);

        return new Scene(root, 1280,720, Color.rgb(33,41,50));
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

