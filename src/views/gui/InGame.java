package views.gui;

import engine.Player;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.world.Champion;
import model.world.Cover;

import java.util.ArrayList;

public class InGame {
    public static Scene create() {
        Player player1 = Control.getP1();
        Player player2 = Control.getP2();
        ArrayList<Champion> firstTeam = player1.getTeam();
        ArrayList<Champion> secondTeam = player2.getTeam();

        ArrayList<Image> firstTeamImages = new ArrayList<Image>();
        for (Champion champ : firstTeam) {
            Image img = new Image("views/assets/champions/%s.png".formatted(champ.getName()));
            firstTeamImages.add(img);
        }
        ArrayList<Image> secondTeamImages = new ArrayList<Image>();
        for (Champion champ : secondTeam) {
            Image img = new Image("views/assets/champions/%s.png".formatted(champ.getName()));
            secondTeamImages.add(img);
        }

        VBox profile1 = new VBox();
        profile1.getChildren().add(new Label(player1.getName()));
        if (Control.getP1LeaderAbility())
            profile1.getChildren().add(new Label("Leader Ability Used"));
        else
            profile1.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team1 = new VBox();
        team1.getChildren().add(new ImageView(firstTeamImages.get(0)));
        team1.getChildren().add(new ImageView(firstTeamImages.get(1)));
        team1.getChildren().add(new ImageView(firstTeamImages.get(2)));
        profile1.getChildren().add(team1);
        profile1.setPrefWidth(150);
        profile1.setLayoutX(10);
        profile1.setLayoutY(10);

        VBox profile2 = new VBox();
        profile2.getChildren().add(new Label(player2.getName()));
        if (Control.getP2LeaderAbility())
            profile1.getChildren().add(new Label("Leader Ability Used"));
        else
            profile1.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team2 = new VBox();
        team2.getChildren().add(new ImageView(secondTeamImages.get(0)));
        team2.getChildren().add(new ImageView(secondTeamImages.get(1)));
        team2.getChildren().add(new ImageView(secondTeamImages.get(2)));
        team2.setAlignment(Pos.CENTER_RIGHT);
        profile2.getChildren().add(team2);
        profile2.setPrefWidth(150);
        profile2.setLayoutX(1270-profile2.getPrefWidth());
        profile2.setLayoutY(10);
        profile2.setAlignment(Pos.CENTER_RIGHT);

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
        board.setPrefSize(500, 500);
        board.setLayoutX(1280/2 - 250);
        board.setLayoutY(720/2 - 250);
        ArrayList<ImageView> boardImages = new ArrayList<ImageView>();
        for (Object tile : Control.getBoard()) {
            if (tile != null) {
                if (tile instanceof Champion) {
                    Champion ch = (Champion) tile;
                    ImageView iv = new ImageView(new Image("views/assets/champions/%s.png".formatted(ch.getName())));
                    boardImages.add(iv);
                    GridPane.setConstraints(iv, ch.getLocation().x, ch.getLocation().y);
                } else {
                    Cover cv = (Cover) tile;
                    ImageView iv = new ImageView(new Image("views/assets/champions/wall.png"));
                    boardImages.add(iv);
                    GridPane.setConstraints(iv, cv.getLocation().x, cv.getLocation().y);
                }
            }
        }
        board.getChildren().addAll(boardImages);

        HBox turn = new HBox();
        ArrayList<ImageView> turnImages = new ArrayList<ImageView>();
        turn.getChildren().addAll(turnImages);
        turn.setPrefSize(350, 55);
        turn.setLayoutX(1280 - 350);
        turn.setLayoutY(720 - 55);

        Group root = new Group();
        root.getChildren().add(profile1);
        root.getChildren().add(profile2);
        root.getChildren().add(board);
        root.getChildren().add(turn);
        return new Scene(root, 1280,720);
    }
}
