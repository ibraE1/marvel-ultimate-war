package views.gui;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class AvailableChampions extends Application {
    public AvailableChampions () throws Exception {
        Stage championScreen = new Stage();
        start(championScreen);
    }

    @Override
    public void start(Stage championScreen) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Image icon = new Image("views/assets/icon.png");
        championScreen.getIcons().add(icon);

        GridPane gp = new GridPane();
        ArrayList<ImageView> icons = new ArrayList<>(15);

        for (int i = 1; i <= 15; i++){
            String  imgPath = "views/assets/champions/%s.png".formatted(i);
            Image img = new Image(imgPath);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button();
                btn.setPrefSize(80,100);
                btn.setGraphic(icons.get(counter));
                gp.add(btn, i, j);
                counter++;
            }
        }
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(50);
        gp.setLayoutY(100);

        Group root = new Group();
        root.getChildren().add(gp);

        Scene scene = new Scene(root, 1280,720, Color.rgb(33,41,50));
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        championScreen.setResizable(false);
        championScreen.setX((screenSize.getWidth() / 2) - 640);
        championScreen.setY((screenSize.getHeight() / 2) - 360);
        championScreen.setTitle("Marvel Ultimate War");
        championScreen.setScene(scene);
        championScreen.show();
    }
}
