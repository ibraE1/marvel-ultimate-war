package views.gui;

import engine.Player;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import model.abilities.Ability;
import model.world.AntiHero;
import model.world.Hero;
import model.world.Villain;

import java.io.IOException;
import java.util.ArrayList;

import static engine.Game.getAvailableChampions;

public class DisplayChampions {

    private static final Button ready = new Button("Ready");
    private static final HBox iconsContainer1 = new HBox();
    private static final HBox iconsContainer2 = new HBox();
    private static final HBox iconsContainer3 = new HBox();
    private static final HBox iconsContainer4 = new HBox();
    private static final HBox iconsContainer5 = new HBox();
    private static final HBox iconsContainer6 = new HBox();
    private static final ArrayList<HBox> iconsContainerArr1 = new ArrayList<>();
    private static final ArrayList<HBox> iconsContainerArr2 = new ArrayList<>();
    private static final Color transparentBlack = new Color(0,0,0,0.4);
    private static final Group root = new Group();
    private static final VBox nameBox = new VBox();
    private static final HBox statsParent = new HBox();
    private static final VBox vBoxLeft = new VBox();
    private static final VBox stats = new VBox();
    private static final VBox vBoxRight = new VBox();
    private static final VBox abilityDisplay = new VBox();
    private static final VBox abilityTitleBox = new VBox();
    private static final VBox abilityDisplayBox = new VBox();
    private static final Pane champPreview = new Pane();
    private static final Pane statGraph = new Pane();
    private static final VBox nameBoxCont = new VBox();
    private static final HBox player1Team = new HBox();
    private static final HBox player2Team = new HBox();
    private static final ArrayList<ImageView> champPrevArr = new ArrayList<>(15);
    private static final ArrayList<BorderPane> chartsArray = new ArrayList<>();
    private static int playerTurn = 0;
    private static Player player1;
    private static Player player2;
    private static String player1Name;
    private static String player2Name;
    private static final VBox numbers = new VBox();
    private static final VBox hintBox = new VBox();
    private static final VBox hintBoxContainer = new VBox();

    private static final Pane lead1Pane = new Pane();
    private static final Pane lead2Pane = new Pane();


