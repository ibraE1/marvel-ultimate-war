package views.gui;

import engine.Game;
import engine.Player;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

import javax.security.auth.callback.LanguageCallback;
import java.io.IOException;
import java.util.ArrayList;

import static engine.Game.getAvailableChampions;

public class DisplayChampions {
    private static int playerTurn = 0;
    private static final Player player1 = Control.getP1();
    private static final Player player2 = Control.getP2();
    private static final ArrayList<Champion> chosenChampions = new ArrayList<>();
    public static void chooseChampions(Button btn, int i) {
        ArrayList<Champion> availableChampions = getAvailableChampions();
        if (playerTurn % 2 == 0) {
            if (playerTurn == 0) {
                player1.setLeader(availableChampions.get(i));
            }
            player1.getTeam().add(availableChampions.get(i));
            btn.setDisable(true);
            chosenChampions.add(availableChampions.get(i));

        } else {
            if (playerTurn == 1){
                player2.setLeader(availableChampions.get(i));
            }
            player2.getTeam().add(availableChampions.get(i));
            btn.setDisable(true);
            chosenChampions.add(availableChampions.get(i));
        }
    }
    public static Scene createDisplayChampions() {
        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();
        ArrayList<Pane> pnArray = new ArrayList<>();

        Color transparentBlack = new Color(0,0,0,0.4);

        for (int i = 1; i <= 15; i++) {
            String imgPath = "views/assets/champions/%s.png".formatted(i);
            Image img = new Image(imgPath, 80, 80, false, true, true);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);
        gp.setLayoutX(15);
        gp.setLayoutY(40);

        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button();
                btn.setPrefSize(80,80);
                btn.setGraphic(icons.get(counter));
                btn.styleProperty().bind(Bindings.when(btn.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);")
                        .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                                ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));" ));
                gp.add(btn, i, j);
                avatars.add(btn);
                counter++;
            }
        }

        Polygon triangleUp = new Polygon();
        triangleUp.getPoints().setAll(
                800d, 45d,
                800d, 350d,
                1400d,45d
        );
        triangleUp.setFill(transparentBlack);

        Polygon triangleDown = new Polygon();
        triangleDown.getPoints().setAll(
                810d, 350d,
                1410d, 350d,
                1410d,45d
        );
        triangleDown.setFill(transparentBlack);

        Line hl = new Line();
        hl.setStartX(465);
        hl.setStartY(405);
        hl.setEndX(710);
        hl.setEndY(405);
        hl.setStroke(Color.WHITE);
        hl.setStrokeWidth(2);
        hl.setOpacity(0.6);

        Line vl = new Line();
        vl.setStartX(355);
        vl.setStartY(540);
        vl.setEndX(355);
        vl.setEndY(850);
        vl.setStroke(Color.WHITE);
        vl.setStrokeWidth(2);
        vl.setOpacity(0.6);

        VBox nameBox = new VBox();
        HBox statsParent = new HBox();
        VBox vBoxLeft = new VBox();
        VBox stats = new VBox();
        VBox vBoxRight = new VBox();
        VBox abilityDisplay = new VBox();
        VBox abilityTitleBox = new VBox();
        VBox abilityDisplayBox = new VBox();
        Pane champPreview = new Pane();
        Pane statGraph = new Pane();
        VBox nameBoxCont = new VBox();
        HBox player1Team = new HBox();
        HBox player2Team = new HBox();

        Button ready = new Button("Ready");
        statsParent.setId("stats-parent");

        VBox numbers = new VBox();

        for (int count = 0; count < avatars.size(); count++) {
            int i = count;
            ArrayList<Champion> availableChampions = getAvailableChampions();
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED , e -> {
                nameBox.getChildren().clear();
                stats.getChildren().clear();
                abilityDisplay.getChildren().clear();
                nameBoxCont.getChildren().clear();
                statGraph.getChildren().clear();
                abilityTitleBox.getChildren().clear();
                champPreview.getChildren().clear();
                numbers.getChildren().clear();

                double hpPercent = (availableChampions.get(i).getMaxHP() / 2250d) * 150;
                double manaPercent = (availableChampions.get(i).getMana() / 1500d) * 150;
                double actionsPercent = (availableChampions.get(i).getMaxActionPointsPerTurn() / 8d) * 150;
                double speedPercent = (availableChampions.get(i).getSpeed() / 99d) * 150;
                double rngPercent = (availableChampions.get(i).getAttackRange() / 3d) * 150;
                double dmgPercent = (availableChampions.get(i).getAttackDamage() / 200d) * 150;

                Rectangle hpBar = new Rectangle(hpPercent, 10,Color.WHITE);
                Rectangle speedBar = new Rectangle(speedPercent, 10,Color.WHITE);
                Rectangle manaBar = new Rectangle(manaPercent, 10,Color.WHITE);
                Rectangle damageBar = new Rectangle(dmgPercent, 10,Color.WHITE);
                Rectangle rangeBar = new Rectangle(rngPercent, 10,Color.WHITE);
                Rectangle pointsBar = new Rectangle(actionsPercent, 10,Color.WHITE);

                Label nameL = new Label(availableChampions.get(i).getName());
                nameL.setStyle("-fx-text-fill: white");
                nameBox.getChildren().add(nameL);


                String type = "";
                if (availableChampions.get(i) instanceof Hero) {
                    type = "Hero";
                } else if (availableChampions.get(i) instanceof Villain) {
                    type = "Villain";
                } else if (availableChampions.get(i) instanceof AntiHero) {
                    type = "Anti-Hero";
                }

                Label typeL = new Label(type);
                typeL.setStyle("-fx-font-size: 32;-fx-text-fill: white;");
                nameBoxCont.getChildren().add(nameBox);
                nameBoxCont.getChildren().add(typeL);

                Label abilityTitle = new Label("Champion Abilities");
                abilityTitle.setStyle("-fx-font-size: 32; -fx-text-fill: white");
                abilityTitleBox.getChildren().add(abilityTitle);

                stats.setId("vbox-left");
                abilityTitle.setId("ability-title");
                abilityDisplay.setId("vbox-bottom");

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

                Label l1 = new Label(Integer.toString(availableChampions.get(i).getMaxHP()));
                l1.setStyle("-fx-text-fill: white");
                numbers.getChildren().add(l1);

                Label l2 = new Label(Integer.toString(availableChampions.get(i).getSpeed()));
                l2.setStyle("-fx-text-fill: white");
                numbers.getChildren().add(l2);

                Label l3 = new Label(Integer.toString(availableChampions.get(i).getMana()));
                l3.setStyle("-fx-text-fill: white");
                numbers.getChildren().add(l3);

                Label l4 = new Label(Integer.toString(availableChampions.get(i).getAttackDamage()));
                l4.setStyle("-fx-text-fill: white");
                numbers.getChildren().add(l4);

                Label l5 = new Label(Integer.toString(availableChampions.get(i).getAttackRange()));
                l5.setStyle("-fx-text-fill: white");
                numbers.getChildren().add(l5);

                Label l6 = new Label(Integer.toString(availableChampions.get(i).getMaxActionPointsPerTurn()));
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

                Label ab1 = new Label(availableChampions.get(i).getAbilities().get(0).getName());
                ab1.setStyle("-fx-text-fill: white");
                Label ab2 = new Label(availableChampions.get(i).getAbilities().get(1).getName());
                ab2.setStyle("-fx-text-fill: white");
                Label ab3 = new Label(availableChampions.get(i).getAbilities().get(2).getName());
                ab3.setStyle("-fx-text-fill: white");

                abilityDisplay.getChildren().add(ab1);
                abilityDisplay.getChildren().add(ab2);
                abilityDisplay.getChildren().add(ab3);

                Image champDisplay = new Image("views/assets/champions-full/%s.png".formatted(i));
                ImageView champDisplayView = new ImageView(champDisplay);
                champDisplayView.setFitHeight(500);
                champDisplayView.setPreserveRatio(true);
                champPreview.getChildren().add(champDisplayView);


                Image chart = new Image("views/assets/charts/%s.png".formatted(i));
                ImageView chart_view = new ImageView(chart);
                chart_view.setFitWidth(280);
                chart_view.setPreserveRatio(true);
                BorderPane bp = new BorderPane();
                bp.setPrefSize(300,300);
                bp.setCenter(chart_view);
                statGraph.getChildren().add(bp);

            });
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                chooseChampions(avatars.get(i), i);
                Image icon = new Image("views/assets/champions/%s.png".formatted(getAvailableChampions().get(i).getName()));
                ImageView icon_view = new ImageView(icon);
                icon_view.setFitWidth(80);
                Pane pn = new Pane();
                pn.setPrefSize(80,80);
                pn.getChildren().add(icon_view);
                pn.setStyle("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));");

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
                    ready.setDisable(false);
                }
            });
        }

        Button back = new Button("Main Menu");
        back.setPrefSize(150,40);
        back.setLayoutX(1430);
        back.setLayoutY(15);
        back.setFont(Font.font("Arial", 18));
        back.styleProperty().bind(Bindings.when(back.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        back.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            playerTurn = 0;
            Control.onMainMenu();
        });


        ready.setPrefSize(250,85);
        ready.setLayoutX(1330);
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
                Control.onReady();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
            System.out.println("\nPlayer 1: " + EnterPlayerNames.getPlayer1()+ "and Player 2: " + EnterPlayerNames.getPlayer2());
            System.out.println(playerTurn);
        });

        statsParent.setLayoutX(25);
        statsParent.setLayoutY(360);
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

        champPreview.setPrefSize(300,500);
        champPreview.setLayoutX(780);
        champPreview.setLayoutY(380);

        numbers.setSpacing(30);
        numbers.setLayoutX(370);
        numbers.setLayoutY(540);
        numbers.setStyle("-fx-font-size: 20px;");
        numbers.setAlignment(Pos.CENTER);

        player1Team.setSpacing(22);
        player2Team.setSpacing(22);

        player1Team.setLayoutX(850);
        player1Team.setLayoutY(80);
        player2Team.setLayoutX(1076);
        player2Team.setLayoutY(235);

        HBox iconsContainer1 = new HBox();
        HBox iconsContainer2 = new HBox();
        HBox iconsContainer3 = new HBox();
        HBox iconsContainer4 = new HBox();
        HBox iconsContainer5 = new HBox();
        HBox iconsContainer6 = new HBox();

        iconsContainer1.setPrefSize(80,80);
        iconsContainer2.setPrefSize(80,80);
        iconsContainer3.setPrefSize(80,80);
        iconsContainer4.setPrefSize(80,80);
        iconsContainer5.setPrefSize(80,80);
        iconsContainer6.setPrefSize(80,80);

        iconsContainer1.setLayoutX(850);
        iconsContainer1.setLayoutY(80);

        iconsContainer2.setLayoutX(952);
        iconsContainer2.setLayoutY(80);

        iconsContainer3.setLayoutX(1054);
        iconsContainer3.setLayoutY(80);

        iconsContainer4.setLayoutX(1076);
        iconsContainer4.setLayoutY(235);

        iconsContainer5.setLayoutX(1178);
        iconsContainer5.setLayoutY(235);

        iconsContainer6.setLayoutX(1280);
        iconsContainer6.setLayoutY(235);

        iconsContainer1.setStyle("-fx-border-radius: 3px; -fx-border-color: white");
        iconsContainer2.setStyle("-fx-border-radius: 3px; -fx-border-color: white");
        iconsContainer3.setStyle("-fx-border-radius: 3px; -fx-border-color: white");
        iconsContainer4.setStyle("-fx-border-radius: 3px; -fx-border-color: white");
        iconsContainer5.setStyle("-fx-border-radius: 3px; -fx-border-color: white");
        iconsContainer6.setStyle("-fx-border-radius: 3px; -fx-border-color: white");

        Label playerOne = new Label("Player 1");
        Label playerTwo = new Label("Player 2");
        Label playerName1 = new Label(player1.getName());
        Label playerName2 = new Label(player2.getName());

        playerOne.setStyle("-fx-text-fill: white; -fx-font-size: 30;");
        playerTwo.setStyle("-fx-text-fill: white; -fx-font-size: 30;");

        playerName1.setStyle("-fx-text-fill: white; -fx-font-size: 22;");
        playerName2.setStyle("-fx-text-fill: white; -fx-font-size: 22;");

        VBox player1Box = new VBox();
        VBox player2Box = new VBox();

        player1Box.getChildren().add(playerOne);
        player1Box.getChildren().add(playerName1);
        player2Box.getChildren().add(playerTwo);
        player2Box.getChildren().add(playerName2);

        player1Box.setLayoutX(850);
        player1Box.setLayoutY(200);

        player2Box.setLayoutX(1280);
        player2Box.setLayoutY(130);

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(ready);
        root.getChildren().add(back);
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

        return new Scene(root, 1600,900, Color.rgb(33,41,50));
    }

    public static void setPlayerTurn(int playerTurn) {
        DisplayChampions.playerTurn = playerTurn;
    }
}
