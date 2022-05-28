package views.gui;

import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import model.world.Champion;
import model.world.Hero;
import views.gui.Control;

import static engine.Game.getAvailableChampions;

public class AvailableChampions extends Application {
    public AvailableChampions () throws Exception {
        Stage championScreen = new Stage();
        start(championScreen);
    }

    @Override
    public void start(Stage championScreen) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Image icon = new Image("views/assets/icon.png");
        championScreen.getIcons().add(icon);
        new Game(new Player("Ahma"), new Player("Zizo"));
        GridPane gp = new GridPane();
        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Champion> availableChampions = getAvailableChampions();
        ArrayList<Button> avatars = new ArrayList<>();
        ArrayList<String> avatarNames = new ArrayList<>();
        ArrayList<Integer> hp = new ArrayList<>();
        ArrayList<Integer> mana = new ArrayList<>();
        ArrayList<Integer> speed = new ArrayList<>();
        ArrayList<Integer> attackDamage = new ArrayList<>();
        ArrayList<Integer> attackRange = new ArrayList<>();
        ArrayList<Integer> actions = new ArrayList<>();

        for (Champion champ : getAvailableChampions()) {
            avatarNames.add(champ.getName());
            hp.add(champ.getMaxHP());
            mana.add(champ.getMana());
            speed.add(champ.getSpeed());
            attackDamage.add(champ.getAttackDamage());
            attackRange.add(champ.getAttackRange());
            actions.add(champ.getMaxActionPointsPerTurn());
        }



        for (int i = 1; i <= 15; i++){
            String  imgPath = "views/assets/champions/%s.png".formatted(i);
            Image img = new Image(imgPath);
            ImageView icn = new ImageView(img);
            icons.add(icn);
        }

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

        for (int i = 0; i < avatars.size(); i++) {
            int finalI1 = i;
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    System.out.println(getAvailableChampions().get(finalI1));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(25);
        gp.setLayoutY(100);


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

        VBox statsParent = new VBox();
        HBox middleParent = new HBox();
        VBox nameBox = new VBox();
        VBox vboxRight = new VBox();
        VBox vboxLeft = new VBox();
        VBox vboxBottom = new VBox();

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


        Pane champPreview = new Pane();
        Pane statGraph = new Pane();

        champPreview.setPrefSize(300,500);
        champPreview.setLayoutX(550);
        champPreview.setLayoutY(100);

        statGraph.setPrefSize(250,250);
        statGraph.setLayoutX(150);
        statGraph.setLayoutY(455);

        champPreview.setBackground(Background.fill(Color.PINK));
        statGraph.setBackground(Background.fill(Color.WHITE));

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(back);
        root.getChildren().add(statsParent);
        root.getChildren().add(champPreview);
        root.getChildren().add(statGraph);

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
