package views.gui;

import engine.Player;
import engine.PriorityQueue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.world.*;

import java.util.ArrayList;

public class InGame {
    static Player player1 = Control.getP1();
    static Player player2 = Control.getP2();
    static ArrayList<Champion> firstTeam = player1.getTeam();
    static ArrayList<Champion> secondTeam = player2.getTeam();

    public static Scene create() {
        TilePane menu = new TilePane(Orientation.HORIZONTAL);
        Button quit = new Button("Quit");
        quit.setPrefSize(100, 50);
        quit.setOnAction(e -> Control.onQuit());
        Button mainMenuBtn = new Button("Main Menu");
        mainMenuBtn.setPrefSize(100, 50);
        mainMenuBtn.setOnAction(e -> Control.onMainMenu());
        menu.getChildren().add(quit);
        menu.getChildren().add(mainMenuBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(60);

        VBox profile1 = new VBox();
        profile1.getChildren().add(new Label(player1.getName()));
        if (Control.getP1LeaderAbility())
            profile1.getChildren().add(new Label("Leader Ability Used"));
        else
            profile1.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team1 = new VBox();
        for (Champion c : firstTeam) {
            team1.getChildren().add(champInfo(c));
        }
        profile1.getChildren().add(team1);

        VBox profile2 = new VBox();
        profile2.getChildren().add(new Label(player2.getName()));
        if (Control.getP2LeaderAbility())
            profile2.getChildren().add(new Label("Leader Ability Used"));
        else
            profile2.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team2 = new VBox();
        for (Champion c : secondTeam) {
            team2.getChildren().add(champInfo(c));
        }
        profile2.getChildren().add(team2);

        TilePane profiles = new TilePane(Orientation.HORIZONTAL);
        profiles.getChildren().add(profile1);
        profiles.getChildren().add(profile2);
        profiles.setHgap(15);
        profiles.setPrefWidth(380);
        profiles.setPadding(new Insets(0, 10, 0, 0));

        GridPane board = new GridPane();
        for (int i = 0; i < 5; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 5);
            colConst.setHgrow(Priority.ALWAYS);
            colConst.setFillWidth(true);
            board.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 5);
            rowConst.setVgrow(Priority.ALWAYS);
            rowConst.setFillHeight(true);
            board.getRowConstraints().add(rowConst);
        }
        ArrayList<Node> boardTiles = new ArrayList<>();
        for (int i = 0; i < Control.getBoard().length; i++) {
            for (int j = 0; j < Control.getBoard()[i].length; j++) {
                Object tile = Control.getBoard()[i][j];
                if (tile != null) {
                    if (tile instanceof Champion) {
                        Champion ch = (Champion) tile;
                        ImageView iv = new ImageView(new Image("views/assets/champions/%s.png".formatted(ch.getName())));
                        Button btn = new Button();
                        btn.setGraphic(iv);
                        GridPane.setConstraints(btn, ch.getLocation().y, 4 - ch.getLocation().x);
                        GridPane.setHalignment(btn, HPos.CENTER);
                        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        boardTiles.add(btn);
                    } else if (tile instanceof Cover) {
                        Cover cv = (Cover) tile;
                        ImageView iv = new ImageView(new Image("views/assets/champions/wall.png"));
                        VBox box = new VBox();
                        box.getChildren().add(new Label("HP: " + cv.getCurrentHP()));
                        Button btn = new Button();
                        btn.setGraphic(iv);
                        box.getChildren().add(btn);
                        box.setAlignment(Pos.CENTER);
                        GridPane.setConstraints(box, cv.getLocation().y, 4 - cv.getLocation().x);
                        GridPane.setHalignment(box, HPos.CENTER);
                        box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        boardTiles.add(box);
                    }
                } else {
                    Button btn = new Button();
                    GridPane.setConstraints(btn, j, 4 - i);
                    btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    boardTiles.add(btn);
                }
            }
        }
        board.getChildren().addAll(boardTiles);

