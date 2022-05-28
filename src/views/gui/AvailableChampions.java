package views.gui;

import engine.Game;
import engine.Player;
import javafx.application.Application;
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
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

import static engine.Game.getAvailableChampions;

public class AvailableChampions extends Application {

    VBox statsParent = new VBox();
    HBox middleParent = new HBox();
    VBox nameBox = new VBox();
    VBox vboxRight = new VBox();
    VBox vboxLeft = new VBox();
    VBox vboxBottom = new VBox();
    HBox header = new HBox();
    Pane champPreview = new Pane();
    Pane statGraph = new Pane();
    public AvailableChampions () throws Exception {
        Stage championScreen = new Stage();
        start(championScreen);
    }

    private void focusState(int i, boolean value) {
        ArrayList<Champion> availableChampions = getAvailableChampions();
        if (value) {
            nameBox.getChildren().add(new Label(availableChampions.get(i).getName()));
            String type = "";
            if (availableChampions.get(i) instanceof Hero) {
                type = "Hero";
            } else if (availableChampions.get(i) instanceof Villain) {
                type = "Villain";
            } else if (availableChampions.get(i) instanceof AntiHero) {
                type = "Anti-Hero";
            }
            nameBox.getChildren().add(new Label(type));
            vboxLeft.getChildren().add(new Label("Health ".concat(Integer.toString(availableChampions.get(i).getMaxHP()))));
            vboxLeft.getChildren().add(new Label("Speed ".concat(Integer.toString(availableChampions.get(i).getSpeed()))));
            vboxLeft.getChildren().add(new Label("Mana ".concat(Integer.toString(availableChampions.get(i).getMana()))));
            vboxRight.getChildren().add(new Label("Damage ".concat(Integer.toString(availableChampions.get(i).getAttackDamage()))));
            vboxRight.getChildren().add(new Label("Range ".concat(Integer.toString(availableChampions.get(i).getAttackRange()))));
            vboxRight.getChildren().add(new Label("Action Points ".concat(Integer.toString(availableChampions.get(i).getMaxActionPointsPerTurn()))));
            vboxBottom.getChildren().add(new Label("Champion Abilities"));
            vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(0).getName()));
            vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(1).getName()));
            vboxBottom.getChildren().add(new Label(availableChampions.get(i).getAbilities().get(2).getName()));
        }
        else {
            nameBox.getChildren().clear();
            vboxLeft.getChildren().clear();
            vboxLeft.getChildren().clear();
            vboxLeft.getChildren().clear();
            vboxRight.getChildren().clear();
            vboxRight.getChildren().clear();
            vboxRight.getChildren().clear();
            vboxBottom.getChildren().clear();
            vboxBottom.getChildren().clear();
            vboxBottom.getChildren().clear();
            vboxBottom.getChildren().clear();
        }
    }
    @Override
    public void start(Stage championScreen) throws Exception {
        new Game(new Player("Ahma"), new Player("Zizo"));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image icon = new Image("views/assets/icon.png");
        championScreen.getIcons().add(icon);

        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();

        for (int i = 1; i <= 15; i++){
            String  imgPath = "views/assets/champions/%s.png".formatted(i);
            Image img = new Image(imgPath);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(25);
        gp.setLayoutY(100);

        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 3; j++) {
                Button btn = new Button();
                btn.setPrefSize(80,100);
                btn.setGraphic(icons.get(counter));
                btn.setId("btn");
                gp.add(btn, i, j);
                avatars.add(btn);
                counter++;
            }
        }

        statsParent = new VBox();
        middleParent = new HBox();
        nameBox = new VBox();
        vboxRight = new VBox();
        vboxLeft = new VBox();
        vboxBottom = new VBox();
        header = new HBox();
        champPreview = new Pane();
        statGraph = new Pane();

        for (int count = 0; count < avatars.size(); count++) {
            int i = count;

            avatars.get(i).focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                focusState(i, newValue);
            });

        }



        Button back = new Button("Main Menu");
        back.setPrefSize(150,25);
        back.setLayoutX(20);
        back.setLayoutY(15);
        back.setFont(Font.font("Georgia", 18));

        back.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                championScreen.close();
                MainMenu m = new MainMenu();
                Stage main = new Stage();
                m.start(main);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });




        statsParent.setPrefSize(354,600);
        statsParent.setLayoutX(900);
        statsParent.setLayoutY(100);

        nameBox.setPrefSize(354, 150);
        nameBox.setLayoutX(900);
        nameBox.setLayoutY(100);

        vboxRight.setPrefSize(177, 250);
        vboxRight.setLayoutX(900);
        vboxRight.setLayoutY(250);

        vboxLeft.setPrefSize(177, 250);
        vboxLeft.setLayoutX(1100);
        vboxLeft.setLayoutY(250);

        vboxBottom.setPrefSize(354,200);
        vboxBottom.setLayoutX(900);
        vboxBottom.setLayoutY(550);

        statsParent.setBackground(Background.fill(Color.BLACK));
        nameBox.setBackground(Background.fill(Color.RED));
        vboxLeft.setBackground(Background.fill(Color.BLUE));
        vboxRight.setBackground(Background.fill(Color.GREEN));
        vboxBottom.setBackground(Background.fill(Color.YELLOW));


        middleParent.getChildren().add(vboxLeft);
        middleParent.getChildren().add(vboxRight);
        statsParent.getChildren().add(nameBox);
        statsParent.getChildren().add(middleParent);
        statsParent.getChildren().add(vboxBottom);




        champPreview.setPrefSize(300,500);
        champPreview.setLayoutX(550);
        champPreview.setLayoutY(100);

        statGraph.setPrefSize(250,250);
        statGraph.setLayoutX(150);
        statGraph.setLayoutY(455);

        champPreview.setBackground(Background.fill(Color.PINK));
        statGraph.setBackground(Background.fill(Color.WHITE));

        header.setPrefSize(1280,80);
        vboxBottom.setLayoutX(0);
        vboxBottom.setLayoutY(0);

        header.setBackground(Background.fill(Color.GOLD));

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(back);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(statGraph);
        root.getChildren().add(header);

        Scene scene = new Scene(root, 1280,720, Color.rgb(33,41,50));
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("championScreenStyle.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm());
        championScreen.setResizable(false);
        championScreen.setX((screenSize.getWidth() / 2) - 640);
        championScreen.setY((screenSize.getHeight() / 2) - 360);
        championScreen.setTitle("Marvel Ultimate War");
        championScreen.setScene(scene);
        championScreen.show();
    }
}
