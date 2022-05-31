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
    public static Scene create() {
        Player player1 = Control.getP1();
        Player player2 = Control.getP2();
        ArrayList<Champion> firstTeam = player1.getTeam();
        ArrayList<Champion> secondTeam = player2.getTeam();

        ArrayList<Image> firstTeamImages = new ArrayList<>();
        for (Champion champ : firstTeam) {
            Image img = new Image("views/assets/champions/%s.png".formatted(champ.getName()));
            firstTeamImages.add(img);
        }
        ArrayList<Image> secondTeamImages = new ArrayList<>();
        for (Champion champ : secondTeam) {
            Image img = new Image("views/assets/champions/%s.png".formatted(champ.getName()));
            secondTeamImages.add(img);
        }

        TilePane menu = new TilePane(Orientation.HORIZONTAL);
        Button quit = new Button("Quit");
        quit.setPrefSize(100,50);
        quit.setOnAction(e -> Control.onQuit());
        Button mainMenuBtn = new Button("Main Menu");
        mainMenuBtn.setPrefSize(100,50);
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
        for (Image img : firstTeamImages) {
            team1.getChildren().add(new ImageView(img));
        }
        for (Node n : team1.getChildren()) {
           ImageView iv = (ImageView) n;
           iv.setFitWidth(50);
           iv.setFitHeight(50);
        }
        profile1.getChildren().add(team1);

        VBox profile2 = new VBox();
        profile2.getChildren().add(new Label(player2.getName()));
        if (Control.getP2LeaderAbility())
            profile2.getChildren().add(new Label("Leader Ability Used"));
        else
            profile2.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team2 = new VBox();
        for (Image img : secondTeamImages) {
            team2.getChildren().add(new ImageView(img));
        }
        for (Node n : team2.getChildren()) {
            ImageView iv = (ImageView) n;
            iv.setFitWidth(50);
            iv.setFitHeight(50);
        }
        profile2.getChildren().add(team2);

        TilePane profiles = new TilePane(Orientation.VERTICAL);
        profiles.getChildren().add(profile1);
        profiles.getChildren().add(profile2);
        profiles.setVgap(20);
        profiles.setPrefWidth(390);

        GridPane board = new GridPane();
        board.setGridLinesVisible(true);
        for (int i = 0; i < 5; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 5);
            board.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 5);
            board.getRowConstraints().add(rowConst);
        }
        ArrayList<ImageView> boardImages = new ArrayList<>();
        for (Object[] o : Control.getBoard()) {
            for (Object tile : o) {
                if (tile != null) {
                    if (tile instanceof Champion) {
                        Champion ch = (Champion) tile;
                        ImageView iv = new ImageView(new Image("views/assets/champions/%s.png".formatted(ch.getName())));
                        boardImages.add(iv);
                        GridPane.setConstraints(iv, ch.getLocation().y, 4 - ch.getLocation().x);
                        GridPane.setHalignment(iv, HPos.CENTER);
                    } else if (tile instanceof Cover){
                        Cover cv = (Cover) tile;
                        ImageView iv = new ImageView(new Image("views/assets/champions/wall.png"));
                        boardImages.add(iv);
                        GridPane.setConstraints(iv, cv.getLocation().y, 4 - cv.getLocation().x);
                        GridPane.setHalignment(iv, HPos.CENTER);
                    }
                }
            }
        }
        board.getChildren().addAll(boardImages);

        VBox currentChampInfo = new VBox();
        Champion currentChamp = Control.getCurrentChampion();
        ImageView champView = new ImageView(new Image("views/assets/champions/%s.png".formatted(currentChamp.getName())));
        VBox nameBox = new VBox();
        nameBox.getChildren().add(new Label(currentChamp.getName()));
        if (currentChamp instanceof Hero) {
            nameBox.getChildren().add(new Label("Hero"));
        } else if (currentChamp instanceof Villain) {
            nameBox.getChildren().add(new Label("Villain"));
        } else if (currentChamp instanceof AntiHero) {
            nameBox.getChildren().add(new Label("AntiHero"));
        }
        nameBox.setAlignment(Pos.CENTER);
        HBox topBox = new HBox();
        topBox.getChildren().add(champView);
        topBox.getChildren().add(nameBox);
        currentChampInfo.getChildren().add(topBox);
        currentChampInfo.getChildren().add(new Label("HP: " + currentChamp.getCurrentHP() + "/" + currentChamp.getMaxHP()));
        GridPane stats = new GridPane();
        stats.add(new Label("Mana: " + currentChamp.getMana()), 0, 0, 1, 1);
        stats.add(new Label("Actions: " + currentChamp.getCurrentActionPoints() + "/" + currentChamp.getMaxActionPointsPerTurn()), 1, 0, 1, 1);
        stats.add(new Label("Damage: " + currentChamp.getAttackDamage()), 0, 1, 1,1);
        stats.add(new Label("Range: " + currentChamp.getAttackRange()), 1, 1, 1, 1);
        stats.setVgap(5);
        stats.setHgap(5);
        currentChampInfo.getChildren().add(stats);
        currentChampInfo.setAlignment(Pos.CENTER);
        TilePane right = new TilePane();
        right.getChildren().add(currentChampInfo);
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

        BorderPane root = new BorderPane(board, menu, right, turn, profiles);
        root.setPadding(new Insets(0, 10, 0, 10));
        return new Scene(root, 1280, 720);
    }
}
