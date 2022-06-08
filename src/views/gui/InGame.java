package views.gui;

import engine.Game;
import engine.Player;
import exceptions.*;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.abilities.*;
import model.effects.Effect;
import model.world.*;

import java.util.ArrayList;

import static views.gui.GameApp.popUp;

public class InGame {

    static boolean singleTarget = false;
    static int index;
    static Player player1;
    static Player player2;
    static Button[][] gameBoard = new Button[5][5];

    public static Scene create(Game newGame) {
        player1 = newGame.getFirstPlayer();
        player2 = newGame.getSecondPlayer();

        TilePane menu = new TilePane(Orientation.HORIZONTAL);
        Button quit = new Button("Quit");
        quit.setPrefSize(100, 50);
        quit.setOnAction(e -> GameApp.onQuit());
        menu.getChildren().add(quit);
        ToggleButton attack = new ToggleButton("Attack");
        attack.setPrefSize(100, 50);
        menu.getChildren().add(attack);

        TabPane profiles = new TabPane();
        profiles.getTabs().add(new Tab(player1.getName(), createProfile(player1, newGame, 1)));
        profiles.getTabs().add(new Tab(player2.getName(), createProfile(player2, newGame, 2)));
        profiles.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        profiles.addEventFilter(KeyEvent.ANY, Event::consume);
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
        board.getChildren().addAll(createBoard(newGame));

        VBox right = new VBox();
        right.getChildren().add(currentChampInfo(newGame));
        right.setPrefWidth(400);
        right.setPadding(new Insets(0, 10, 0, 0));

        TilePane turn = new TilePane(Orientation.HORIZONTAL);
        ArrayList<ImageView> turnImages = new ArrayList<>();
        /*PriorityQueue pq = newGame.getTurnOrder();
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
        */
        turn.setPrefHeight(160);
        turn.setAlignment(Pos.CENTER);
        turn.setPrefTileWidth(40);

        Button leaderAbility = new Button("Use Leader Ability");
        leaderAbility.setPrefHeight(50);
        leaderAbility.setOnAction(e -> {
            try {
                newGame.useLeaderAbility();
            } catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException ex) {
                popUp(ex);
            }
            board.getChildren().clear();
            board.getChildren().addAll(createBoard(newGame));
            profiles.getTabs().clear();
            profiles.getTabs().add(new Tab("Player 1", createProfile(player1, newGame, 1)));
            profiles.getTabs().add(new Tab("Player 2", createProfile(player2, newGame, 2)));
        });
        menu.getChildren().add(leaderAbility);

        BorderPane root = new BorderPane(board, menu, right, turn, profiles);
        root.setPadding(new Insets(0, 10, 0, 10));
        root.addEventFilter(KeyEvent.KEY_RELEASED, key -> {
            if (!attack.isSelected()) {
                handleMove(key, newGame);
            } else if (attack.isSelected()){
                attack.selectedProperty().setValue(false);
                handleAttack(key, newGame);
            }
            try {
                handleAbility(key, newGame);
            } catch (AbilityUseException | CloneNotSupportedException | NotEnoughResourcesException e) {
                popUp(e);
            }
            if (newGame.checkGameOver() != null)
                GameApp.onGameOver(newGame.checkGameOver());
            if (newGame.getCurrentChampion().getCurrentActionPoints() == 0) {
                newGame.endTurn();
                right.getChildren().clear();
                right.getChildren().add(currentChampInfo(newGame));
            }
            board.getChildren().clear();
            board.getChildren().addAll(createBoard(newGame));
            profiles.getTabs().clear();
            profiles.getTabs().add(new Tab("Player 1", createProfile(player1, newGame, 1)));
            profiles.getTabs().add(new Tab("Player 2", createProfile(player2, newGame, 2)));
        });


        ArrayList<Ability> abs = newGame.getCurrentChampion().getAbilities();
        for (int k = 0; k < 5; k++) {
            System.out.println("a");
            for (int j = 0; j < 5; j++) {
                System.out.println("b");
                final int finalK = getButtonX(gameBoard[k][j]);
                final int finalJ = getButtonY(gameBoard[k][j]);

                gameBoard[k][j].addEventFilter(MouseEvent.MOUSE_CLICKED , e -> {
                    try {
                        if (singleTarget) {
                            newGame.castAbility(abs.get(index), finalK, finalJ);
                            singleTarget = false;
                        }
                    } catch (NotEnoughResourcesException | CloneNotSupportedException | InvalidTargetException |
                             AbilityUseException ex) {
                        popUp(ex);
                    }
                });
            }
        }


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

