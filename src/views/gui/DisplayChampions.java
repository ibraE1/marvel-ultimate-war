package views.gui;

import engine.Player;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

import static engine.Game.*;

public class DisplayChampions {

    static Timer myTimer = new Timer();

    private static int playerTurn = 0;
    private static final Player player1 = Control.getP1();
    private static final Player player2 = Control.getP2();
    private static boolean flag = false;

    public static TimerTask chooseChampions(Button btn, int i) {
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
        return null;
    }
    public static Scene createDisplayChampions() throws IOException {
        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            String imgPath = "views/assets/champions/%s.png".formatted(i);
            Image img = new Image(imgPath, 80, 80, false, true, true);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(25);
        gp.setLayoutY(80);

        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button();
                btn.setPrefSize(80,80);
                btn.setGraphic(icons.get(counter));
                btn.setId("btn");
                gp.add(btn, i, j);
                avatars.add(btn);
                counter++;
            }
        }

        VBox statsParent = new VBox();
        HBox middleParent = new HBox();
        VBox nameBox = new VBox();
        VBox vboxRight = new VBox();
        VBox vboxLeft = new VBox();
        VBox vboxBottom = new VBox();
        VBox abilityTitle = new VBox();
        HBox header = new HBox();
        Pane champPreview = new Pane();
        Pane statGraph = new Pane();

        Pane time = new Pane();
        time.setLayoutX(500);
        time.setLayoutY(500);

        abilityTitle.getChildren().add(new Label("Champion Abilities"));
        statsParent.setId("stats-parent");
        for (int count = 0; count < avatars.size(); count++) {
            int i = count;
            ArrayList<Champion> availableChampions = getAvailableChampions();
            avatars.get(i).hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {

                    double hpPercent = (availableChampions.get(i).getMaxHP() / 2250d) * 100;
                    double manaPercent = (availableChampions.get(i).getMana() / 1500d) * 100;
                    double actionsPercent = (availableChampions.get(i).getMaxActionPointsPerTurn() / 8d) * 100;
                    double speedPercent = (availableChampions.get(i).getSpeed() / 99d) * 100;
                    double rngPercent = (availableChampions.get(i).getAttackRange() / 3d) * 100;
                    double dmgPercent = (availableChampions.get(i).getAttackDamage() / 200d) * 100;

                    Rectangle blackBar = new Rectangle(50, 5);
                    Rectangle hpBar = new Rectangle(hpPercent, 5);
                    Rectangle speedBar = new Rectangle(speedPercent, 5);
                    Rectangle manaBar = new Rectangle(manaPercent, 5);
                    Rectangle damageBar = new Rectangle(dmgPercent, 5);
                    Rectangle rangeBar = new Rectangle(rngPercent, 5);
                    Rectangle pointsBar = new Rectangle(actionsPercent, 5);

                    blackBar.setFill(Color.BLACK);

                    Label nameL = new Label(availableChampions.get(i).getName());
                    nameL.setId("name");
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
                    typeL.setId("type");

                    vboxLeft.setId("vbox-left");
                    vboxRight.setId("vbox-right");
                    abilityTitle.setId("ability-title");
                    vboxBottom.setId("vbox-bottom");

                    nameBox.getChildren().add(typeL);

                    vboxLeft.getChildren().add(new Label("Health ".concat(Integer.toString(availableChampions.get(i).getMaxHP()))));
                    vboxLeft.getChildren().add(hpBar);
                    vboxLeft.getChildren().add(new Label("Speed ".concat(Integer.toString(availableChampions.get(i).getSpeed()))));
                    vboxLeft.getChildren().add(speedBar);
                    vboxLeft.getChildren().add(new Label("Mana ".concat(Integer.toString(availableChampions.get(i).getMana()))));
                    vboxLeft.getChildren().add(manaBar);
                    vboxRight.getChildren().add(new Label("Damage ".concat(Integer.toString(availableChampions.get(i).getAttackDamage()))));
                    vboxRight.getChildren().add(damageBar);
                    vboxRight.getChildren().add(new Label("Range ".concat(Integer.toString(availableChampions.get(i).getAttackRange()))));
                    vboxRight.getChildren().add(rangeBar);
                    vboxRight.getChildren().add(new Label("Action Points ".concat(Integer.toString(availableChampions.get(i).getMaxActionPointsPerTurn()))));
                    vboxRight.getChildren().add(pointsBar);

                    vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(0).getName()));
                    vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(1).getName()));
                    vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(2).getName()));

                    Image chart = new Image("views/assets/charts/%s.png".formatted(i));
                    ImageView chart_view = new ImageView(chart);
                    chart_view.setFitWidth(280);
                    chart_view.setPreserveRatio(true);
                    statGraph.getChildren().add(chart_view);

                } else {
                    nameBox.getChildren().clear();
                    vboxLeft.getChildren().clear();
                    vboxRight.getChildren().clear();
                    vboxBottom.getChildren().clear();
                    statGraph.getChildren().clear();
                }
            });
            avatars.get(i).pressedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue) {
                    myTimer.schedule(new TimerTask(){

                        @Override
                        public void run() {
                            chooseChampions(avatars.get(i), i);
                        }
                    }, 10000);

                    playerTurn++;
                }
            });
        }

        Button back = new Button("Main Menu");
        back.setPrefSize(150,25);
        back.setLayoutX(20);
        back.setLayoutY(15);
        back.setFont(Font.font("Georgia", 18));

        back.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Control.onMainMenu();
        });

        Button ready = new Button("Ready");
        ready.setPrefSize(250,75);
        ready.setLayoutX(952);
        ready.setLayoutY(600);
        ready.setFont(Font.font("Georgia", 18));

        ready.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (flag) {
                try {
                    Control.onReady();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Button test = new Button("Ready");
        test.setPrefSize(250,75);
        test.setLayoutX(400);
        test.setLayoutY(600);
        test.setFont(Font.font("Georgia", 18));

        test.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    System.out.print(player1.getLeader().getName() + " ");
                } else {
                    System.out.print(player1.getTeam().get(i-1).getName() + " ");
                }
            }
            System.out.println();
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    System.out.print(player2.getLeader().getName() + " ");
                } else {
                    System.out.print(player2.getTeam().get(i-1).getName() + " ");
                }
            }
        });

        statsParent.setPrefSize(354,475);
        statsParent.setLayoutX(900);
        statsParent.setLayoutY(90);
        nameBox.setPrefSize(354, 150);
        vboxRight.setPrefSize(177, 125);
        vboxLeft.setPrefSize(177, 125);
        abilityTitle.setPrefSize(354,50);
        vboxBottom.setPrefSize(354,150);

        statsParent.setBackground(Background.fill(Color.BLACK));
        nameBox.setBackground(Background.fill(Color.RED));
        vboxLeft.setBackground(Background.fill(Color.WHITE));
        vboxRight.setBackground(Background.fill(Color.GREEN));
        abilityTitle.setBackground(Background.fill(Color.YELLOW));
        vboxBottom.setBackground(Background.fill(Color.YELLOW));

        middleParent.getChildren().add(vboxLeft);
        middleParent.getChildren().add(vboxRight);
        statsParent.getChildren().add(nameBox);
        statsParent.getChildren().add(middleParent);
        statsParent.getChildren().add(abilityTitle);
        statsParent.getChildren().add(vboxBottom);

        champPreview.setPrefSize(300,500);
        champPreview.setLayoutX(550);
        champPreview.setLayoutY(90);

        statGraph.setPrefSize(280,280);
        statGraph.setLayoutX(150);
        statGraph.setLayoutY(420);

        champPreview.setBackground(Background.fill(Color.PINK));
        statGraph.setBackground(Background.fill(Color.WHITE));

        header.setPrefSize(1280,80);
        vboxBottom.setLayoutX(0);
        vboxBottom.setLayoutY(0);

        header.setBackground(Background.fill(Color.GOLD));
        header.getChildren().add(back);

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(statGraph);
        root.getChildren().add(header);
        root.getChildren().add(ready);
        root.getChildren().add(test);
        return new Scene(root, 1280,720, Color.rgb(33,41,50));
    }
}