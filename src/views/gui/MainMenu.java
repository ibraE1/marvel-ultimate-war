package views.gui;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;

public class MainMenu extends Application {

    public MainMenu () throws Exception {
        Stage main = new Stage();
        start(main);
    }

    @Override
    public void start(Stage main) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image icon = new Image("views/assets/icon.png");

        Group root = new Group();


        Scene scene = new Scene(root, 1280,720, Color.rgb(36,36,38));

        main.setResizable(false);
        main.setX((screenSize.getWidth() / 2) - 640);
        main.setY((screenSize.getHeight() / 2) - 360);

        main.getIcons().add(icon);
        main.setTitle("Marvel Ultimate War");

        main.setScene(scene);
        main.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
