package views.gui;

import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
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
        stage.setX((screenWidth - 1600f) / 2);
        stage.setY((screenHeight - 900f) / 2);
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

    public static void onGameOver(Player winner) {
        stage.setScene(GameOver.create(winner));
    }

    public static void popUp(Exception e) {
        Popup popUp = new Popup();
        VBox popUpContainer = new VBox();
        javafx.scene.control.Label exceptionMessage = new javafx.scene.control.Label(e.getMessage());
        javafx.scene.control.Label exceptionTitle = new Label("Oops!");
        exceptionMessage.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: 700");
        exceptionTitle.setStyle("-fx-text-fill: gold; -fx-font-size: 22; -fx-font-weight: 700;-fx-underline: true;");
        popUpContainer.getChildren().add(exceptionTitle);
        popUpContainer.getChildren().add(exceptionMessage);
        popUpContainer.setPrefSize(450,300);
        popUpContainer.setStyle("-fx-background-color: rgb(0,0,0,0.8)");
        popUp.getContent().add(popUpContainer);
        popUp.setAutoHide(true);
        popUp.show(GameApp.getStage());
    }

    public static Stage getStage() {
        return stage;
    }
}