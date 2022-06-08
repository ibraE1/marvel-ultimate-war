package views.gui;

import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

import static engine.Game.loadAbilities;
import static engine.Game.loadChampions;

public class GameApp extends Application {
    private static final Stage stage = new Stage();
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenHeight = screenSize.height;
    private final int screenWidth = screenSize.width;

    @Override
    public void start(Stage Ignore) throws IOException {
        stage.getIcons().add(new Image("views/assets/icon.png"));
        stage.setResizable(false);

        if (Screen.getScreens().size() > 1) {
            // Middle of secondary screen
            Rectangle2D bounds = Screen.getScreens().get(1).getVisualBounds();
            stage.setX(bounds.getMinX() + (bounds.getWidth() - 1600f) / 2);
            stage.setY(bounds.getMinY() + (bounds.getHeight() - 900f) / 2);
        } else {
            // Middle of primary screen
            stage.setX((screenWidth - 1600f) / 2);
            stage.setY((screenHeight - 900f) / 2);
        }

        stage.setTitle("Marvel Ultimate War");
        stage.setScene(MainMenu.create());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void onStart() {
        stage.setScene(EnterPlayerNames.create());
    }

    public static void onQuit() {
        System.exit(0);
    }

    public static void onPlay(String p1Name, String p2Name) throws Exception {
        loadAbilities("Abilities.csv");
        loadChampions("Champions.csv");
        Player player1 = new Player(p1Name);
        Player player2 = new Player(p2Name);
        stage.setScene(DisplayChampions.create(player1, player2));
        DisplayChampions.onHover(0);
    }

    public static void onReady(Player player1, Player player2) throws IOException {
        Game newGame = new Game(player1, player2);
        stage.setScene(InGame.create(newGame));
    }
}