package views.gui;

import javafx.application.Application;
import javafx.css.Stylesheet;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class Control extends Application {
    private static Scene scene;
    private static final Stage main = new Stage();

    //    public Stylesheet addStyles() {
//        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
//        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("championScreenStyle.css")).toExternalForm());
//    }


    public static void onStart() {
        scene = EnterPlayerNames.createStart();
        main.setScene(scene);
    }
    public static void onPlay() throws Exception {
        scene = DisplayChampions.createDisplayChampions();
        main.setScene(scene);
    }
    public static void onMainMenu() {
        scene = MainMenu.createMain();
        main.setScene(scene);
    }
    public static void onQuit() {
        System.exit(0);
    }
    @Override
    public void start(Stage Ignore) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image icon = new Image("views/assets/icon.png");
        main.getIcons().add(icon);
        Image logo = new Image("views/assets/logo.png");
        ImageView logo_view = new ImageView(logo);
        logo_view.setX(440);
        logo_view.setY(20);
        logo_view.setFitWidth(400);
        logo_view.setPreserveRatio(true);
        onMainMenu();
        main.setResizable(false);
        main.setX((screenSize.getWidth() / 2) - 640);
        main.setY((screenSize.getHeight() / 2) - 360);
        main.setTitle("Marvel Ultimate War");
        main.show();
    }
}
