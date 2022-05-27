package views.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

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

        GridPane gp = new GridPane();
        ArrayList<ImageView> icons = new ArrayList<>(15);
        ArrayList<Button> avatars = new ArrayList<>();
        ArrayList<String> avatarNames = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("Champions.csv"));
        String line = br.readLine();
        while (line != null) {
            String[] content = line.split(",");
            avatarNames.add(content[1]);
            line = br.readLine();
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
            int finalI = i;
            avatars.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                try {
                    System.out.println(avatarNames.get(finalI));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        gp.setHgap(10);
        gp.setVgap(10);
        gp.setLayoutX(50);
        gp.setLayoutY(100);


        Button back = new Button("Main Menu");
        back.setPrefSize(150,25);
        back.setLayoutX(20);
        back.setLayoutY(10);
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

        Group root = new Group();
        root.getChildren().add(gp);
        root.getChildren().add(back);

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
