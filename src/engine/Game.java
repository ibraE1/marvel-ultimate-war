package engine;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Cover;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean firstLeaderAbilityUsed;
    private boolean secondLeaderAbilityUsed;
    private Object[][] board;
    private static ArrayList<Champion> availableChampions;
    private static ArrayList<Ability> availableAbilities;
    private PriorityQueue turnOrder;
    private static int BOARDHEIGHT = 5;
    private static int BOARDWIDTH = 5;

    public Game(Player first, Player second) {
        firstPlayer = first;
        secondPlayer = second;
        board = new Object[BOARDWIDTH][BOARDHEIGHT];
        placeChampions();
        placeCovers();
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public boolean isFirstLeaderAbilityUsed() {
        return firstLeaderAbilityUsed;
    }

    public boolean isSecondLeaderAbilityUsed() {
        return secondLeaderAbilityUsed;
    }

    public Object[][] getBoard() {
        return board;
    }

    public static ArrayList<Champion> getAvailableChampions() {
        return availableChampions;
    }

    public static ArrayList<Ability> getAvailableAbilities() {
        return availableAbilities;
    }

    public PriorityQueue getTurnOrder() {
        return turnOrder;
    }

    public static int getBOARDHEIGHT() {
        return BOARDHEIGHT;
    }

    public static int getBOARDWIDTH() { return BOARDWIDTH; }

    private void placeChampions() {
        ArrayList<Champion> firstTeam = getFirstPlayer().getTeam();
        ArrayList<Champion> secondTeam = getSecondPlayer().getTeam();
        board[1][0] = firstTeam.get(0);
        board[2][0] = firstTeam.get(1);
        board[3][0] = firstTeam.get(2);
        board[1][4] = secondTeam.get(0);
        board[2][4] = secondTeam.get(1);
        board[3][4] = secondTeam.get(2);
        firstTeam.get(0).setLocation(new Point(1,0));
        firstTeam.get(1).setLocation(new Point(2,0));
        firstTeam.get(2).setLocation(new Point(3,0));
        secondTeam.get(0).setLocation(new Point(1,4));
        secondTeam.get(1).setLocation(new Point(2,4));
        secondTeam.get(2).setLocation(new Point(3,4));
    }

    private void placeCovers() {
        int c = 0;
        while (c < 5) {
            int x = (int) (Math.random() * 5);
            int y = (int) ((Math.random() * 2) + 1);
            if (board[x][y] == null) {
                board[x][y] = new Cover(x, y);
                c++;
            }
        }
    }

    public static void loadAbilities(String filePath) throws Exception {
        BufferedReader br= new BufferedReader(new FileReader("csv/Abilities.csv"));
    }

    public static void loadChampions(String filePath) throws Exception {
        BufferedReader br= new BufferedReader(new FileReader("csv/Champions.csv"));
    }

}
