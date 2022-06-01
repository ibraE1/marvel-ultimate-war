package views.gui;

import engine.Player;
import exceptions.NotEnoughChampionsException;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

import static engine.Game.*;

public class DisplayChampions {
    private static int playerTurn = 0;
    private static final Player player1 = Control.getP1();
    private static final Player player2 = Control.getP2();

    public static void chooseChampions(Button btn, int i) {
        ArrayList<Champion> availableChampions = getAvailableChampions();

        if (playerTurn % 2 == 0) {
            if (playerTurn == 0) {
                player1.setLeader(availableChampions.get(i));
            }
            player1.getTeam().add(availableChampions.get(i));
            btn.setDisable(true);

        } else {
            if (playerTurn == 1){
                player2.setLeader(availableChampions.get(i));
            }
            player2.getTeam().add(availableChampions.get(i));
            btn.setDisable(true);
        }
    }
    public static Scene createDisplayChampions() {
        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();
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
                                ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"));

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

        statsParent.setId("stats-parent");

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
                Label stat6 = new Label("Action Points");
                stat6.setStyle("-fx-text-fill: white");

                hpBox.getChildren().add(stat1); //.concat(Integer.toString(availableChampions.get(i).getMaxHP()))));
                hpBox.getChildren().add(hpBar);

                speedBox.getChildren().add(stat2); //.concat(Integer.toString(availableChampions.get(i).getSpeed()))));
                speedBox.getChildren().add(speedBar);

                manaBox.getChildren().add(stat3); //.concat(Integer.toString(availableChampions.get(i).getMana()))));
                manaBox.getChildren().add(manaBar);

                dmgBox.getChildren().add(stat4); //.concat(Integer.toString(availableChampions.get(i).getAttackDamage()))));
                dmgBox.getChildren().add(damageBar);

                rngBox.getChildren().add(stat5); //.concat(Integer.toString(availableChampions.get(i).getAttackRange()))));
                rngBox.getChildren().add(rangeBar);

                ptsBox.getChildren().add(stat6); //.concat(Integer.toString(availableChampions.get(i).getMaxActionPointsPerTurn()))));
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
                playerTurn++;
                if (playerTurn == 6) {
                    for (Button b : avatars) {
                        b.setDisable(true);
                    }
                }
            });
        }

        Button back = new Button("Main Menu");
        back.setPrefSize(150,40);
        back.setLayoutX(1520);
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

        Button ready = new Button("Ready");
        ready.setPrefSize(250,75);
        ready.setLayoutX(1410);
        ready.setLayoutY(820);
        ready.styleProperty().bind(Bindings.when(ready.hoverProperty()).then("-fx-cursor: hand; -fx-scale-x: 1.1;" +
                        " -fx-scale-y: 1.1;-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," +
                        " linear-gradient(#20262b, #191d22), radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9)," +
                        " rgba(255,255,255,0));-fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0;" +
                        "-fx-effect: dropshadow(three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1);-fx-text-fill: white;")
                .otherwise("-fx-background-color: #090a0c, linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%)," + "  linear-gradient(#20262b, #191d22)" +
                        ", radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));-fx-text-fill: white;"));
        ready.setFont(Font.font("Arial", 32));

        ready.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                if (playerTurn != 6) {
                    throw new NotEnoughChampionsException();
                }
                Control.onReady();
            } catch (IOException | NotEnoughChampionsException ex) {
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

        });

        statsParent.setLayoutX(25);
        statsParent.setLayoutY(380);
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
        champPreview.setLayoutX(850);
        champPreview.setLayoutY(400);

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(ready);
        root.getChildren().add(back);
        root.getChildren().add(triangleUp);
        root.getChildren().add(triangleDown);
//        root.getChildren().add(test);
        return new Scene(root, 1680,1050, Color.rgb(33,41,50));
    }

    public static void setPlayerTurn(int playerTurn) {
        DisplayChampions.playerTurn = playerTurn;
    }
}