    public static void chooseLeader() {
        for (int i = 0; i < 3; i++) {

            HBox actionSite1 = iconsContainerArr1.get(i);
            HBox actionSite2 = iconsContainerArr2.get(i);


            actionSite1.styleProperty().bind(Bindings.when(actionSite1.hoverProperty()).then("-fx-cursor: hand;-fx-border-radius: 8px; -fx-border-color: white")
                    .otherwise("-fx-cursor: default;-fx-border-radius: 8px; -fx-border-color: white"));

            actionSite2.styleProperty().bind(Bindings.when(actionSite2.hoverProperty()).then("-fx-cursor: hand;-fx-border-radius: 8px; -fx-border-color: white")
                    .otherwise("-fx-cursor: default;-fx-border-radius: 8px; -fx-border-color: white"));

            int finalI = i;
            actionSite1.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                if (player1.getLeader() == null) {
                    player1.setLeader(player1.getTeam().get(finalI));
                    root.getChildren().add(lead1Pane);
                    lead1Pane.setLayoutX(actionSite1.getLayoutX() + 27);
                    lead1Pane.setLayoutY(25);
                    showHint();
                    if (player2.getLeader() != null) {
                        ready.setDisable(false);
                    }
                }
            });
            actionSite2.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                if (player2.getLeader() == null) {
                    player2.setLeader(player2.getTeam().get(finalI));
                    root.getChildren().add(lead2Pane);
                    lead2Pane.setLayoutX(actionSite2.getLayoutX() + 27);
                    lead2Pane.setLayoutY(210);
                    showHint();
                    if (player1.getLeader() != null) {
                        ready.setDisable(false);
                    }
                }
            });
        }
    }

    public static void showHint() {
        hintBoxContainer.getChildren().clear();
        hintBox.getChildren().clear();
        hintBoxContainer.setPrefSize(400,200);
        hintBox.setPrefSize(360,200);
        hintBox.setBackground(Background.fill(transparentBlack));

        Image hint = new Image("views/assets/uatu.png");
        ImageView hint_view = new ImageView(hint);
        hint_view.setFitHeight(48);
        hint_view.setPreserveRatio(true);

        String hintString = "";

        if (playerTurn == 0) {
            hintString = "Each player has to choose 3 champions\n\n%s, choose a champion".formatted(player1Name);
        } else if (playerTurn == 1) {
            hintString = "%s, choose a champion".formatted(player2Name);
        } else if (playerTurn == 2) {
            hintString = "%s, choose another champion".formatted(player1Name);
        } else if (playerTurn == 3) {
            hintString = "%s, choose another champion".formatted(player2Name);
        } else if (playerTurn == 4) {
            hintString = "%s, choose one last champion".formatted(player1Name);
        } else if (playerTurn == 5) {
            hintString = "%s, choose one last champion".formatted(player2Name);
        } else if (playerTurn == 6) {
            if (player1.getLeader() == null || player2.getLeader() == null) {
                hintString = "Choose your leaders";
            } else {
                root.getChildren().remove(hintBoxContainer);
            }
        }

        Label hintTitle = new Label("Hint");
        Label hintDisplayed = new Label(hintString);

        VBox hintTitleLabelBox = new VBox(hintTitle);
        VBox hintDisplayedBox = new VBox(hintDisplayed);
        HBox hintTitleBox = new HBox();

        hintTitle.setStyle("-fx-font-size: 28; -fx-text-fill: gold; -fx-font-weight: 700;-fx-underline: true;");
        hintDisplayed.setStyle("-fx-font-size: 22; -fx-text-fill: white; -fx-font-weight: 700");

        hintTitleLabelBox.setPrefSize(hintTitleBox.getMaxWidth(),hintTitleBox.getMaxHeight());
        hintTitleLabelBox.setAlignment(Pos.CENTER_LEFT);

        hintDisplayedBox.setPrefWidth(555);
        hintDisplayedBox.setAlignment(Pos.CENTER);


        hintTitleBox.setLayoutX(555);
        hintTitleBox.setLayoutY(50);

        hintTitleBox.getChildren().add(hint_view);
        hintTitleBox.getChildren().add(hintTitleLabelBox);
        hintTitleBox.setSpacing(15);

        hintBox.setSpacing(15);
        hintBox.getChildren().add(hintTitleBox);
        hintBox.getChildren().add(hintDisplayedBox);

        hintBoxContainer.setAlignment(Pos.CENTER);
        hintBoxContainer.getChildren().add(hintBox);
        hintBoxContainer.setLayoutX(555);
        hintBoxContainer.setLayoutY(15);
    }

    public static void chooseChampions(Button btn, int i) {
        if (playerTurn % 2 == 0) {
            player1.getTeam().add(getAvailableChampions().get(i));
            btn.setDisable(true);
        } else {
            player2.getTeam().add(getAvailableChampions().get(i));
            btn.setDisable(true);
        }
    }

    public static void onHover(int i) {
        double hpPercent = (getAvailableChampions().get(i).getMaxHP() / 2250d) * 150;
        double manaPercent = (getAvailableChampions().get(i).getMana() / 1500d) * 150;
        double actionsPercent = (getAvailableChampions().get(i).getMaxActionPointsPerTurn() / 8d) * 150;
        double speedPercent = (getAvailableChampions().get(i).getSpeed() / 99d) * 150;
        double rngPercent = (getAvailableChampions().get(i).getAttackRange() / 3d) * 150;
        double dmgPercent = (getAvailableChampions().get(i).getAttackDamage() / 200d) * 150;

        Rectangle hpBar = new Rectangle(hpPercent, 10,Color.WHITE);
        Rectangle speedBar = new Rectangle(speedPercent, 10,Color.WHITE);
        Rectangle manaBar = new Rectangle(manaPercent, 10,Color.WHITE);
        Rectangle damageBar = new Rectangle(dmgPercent, 10,Color.WHITE);
        Rectangle rangeBar = new Rectangle(rngPercent, 10,Color.WHITE);
        Rectangle pointsBar = new Rectangle(actionsPercent, 10,Color.WHITE);

        Label nameL = new Label(getAvailableChampions().get(i).getName());
        nameL.setStyle("-fx-text-fill: white");
        nameBox.getChildren().add(nameL);


        String type = "";
        if (getAvailableChampions().get(i) instanceof Hero) {
            type = "Hero";
        } else if (getAvailableChampions().get(i) instanceof Villain) {
            type = "Villain";
        } else if (getAvailableChampions().get(i) instanceof AntiHero) {
            type = "Anti-Hero";
        }

        Label typeL = new Label(type);
        typeL.setStyle("-fx-font-size: 32;-fx-text-fill: white;");
        nameBoxCont.getChildren().add(nameBox);
        nameBoxCont.getChildren().add(typeL);

        Label abilityTitle = new Label("Champion Abilities");
        abilityTitle.setStyle("-fx-font-size: 32; -fx-text-fill: white");
        abilityTitleBox.getChildren().add(abilityTitle);

        HBox hpBox = new HBox();
        HBox speedBox = new HBox();
        HBox manaBox = new HBox();
        HBox dmgBox = new HBox();
        HBox rngBox = new HBox();
        HBox ptsBox = new HBox();

        Label stat1 = new Label("Health");
        stat1.setStyle("-fx-text-fill: white");
        Label stat2 = new Label("Speed");
        stat2.setStyle("-fx-text-fill: white");
        Label stat3 = new Label("Mana");
        stat3.setStyle("-fx-text-fill: white");
        Label stat4 = new Label("Damage");
        stat4.setStyle("-fx-text-fill: white");
        Label stat5 = new Label("Range");
        stat5.setStyle("-fx-text-fill: white");
        Label stat6 = new Label("Action Pts");
        stat6.setStyle("-fx-text-fill: white");

        Label l1 = new Label(Integer.toString(getAvailableChampions().get(i).getMaxHP()));
        l1.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l1);

        Label l2 = new Label(Integer.toString(getAvailableChampions().get(i).getSpeed()));
        l2.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l2);

        Label l3 = new Label(Integer.toString(getAvailableChampions().get(i).getMana()));
        l3.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l3);

        Label l4 = new Label(Integer.toString(getAvailableChampions().get(i).getAttackDamage()));
        l4.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l4);

        Label l5 = new Label(Integer.toString(getAvailableChampions().get(i).getAttackRange()));
        l5.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l5);

        Label l6 = new Label(Integer.toString(getAvailableChampions().get(i).getMaxActionPointsPerTurn()));
        l6.setStyle("-fx-text-fill: white");
        numbers.getChildren().add(l6);

        hpBox.getChildren().add(stat1);
        hpBox.getChildren().add(hpBar);

        speedBox.getChildren().add(stat2);
        speedBox.getChildren().add(speedBar);

        manaBox.getChildren().add(stat3);
        manaBox.getChildren().add(manaBar);

        dmgBox.getChildren().add(stat4);
        dmgBox.getChildren().add(damageBar);

        rngBox.getChildren().add(stat5);
        rngBox.getChildren().add(rangeBar);

        ptsBox.getChildren().add(stat6);
        ptsBox.getChildren().add(pointsBar);

        stats.getChildren().add(hpBox);
        stats.getChildren().add(speedBox);
        stats.getChildren().add(manaBox);
        stats.getChildren().add(dmgBox);
        stats.getChildren().add(rngBox);
        stats.getChildren().add(ptsBox);

        hpBox.setAlignment(Pos.CENTER_LEFT);
        hpBox.setSpacing(15);
        speedBox.setAlignment(Pos.CENTER_LEFT);
        speedBox.setSpacing(15);
        manaBox.setAlignment(Pos.CENTER_LEFT);
        manaBox.setSpacing(15);
        dmgBox.setAlignment(Pos.CENTER_LEFT);
        dmgBox.setSpacing(15);
        rngBox.setAlignment(Pos.CENTER_LEFT);
        rngBox.setSpacing(15);
        ptsBox.setAlignment(Pos.CENTER_LEFT);
        ptsBox.setSpacing(15);

        Ability currAb1 = getAvailableChampions().get(i).getAbilities().get(0);
        Ability currAb2 = getAvailableChampions().get(i).getAbilities().get(1);
        Ability currAb3 = getAvailableChampions().get(i).getAbilities().get(2);

        Label ab1 = new Label(currAb1.getName());
        ab1.setStyle("-fx-text-fill: white");
        ab1.setTooltip(new Tooltip("Mana Cost: %s\nCooldown: %s\nRange: %s\nArea: %s\nAction Pts: %s"
                .formatted(currAb1.getManaCost(), currAb1.getBaseCooldown(),currAb1.getCastRange(), currAb1.getCastArea(),currAb1.getRequiredActionPoints())));
        Label ab2 = new Label(currAb2.getName());
        ab2.setStyle("-fx-text-fill: white");
        ab2.setTooltip(new Tooltip("Mana Cost: %s\nCooldown: %s\nRange: %s\nArea: %s\nAction Pts: %s"
                .formatted(currAb2.getManaCost(), currAb2.getBaseCooldown(),currAb2.getCastRange(), currAb2.getCastArea(),currAb2.getRequiredActionPoints())));
        Label ab3 = new Label(currAb3.getName());
        ab3.setStyle("-fx-text-fill: white");
        ab3.setTooltip(new Tooltip("Mana Cost: %s\nCooldown: %s\nRange: %s\nArea: %s\nAction Pts: %s"
                .formatted(currAb3.getManaCost(), currAb3.getBaseCooldown(),currAb3.getCastRange(), currAb3.getCastArea(),currAb3.getRequiredActionPoints())));

        abilityDisplay.getChildren().add(ab1);
        abilityDisplay.getChildren().add(ab2);
        abilityDisplay.getChildren().add(ab3);


        champPreview.setPrefSize(300,500);
        champPreview.setLayoutX(780);
        champPreview.setLayoutY(400);

        if (i == 10) {
            champPreview.setLayoutY(345);
            champPreview.setLayoutX(850);
        }
        if (i == 14) {
            champPreview.setLayoutY(350);
            champPreview.setLayoutX(810);
        }
        if (i == 5) {
            champPreview.setLayoutX(760);
        }
        if (i == 8) {
            champPreview.setLayoutX(820);
            champPreview.setLayoutY(410);
        }
        if (i == 13 | i == 6 || i == 12) {
            champPreview.setLayoutX(840);
        }
        if (i == 12 || i == 8 || i == 13 ) {
            champPreview.setLayoutY(360);
        }
        if (i == 7) {
            champPreview.setLayoutX(750);
        }
        if (i == 4) {
            champPreview.setLayoutY(370);
        }
        if (i == 1) {
            champPreview.setLayoutY(410);
        }
        champPreview.getChildren().add(champPrevArr.get(i));
        statGraph.getChildren().add(chartsArray.get(i));
    }

    public static Scene create(Player player1, Player player2) {
        DisplayChampions.player1 = player1;
        DisplayChampions.player2 = player2;

        player1Name = player1.getName().substring(0,1).toUpperCase() + player1.getName().substring(1);
        player2Name = player2.getName().substring(0,1).toUpperCase() + player2.getName().substring(1);

        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Image chart = new Image("views/assets/charts/%s.png".formatted(i));
            ImageView chart_view = new ImageView(chart);
            chart_view.setFitWidth(280);
            chart_view.setPreserveRatio(true);
            BorderPane bp = new BorderPane();
            bp.setPrefSize(300,300);
            bp.setCenter(chart_view);
            chartsArray.add(bp);
        }

        for (int i = 0; i < 15; i++) {
            Image champDisplay = new Image("views/assets/champions-full/%s.png".formatted(i));
            ImageView champDisplayView = new ImageView(champDisplay);
            champDisplayView.setFitHeight(500);
            if (i == 2) {
                champDisplayView.setFitHeight(360);
            }
            if (i == 10 || i == 14) {
                champDisplayView.setFitHeight(550);
            }
            if (i == 4) {
                champDisplayView.setFitHeight(530);
            }
            if (i == 13) {
                champDisplayView.setFitHeight(520);
            }
            champDisplayView.setPreserveRatio(true);
            champPrevArr.add(champDisplayView);
        }

        for (int i = 0; i <= 14; i++) {
            String imgPath = "views/assets/champions/%s.png".formatted(getAvailableChampions().get(i).getName());
            Image img = new Image(imgPath, 80, 80, false, true, true);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.setLayoutX(15);
        gp.setLayoutY(15);

        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button();
                btn.setPrefSize(80,80);
                btn.setGraphic(icons.get(counter));
                btn.styleProperty().bind(Bindings.when(btn.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                                " -fx-scale-y: 1.1;-fx-background-color: rgb(0,0,0, 0.4); -fx-border-radius: 8px;")
                        .otherwise("-fx-background-color: rgb(0,0,0, 0.4);-fx-border-radius: 8px;"));
                gp.add(btn, i, j);
                avatars.add(btn);
                counter++;
            }
        }

        Polygon triangleUp = new Polygon();
        triangleUp.getPoints().setAll(
                970d, 15d,
                970d, 350d,
                1580d, 15d
        );
        triangleUp.setFill(transparentBlack);

        Polygon triangleDown = new Polygon();
        triangleDown.getPoints().setAll(
                980d, 350d,
                1590d, 350d,
                1590d, 15d
        );
        triangleDown.setFill(transparentBlack);


        Image vs = new Image("views/assets/vs.png");
        ImageView vs_view = new ImageView(vs);
        vs_view.setFitHeight(52);
        vs_view.setPreserveRatio(true);
        Pane vsPane = new Pane(vs_view);
        vsPane.setLayoutX(1254);
        vsPane.setLayoutY(149);

        Image lead1 = new Image("views/assets/leader.png");
        ImageView lead1_view = new ImageView(lead1);
        lead1_view.setFitHeight(26);
        lead1_view.setPreserveRatio(true);
        lead1Pane.getChildren().add(lead1_view);

        Image lead2 = new Image("views/assets/leader.png");
        ImageView lead2_view = new ImageView(lead2);
        lead2_view.setFitHeight(26);
        lead2_view.setPreserveRatio(true);
        lead2Pane.getChildren().add(lead2_view);

        Line hl = new Line();
        hl.setStartX(465);
        hl.setStartY(390);
        hl.setEndX(710);
        hl.setEndY(390);
        hl.setStroke(Color.WHITE);
        hl.setStrokeWidth(2);
        hl.setOpacity(0.6);

        Line vl = new Line();
        vl.setStartX(355);
        vl.setStartY(525);
        vl.setEndX(355);
        vl.setEndY(835);
        vl.setStroke(Color.WHITE);
        vl.setStrokeWidth(2);
        vl.setOpacity(0.6);

        for (int count = 0; count < avatars.size(); count++) {
            int i = count;
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED , e -> {
                nameBox.getChildren().clear();
                stats.getChildren().clear();
                abilityDisplay.getChildren().clear();
                nameBoxCont.getChildren().clear();
                statGraph.getChildren().clear();
                abilityTitleBox.getChildren().clear();
                champPreview.getChildren().clear();
                numbers.getChildren().clear();
                onHover(i);
            });
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                chooseChampions(avatars.get(i), i);
                Image icon = new Image("views/assets/champions/%s.png".formatted(getAvailableChampions().get(i).getName()));
                ImageView icon_view = new ImageView(icon);
                icon_view.setFitWidth(80);
                icon_view.setFitHeight(80);

                Pane pn = new Pane();
                pn.setPrefSize(80,80);
                pn.getChildren().add(icon_view);
                pn.setStyle("-fx-border-radius: 8px;");

                if (playerTurn % 2 == 0) {
                    player1Team.getChildren().add(pn);
                } else {
                    player2Team.getChildren().add(pn);
                }

                playerTurn++;
                if (playerTurn == 6) {
                    for (Button b : avatars) {
                        b.setDisable(true);
                    }
                    chooseLeader();
                }
                showHint();
            });
        }

        ready.setPrefSize(250,85);
        ready.setLayoutX(1320);
        ready.setLayoutY(785);
        ready.styleProperty().bind(Bindings.when(ready.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        ready.setFont(Font.font("Arial", 32));
        ready.setDisable(true);

        ready.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                GameApp.onReady(player1, player2);
            } catch (IOException ex) {
                GameApp.popUp(ex);
            }
        });

        Button test = new Button("test");
        test.setPrefSize(75,25);
        test.setLayoutX(600);
        test.setLayoutY(0);
        test.setFont(Font.font("Georgia", 18));

        test.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.print("Leader 1: ");
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    System.out.print(player1.getLeader().getName() + "\t");
                } else {
                    System.out.print(player1.getTeam().get(i-1).getName() + " ");
                }
            }
            System.out.println();
            System.out.print("Leader 2: ");
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    System.out.print(player2.getLeader().getName() + "\t");
                } else {
                    System.out.print(player2.getTeam().get(i-1).getName() + ", ");
                }
            }
            System.out.println("\nPlayer 1: " + player1Name + "and Player 2: " + player2Name);
            System.out.println(playerTurn);
        });

        showHint();

        statsParent.setLayoutX(25);
        statsParent.setLayoutY(345);
        nameBoxCont.setPrefSize(400, 130);
        stats.setPrefSize(400, 380);
        vBoxRight.setPrefSize(300,410);
        abilityDisplay.setPrefSize(300,200);
        statGraph.setPrefSize(300,320);

        abilityDisplay.setAlignment(Pos.CENTER);
        abilityTitleBox.setAlignment(Pos.TOP_CENTER);
        stats.setAlignment(Pos.CENTER_LEFT);

        abilityDisplay.setSpacing(10);
        stats.setSpacing(20);

        nameBox.setStyle("-fx-font-size: 52;");
        nameBoxCont.setStyle("-fx-padding: 12px;");
        abilityDisplay.setStyle("-fx-font-size: 30");
        stats.setStyle("-fx-font-size: 30;-fx-padding: 12px;");

        nameBoxCont.setBackground(Background.fill(transparentBlack));
        stats.setBackground(Background.fill(transparentBlack));
        abilityDisplay.setBackground(Background.fill(transparentBlack));
        abilityTitleBox.setBackground(Background.fill(transparentBlack));
        statGraph.setBackground(Background.fill(transparentBlack));

        statsParent.setSpacing(15);
        vBoxLeft.setSpacing(15);
        vBoxRight.setSpacing(15);

        statsParent.getChildren().add(vBoxLeft);
        statsParent.getChildren().add(vBoxRight);
        abilityDisplayBox.getChildren().add(abilityTitleBox);
        abilityDisplayBox.getChildren().add(abilityDisplay);
        vBoxRight.getChildren().add(abilityDisplayBox);
        vBoxRight.getChildren().add(statGraph);
        vBoxLeft.getChildren().add(nameBoxCont);
        vBoxLeft.getChildren().add(stats);

        numbers.setSpacing(30);
        numbers.setLayoutX(370);
        numbers.setLayoutY(525);
        numbers.setStyle("-fx-font-size: 20px;");
        numbers.setAlignment(Pos.CENTER);

        player1Team.setSpacing(22);
        player2Team.setSpacing(22);

        player1Team.setLayoutX(1010);
        player1Team.setLayoutY(55);
        player2Team.setLayoutX(1266);
        player2Team.setLayoutY(240);

        iconsContainer1.setPrefSize(80,80);
        iconsContainer2.setPrefSize(80,80);
        iconsContainer3.setPrefSize(80,80);
        iconsContainer4.setPrefSize(80,80);
        iconsContainer5.setPrefSize(80,80);
        iconsContainer6.setPrefSize(80,80);

        iconsContainerArr1.add(iconsContainer1);
        iconsContainerArr1.add(iconsContainer2);
        iconsContainerArr1.add(iconsContainer3);
        iconsContainerArr2.add(iconsContainer4);
        iconsContainerArr2.add(iconsContainer5);
        iconsContainerArr2.add(iconsContainer6);


        iconsContainer1.setLayoutX(1010);
        iconsContainer1.setLayoutY(55);

        iconsContainer2.setLayoutX(1112);
        iconsContainer2.setLayoutY(55);

        iconsContainer3.setLayoutX(1214);
        iconsContainer3.setLayoutY(55);

        iconsContainer4.setLayoutX(1266);
        iconsContainer4.setLayoutY(240);

        iconsContainer5.setLayoutX(1368);
        iconsContainer5.setLayoutY(240);

        iconsContainer6.setLayoutX(1470);
        iconsContainer6.setLayoutY(240);

        iconsContainer1.setStyle("-fx-border-radius: 8px; -fx-border-color: white");
        iconsContainer2.setStyle("-fx-border-radius: 8px; -fx-border-color: white");
        iconsContainer3.setStyle("-fx-border-radius: 8px; -fx-border-color: white");
        iconsContainer4.setStyle("-fx-border-radius: 8px; -fx-border-color: white");
        iconsContainer5.setStyle("-fx-border-radius: 8px; -fx-border-color: white");
        iconsContainer6.setStyle("-fx-border-radius: 8px; -fx-border-color: white");

        Label playerOne = new Label("Player 1");
        Label playerTwo = new Label("Player 2");
        Label playerName1 = new Label(player1Name);
        Label playerName2 = new Label(player2Name);

        playerName1.setPrefWidth(180);
        playerName1.setWrapText(true);
        playerName1.setAlignment(Pos.TOP_LEFT);
        playerName2.setPrefWidth(180);
        playerName2.setWrapText(true);
        playerName2.setAlignment(Pos.TOP_LEFT);

        playerOne.setStyle("-fx-text-fill: white; -fx-font-size: 28;");
        playerTwo.setStyle("-fx-text-fill: white; -fx-font-size: 28;");

        playerName1.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
        playerName2.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        VBox player1Box = new VBox();
        VBox player2Box = new VBox();

        player1Box.getChildren().add(playerOne);
        player1Box.getChildren().add(playerName1);
        player2Box.getChildren().add(playerTwo);
        player2Box.getChildren().add(playerName2);

        player1Box.setLayoutX(1015);
        player1Box.setLayoutY(185);

        player2Box.setLayoutX(1465);
        player2Box.setLayoutY(135);

        root.getChildren().add(gp);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(ready);
        root.getChildren().add(triangleUp);
        root.getChildren().add(triangleDown);
        root.getChildren().add(hl);
        root.getChildren().add(vl);
        root.getChildren().add(numbers);
        root.getChildren().add(player1Team);
        root.getChildren().add(player2Team);
        root.getChildren().add(iconsContainer1);
        root.getChildren().add(iconsContainer2);
        root.getChildren().add(iconsContainer3);
        root.getChildren().add(iconsContainer4);
        root.getChildren().add(iconsContainer5);
        root.getChildren().add(iconsContainer6);
        root.getChildren().add(player1Box);
        root.getChildren().add(player2Box);
        root.getChildren().add(vsPane);
        root.getChildren().add(hintBoxContainer);
//        root.getChildren().add(test);

        return new Scene(root, 1600,900, Color.rgb(33,41,50));
    }
}