        TilePane right = new TilePane(Orientation.VERTICAL);
        Label l1 = new Label("Current Champ: ");
        l1.setPrefHeight(20);
        right.getChildren().add(l1);
        right.getChildren().add(champInfo(Control.getCurrentChampion()));
        right.setPrefWidth(390);
        right.setAlignment(Pos.TOP_RIGHT);
        right.setPadding(new Insets(0, 10, 0, 0));

        TilePane turn = new TilePane(Orientation.HORIZONTAL);
        ArrayList<ImageView> turnImages = new ArrayList<>();
        PriorityQueue pq = Control.getTurnOrder();
        int size = Control.getTurnOrder().size();
        for (int i = 0; i < size && !pq.isEmpty(); i++) {
            Champion ch = (Champion) (pq.remove());
            Image img = new Image("views/assets/champions/%s.png".formatted(ch.getName()));
            ImageView iv = new ImageView(img);
            iv.setFitHeight(40);
            iv.setFitWidth(40);
            turnImages.add(iv);
        }
        for (ImageView iv : turnImages) {
            turn.getChildren().add(iv);
            turn.getChildren().add(new Label(">"));
        }
        turn.getChildren().remove(turn.getChildren().size() - 1);
        turn.setPrefHeight(160);
        turn.setAlignment(Pos.CENTER);
        turn.setPrefTileWidth(40);

        BorderPane root = new BorderPane(board, menu, right, turn, profiles);
        root.setPadding(new Insets(0, 10, 0, 10));
        return new Scene(root, 1280, 720);
    }

    private static VBox champInfo(Champion c) {
        VBox currentChampInfo = new VBox();
        ImageView champView = new ImageView(new Image("views/assets/champions/%s.png".formatted(c.getName())));
        champView.setFitWidth(40);
        champView.setFitHeight(40);
        VBox nameBox = new VBox();
        String title = (c == player1.getLeader() || c == player2.getLeader()) ? "(Leader) " + c.getName() : c.getName();
        nameBox.getChildren().add(new Label(title));
        if (c instanceof Hero) {
            nameBox.getChildren().add(new Label("Hero"));
        } else if (c instanceof Villain) {
            nameBox.getChildren().add(new Label("Villain"));
        } else if (c instanceof AntiHero) {
            nameBox.getChildren().add(new Label("AntiHero"));
        }
        nameBox.setAlignment(Pos.CENTER);
        HBox topBox = new HBox();
        topBox.getChildren().add(champView);
        topBox.getChildren().add(nameBox);
        topBox.setSpacing(5);
        currentChampInfo.getChildren().add(topBox);
        currentChampInfo.getChildren().add(new Label("HP: " + c.getCurrentHP() + "/" + c.getMaxHP()));
        GridPane stats = new GridPane();
        stats.add(new Label("Mana: " + c.getMana()), 0, 0, 1, 1);
        stats.add(new Label("Actions: " + c.getCurrentActionPoints() + "/" + c.getMaxActionPointsPerTurn()), 1, 0, 1, 1);
        stats.add(new Label("Damage: " + c.getAttackDamage()), 0, 1, 1, 1);
        stats.add(new Label("Range: " + c.getAttackRange()), 1, 1, 1, 1);
        stats.setVgap(5);
        stats.setHgap(5);
        currentChampInfo.getChildren().add(stats);
        //TilePane effects = new TilePane(Orientation.HORIZONTAL);
        //effects.getChildren().add(new Label("Applied Effects: "));
        //for (Effect eft : c.getAppliedEffects())
        //    effects.getChildren().add(new Label(eft.getName() + " (" + eft.getDuration() + ") "));
        //currentChampInfo.getChildren().add(effects);
        currentChampInfo.setAlignment(Pos.CENTER);
        currentChampInfo.setSpacing(5);
        return currentChampInfo;
    }
}
