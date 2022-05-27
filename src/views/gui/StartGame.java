package views.gui;

import exceptions.ShortNameException;
import javafx.application.Application;
import javafx.scene.Cursor;
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
import javafx.stage.Stage;

import java.awt.*;

public class StartGame extends Application {


    private TextField p1;
    private TextField p2;
    private String player1;
    private String player2;

    public StartGame () throws Exception {
        Stage startMenu = new Stage();
        start(startMenu);
    }

    @Override
    public void start(Stage startMenu) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Image icon = new Image("views/assets/icon.png");

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


        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button back = new Button("Main Menu");
        back.setPrefSize(300,50);
        back.setLayoutX(20);
        back.setLayoutY(10);
        back.setFont(Font.font("Georgia", 26));

        back.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            p1.clear();
            p2.clear();

            player1 = null;
            player2 = null;

            try {
                startMenu.close();
                MainMenu m = new MainMenu();
                Stage main = new Stage();
                m.start(main);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button start = new Button("Start");
        start.setPrefSize(300,50);
        start.setLayoutX(540);
        start.setLayoutY(550);
        start.setFont(Font.font("Georgia" , 26));

        start.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (p1.getLength() < 2 || p1.getLength() > 26 || p2.getLength() < 2 || p2.getLength() > 26) {
                try {
                    throw new ShortNameException();
                } catch (ShortNameException ex) {
                    System.out.println("Please enter names between 2 and 26 characters long");
                }
            }

            player1 = p1.getText();
            player2 = p2.getText();
            p1.clear();
            p2.clear();
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
        buttons.getChildren().add(start);
        buttons.getChildren().add(back);
        buttons.setLayoutX(490);
        buttons.setLayoutY(400);
        buttons.setSpacing(20);

        Group root = new Group(logo_view);
        root.getChildren().add(vbox);
        root.getChildren().add(buttons);
        root.getChildren().add(quit);

        vbox.getParent().requestFocus();
        Scene scene = new Scene(root, 1280,720, Color.rgb(33,41,50));

        start.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            scene.setCursor(javafx.scene.Cursor.HAND);
        });

        start.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        back.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            scene.setCursor(Cursor.HAND);
        });

        back.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        quit.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            scene.setCursor(Cursor.HAND);
        });

        quit.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        start.requestFocus();

        startMenu.setResizable(false);
        startMenu.setX((screenSize.getWidth() / 2) - 640);
        startMenu.setY((screenSize.getHeight() / 2) - 360);
        startMenu.getIcons().add(icon);
        startMenu.setTitle("Marvel Ultimate War");
        startMenu.setScene(scene);
        startMenu.show();

    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }
}

