package views.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class MainMenu extends Application {

    @Override
    public void start(Stage main) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Image icon = new Image("views/assets/icon.png");
        main.getIcons().add(icon);

        Image logo = new Image("views/assets/logo.png");
        ImageView logo_view = new ImageView(logo);
        logo_view.setX(440);
        logo_view.setY(20);
        logo_view.setFitWidth(400);
        logo_view.setPreserveRatio(true);


        Button play = new Button("Play");
        play.setPrefSize(300,50);
        play.setFont(Font.font("Georgia" , 26));


        play.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                new StartGame();
                main.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        Button champions = new Button("Champions");
        champions.setPrefSize(300,50);
        champions.setFont(Font.font("Georgia" , 26));

        champions.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                new AvailableChampions();
                main.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        Button quit = new Button("Quit");
        quit.setPrefSize(300,50);
        quit.setFont(Font.font("Georgia" , 26));


        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox buttons = new VBox();
        buttons.getChildren().add(play);
        buttons.getChildren().add(champions);
        buttons.getChildren().add(quit);
        buttons.setLayoutX(490);
        buttons.setLayoutY(300);
        buttons.setSpacing(20);


        Group root = new Group();
        root.getChildren().add(logo_view);
        root.getChildren().add(buttons);

        Scene scene = new Scene(root, 1280,720, Color.rgb(33,41,50));

        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
        main.setResizable(false);
        main.setX((screenSize.getWidth() / 2) - 640);
        main.setY((screenSize.getHeight() / 2) - 360);
        main.setTitle("Marvel Ultimate War");
        main.setScene(scene);
        main.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
