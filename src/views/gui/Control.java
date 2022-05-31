package views.gui;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import javafx.application.Application;
import javafx.css.Stylesheet;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.world.Champion;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static engine.Game.loadAbilities;
import static engine.Game.loadChampions;

public class Control extends Application {

    private static Player p1;
    private static Player p2;
    private static Game newGame;
    private static Scene scene;
    private static final Stage main = new Stage();

//    public Stylesheet addStyles() {
//        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
//        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("championScreenStyle.css")).toExternalForm());
//    }

    public static void onMainMenu() {
        scene = MainMenu.createMain();
        main.setScene(scene);
    }
    public static void onStart() {
        scene = EnterPlayerNames.createStart();
        main.setScene(scene);
    }
    public static void onPlay() throws Exception {
        loadAbilities("Abilities.csv");
        loadChampions("Champions.csv");

        p1 = new Player(EnterPlayerNames.getPlayer1());
        p2 = new Player(EnterPlayerNames.getPlayer2());
        scene = DisplayChampions.createDisplayChampions();
        main.setScene(scene);
    }

    public static void onReady() throws IOException {
        newGame = new Game(p1,p2);
        scene = InGame.create();
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

    public static Player getP1() {
        return p1;
    }

    public static Player getP2() {
        return p2;
    }

    public static boolean getP1LeaderAbility() {
        return newGame.isFirstLeaderAbilityUsed();
    }

    public static boolean getP2LeaderAbility() {
        return newGame.isSecondLeaderAbilityUsed();
    }

    public static Object[][] getBoard() {
        return newGame.getBoard();
    }

    public static PriorityQueue getTurnOrder() {
        return newGame.getTurnOrder();
    }

    public static Champion getCurrentChampion() {
        return newGame.getCurrentChampion();
    }
}

