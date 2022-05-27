package views.gui;

import com.sun.tools.javac.Main;
import exceptions.ShortNameException;
import javafx.application.Application;
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

    @Override
    public void start(Stage main) throws Exception {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image icon = new Image("views/assets/icon.png");
        Image logo = new Image("views/assets/logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setX(515);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        Button quit = new Button("Quit");
        quit.setPrefSize(100,25);
        quit.setLayoutX(1150);
        quit.setLayoutY(680);

        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        Button back = new Button("Main Menu");
        back.setPrefSize(100,25);
        back.setLayoutX(20);
        back.setLayoutY(10);

        back.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            p1.clear();
            p2.clear();

            player1 = null;
            player2 = null;

            try {
                main.close();
                new MainMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        Button start = new Button("Start");
        start.setPrefSize(200,50);
        start.setLayoutX(540);
        start.setLayoutY(550);

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
        vbox.setLayoutY(320);
        vbox.setSpacing(20);

        Group root = new Group(logoView);
        root.getChildren().add(vbox);
        root.getChildren().add(start);
        root.getChildren().add(back);
        root.getChildren().add(quit);

        vbox.getParent().requestFocus();
        Scene scene = new Scene(root, 1280,720, Color.rgb(33,41,50));

        main.setResizable(false);
        main.setX((screenSize.getWidth() / 2) - 640);
        main.setY((screenSize.getHeight() / 2) - 360);

        main.getIcons().add(icon);
        main.setTitle("Marvel Ultimate War");

        main.setScene(scene);
        main.show();

    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

