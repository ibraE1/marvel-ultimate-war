package views.gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class InGame {
    public static Scene create() {
        ArrayList<Image> champs = new ArrayList<Image>();
        for (int x = 1; x < 7; x++) {
            Image img = new Image("views/assets/champions/%s.png".formatted(x));
            champs.add(img);
        }

        VBox player1 = new VBox();
        player1.getChildren().add(new Label("Player 1"));
        player1.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team1 = new VBox();
        team1.getChildren().add(new ImageView(champs.get(0)));
        team1.getChildren().add(new ImageView(champs.get(1)));
        team1.getChildren().add(new ImageView(champs.get(2)));
        player1.getChildren().add(team1);
        player1.setPrefWidth(150);
        player1.setLayoutX(10);
        player1.setLayoutY(10);

        VBox player2 = new VBox();
        player2.getChildren().add(new Label("Player 2"));
        player2.getChildren().add(new Label("Leader Ability Not Used"));
        VBox team2 = new VBox();
        team2.getChildren().add(new ImageView(champs.get(3)));
        team2.getChildren().add(new ImageView(champs.get(4)));
        team2.getChildren().add(new ImageView(champs.get(5)));
        team2.setAlignment(Pos.CENTER_RIGHT);
        player2.getChildren().add(team2);
        player2.setPrefWidth(150);
        player2.setLayoutX(1270-player2.getPrefWidth());
        player2.setLayoutY(10);
        player2.setAlignment(Pos.CENTER_RIGHT);

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
        for (Image img : champs)
            boardImages.add(new ImageView(img));
        for (int i = 1; i < 4; i++)
            GridPane.setConstraints(boardImages.get(i-1), i, 4);
        for (int i = 1; i < 4; i++)
            GridPane.setConstraints(boardImages.get(i+2), i, 0);
        board.getChildren().addAll(boardImages);

        HBox turn = new HBox();
        ArrayList<ImageView> turnImages = new ArrayList<ImageView>();
        for (int i = 0, j = 3; j < 6; i+=1, j+=1) {
            ImageView img1 = new ImageView(champs.get(i));
            img1.setFitWidth(50);
            img1.setFitHeight(50);
            turnImages.add(img1);
            ImageView img2 = new ImageView(champs.get(j));
            img2.setFitWidth(50);
            img2.setFitHeight(50);
            turnImages.add(img2);
        }
        turn.getChildren().addAll(turnImages);
        turn.setPrefSize(350, 55);
        turn.setLayoutX(1280 - 350);
        turn.setLayoutY(720 - 55);

        Group root = new Group();
        root.getChildren().add(player1);
        root.getChildren().add(player2);
        root.getChildren().add(board);
        root.getChildren().add(turn);
        return new Scene(root, 1280,720);
    }
}