    private static VBox createProfile(Player player, Game newGame, int i) {
        VBox profile = new VBox();
        if (i == 1 && newGame.isFirstLeaderAbilityUsed())
            profile.getChildren().add(new Label("Leader Ability Used"));
        else if (i == 2 && newGame.isSecondLeaderAbilityUsed())
            profile.getChildren().add(new Label("Leader Ability Used"));
        else
            profile.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team = new VBox();
        for (Champion c : player.getTeam()) {
            team.getChildren().add(champInfo(c));
        }
        profile.getChildren().add(team);
        return profile;
    }

    public static int getButtonX(Button btn) {
        int count = 4;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == btn) {
                    return count;
                }
            }
            count--;
        }
        return 0;
    }

    public static int getButtonY(Button btn) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gameBoard[i][j] == btn) {
                    return j;
                }
            }
        }
        return 0;
    }

    private static ArrayList<Node> createBoard(Game newGame) {
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
                        gameBoard[i][j] = btn;
                    } else if (tile instanceof Cover) {
                        Cover cv = (Cover) tile;
                        ImageView iv = new ImageView(new Image("views/assets/champions/wall.png"));
                        Button btn = new Button();
                        btn.setGraphic(iv);
                        Tooltip tt = new Tooltip("HP: " + cv.getCurrentHP());
                        tt.setStyle("-fx-font: normal bold 12 Langdon; "
                                + "-fx-base: #AE3522; "
                                + "-fx-text-fill: orange;");
                        btn.setTooltip(tt);
                        GridPane.setConstraints(btn, cv.getLocation().y, 4 - cv.getLocation().x);
                        GridPane.setHalignment(btn, HPos.CENTER);
                        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        boardTiles.add(btn);
                        gameBoard[i][j] = btn;
                    }
                } else {
                    Button btn = new Button();
                    GridPane.setConstraints(btn, j, 4 - i);
                    btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    boardTiles.add(btn);
                    gameBoard[i][j] = btn;
                }
            }
        }
        return boardTiles;
    }

    private static VBox currentChampInfo(Game newGame) {
        VBox info = new VBox();
        Label l1 = new Label("Current Champ: ");
        l1.setPrefHeight(20);
        info.getChildren().add(l1);
        Label l2 = new Label("" + newGame.getCurrentChampion().getName());
        l2.setMaxHeight(20);
        info.getChildren().add(l2);
        info.getChildren().add(champAbilities(newGame.getCurrentChampion()));
        info.setAlignment(Pos.BASELINE_RIGHT);
        return info;
    }

    private static void handleMove(KeyEvent key, Game newGame) {
        switch (key.getCode()) {
            case UP:
                try {
                    newGame.move(Direction.UP);
                } catch (UnallowedMovementException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case DOWN:
                try {
                    newGame.move(Direction.DOWN);
                } catch (UnallowedMovementException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case LEFT:
                try {
                    newGame.move(Direction.LEFT);
                } catch (UnallowedMovementException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case RIGHT:
                try {
                    newGame.move(Direction.RIGHT);
                } catch (UnallowedMovementException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
        }
    }

    private static void handleAttack(KeyEvent key, Game newGame) {
        switch (key.getCode()) {
            case UP:
                try {
                    newGame.attack(Direction.UP);
                } catch (InvalidTargetException | ChampionDisarmedException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case DOWN:
                try {
                    newGame.attack(Direction.DOWN);
                } catch (InvalidTargetException | ChampionDisarmedException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case LEFT:
                try {
                    newGame.attack(Direction.LEFT);
                } catch (InvalidTargetException | ChampionDisarmedException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
            case RIGHT:
                try {
                    newGame.attack(Direction.RIGHT);
                } catch (InvalidTargetException | ChampionDisarmedException | NotEnoughResourcesException e) {
                    popUp(e);
                }
                break;
        }
    }
    public static void abilityHelper(Game newGame, int i) throws AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException {
        ArrayList<Ability> abs = newGame.getCurrentChampion().getAbilities();

        if (abs.get(i).getCastArea() == AreaOfEffect.SELFTARGET) {
            newGame.castAbility(abs.get(i));
        } else if (abs.get(i).getCastArea() == AreaOfEffect.TEAMTARGET) {
            newGame.castAbility(abs.get(i));
        } else if (abs.get(i).getCastArea() == AreaOfEffect.SURROUND) {
            newGame.castAbility(abs.get(i));
        } else if (abs.get(i).getCastArea() == AreaOfEffect.SINGLETARGET) {
            singleTarget = true;
            index = i;
        }
    }
    public static void handleAbility(KeyEvent key, Game newGame) throws AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException {
        switch (key.getCode()) {
            case DIGIT1 -> abilityHelper(newGame, 0);
            case DIGIT2 -> abilityHelper(newGame, 1);
            case DIGIT3 -> abilityHelper(newGame, 2);
        }
    }
}