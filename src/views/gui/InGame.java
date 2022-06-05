package views.gui;

import engine.Game;
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
import model.abilities.Ability;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.*;

import java.util.ArrayList;

public class InGame {
    static Player player1;
    static Player player2;

    public static Scene create(Game newGame) {
        player1 = newGame.getFirstPlayer();
        player2 = newGame.getSecondPlayer();
        ArrayList<Champion> firstTeam = player1.getTeam();
        ArrayList<Champion> secondTeam = player2.getTeam();

        TilePane menu = new TilePane(Orientation.HORIZONTAL);
        Button quit = new Button("Quit");
        quit.setPrefSize(100, 50);
        quit.setOnAction(e -> GameApp.onQuit());
        menu.getChildren().add(quit);

        VBox profile1 = new VBox();
        profile1.getChildren().add(new Label(player1.getName()));
        if (newGame.isFirstLeaderAbilityUsed())
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
        if (newGame.isSecondLeaderAbilityUsed())
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
        profiles.setPrefWidth(390);
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
        for (int i = 0; i < newGame.getBoard().length; i++) {
            for (int j = 0; j < newGame.getBoard()[i].length; j++) {
                Object tile = newGame.getBoard()[i][j];
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
                        btn.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 5px; -fx-border-radius: 3px");
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
                        btn.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 5px; -fx-border-radius: 3px");
                    }
                } else {
                    Button btn = new Button();
                    GridPane.setConstraints(btn, j, 4 - i);
                    btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    boardTiles.add(btn);
                    btn.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 5px; -fx-border-radius: 3px");
                }
            }
        }
        board.getChildren().addAll(boardTiles);

        VBox right = new VBox();
        Label l1 = new Label("Current Champ: ");
        l1.setPrefHeight(20);
        right.getChildren().add(l1);
        Label l2 = new Label("" + newGame.getCurrentChampion().getName());
        l2.setMaxHeight(20);
        right.getChildren().add(l2);
        right.getChildren().add(champAbilities(newGame.getCurrentChampion()));
        right.setPrefWidth(400);
        right.setAlignment(Pos.BASELINE_RIGHT);
        right.setPadding(new Insets(0, 10, 0, 0));

        TilePane turn = new TilePane(Orientation.HORIZONTAL);
        ArrayList<ImageView> turnImages = new ArrayList<>();
        PriorityQueue pq = newGame.getTurnOrder();
        int size = newGame.getTurnOrder().size();
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
        return new Scene(root, 1600, 900);
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
        TilePane effects = new TilePane(Orientation.HORIZONTAL);
        effects.getChildren().add(new Label("Applied Effects: "));
        for (Effect eft : c.getAppliedEffects())
            effects.getChildren().add(new Label(eft.getName() + " (" + eft.getDuration() + ") "));
        currentChampInfo.getChildren().add(effects);
        currentChampInfo.setAlignment(Pos.CENTER_LEFT);
        return currentChampInfo;
    }

    private static VBox champAbilities(Champion c) {
        VBox currentChampAbilities = new VBox();
        for (Ability abt : c.getAbilities()) {
            VBox abilityInfo = new VBox();
            String name = "" + abt.getName();
            String value = "";
            if (abt instanceof DamagingAbility) {
                name += " (Damaging)";
                value += "Damage Amount: " + ((DamagingAbility) abt).getDamageAmount();
            } else if (abt instanceof HealingAbility) {
                name += " (Healing)";
                value += "Heal Amount: " + ((HealingAbility) abt).getHealAmount();
            } else if (abt instanceof CrowdControlAbility) {
                name += " (CC)";
                value += "Effect: " + ((CrowdControlAbility) abt).getEffect().getName();
            }
            abilityInfo.getChildren().add(new Label(name));
            abilityInfo.getChildren().add(new Label(value));
            abilityInfo.getChildren().add(new Label("AoE: " + abt.getCastArea()));
            abilityInfo.getChildren().add(new Label("Range: " + abt.getCastRange()));
            abilityInfo.getChildren().add(new Label("Mana Cost: " + abt.getManaCost()));
            abilityInfo.getChildren().add(new Label("AP Cost: " + abt.getRequiredActionPoints()));
            abilityInfo.getChildren().add(new Label("Cooldown: " + abt.getCurrentCooldown() + "/" + abt.getBaseCooldown()));
            abilityInfo.setAlignment(Pos.TOP_RIGHT);
            currentChampAbilities.getChildren().add(abilityInfo);
        }
        currentChampAbilities.setAlignment(Pos.TOP_RIGHT);
        return currentChampAbilities;
    }
}