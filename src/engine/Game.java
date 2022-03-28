package engine;

import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean firstLeaderAbilityUsed;
    private boolean secondLeaderAbilityUsed;
    private Object[][] board;
    private static ArrayList<Champion> availableChampions;
    private static ArrayList<Ability> availableAbilities;
    private PriorityQueue turnOrder;
    private static final int BOARDHEIGHT = 5;
    private static final int BOARDWIDTH = 5;

    public Game(Player first, Player second) throws Exception {
        firstPlayer = first;
        secondPlayer = second;
        board = new Object[BOARDWIDTH][BOARDHEIGHT];
        turnOrder = new PriorityQueue(6);
        availableChampions = new ArrayList<Champion>(15);
        availableAbilities = new ArrayList<Ability>(45);

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

    public static int getBoardheight() {
        return BOARDHEIGHT;
    }

    public static int getBoardwidth() { return BOARDWIDTH; }

    private void placeChampions() {
        if (!getFirstPlayer().getTeam().isEmpty()) {
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
        BufferedReader br= new BufferedReader(new FileReader(filePath));
        for (String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {
            String[] abilityDetails = currentLine.split(",");
            Ability abt = null;
            switch (abilityDetails[0]) {
                case "CC":
                    Effect eft = null;
                    switch (abilityDetails[7]) {
                        case "Stun":
                            eft = new Stun(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "SpeedUp":
                            eft = new SpeedUp(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Silence":
                            eft = new Silence(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Shock":
                            eft = new Shock(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Shield":
                            eft = new Shield(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Root":
                            eft = new Root(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "PowerUp":
                            eft = new PowerUp(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Embrace":
                            eft = new Embrace(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Dodge":
                            eft = new Dodge(Integer.parseInt(abilityDetails[8]));
                            break;
                        case "Disarm":
                            eft = new Disarm(Integer.parseInt(abilityDetails[8]));
                            break;
                    }
                    abt = new CrowdControlAbility(abilityDetails[1], Integer.parseInt(abilityDetails[2]),
                            Integer.parseInt(abilityDetails[4]), Integer.parseInt(abilityDetails[3]),
                            AreaOfEffect.valueOf(abilityDetails[5]), Integer.parseInt(abilityDetails[6]), eft);
                    break;
                case "DMG":
                    abt = new DamagingAbility(abilityDetails[1], Integer.parseInt(abilityDetails[2]),
                            Integer.parseInt(abilityDetails[4]), Integer.parseInt(abilityDetails[3]),
                            AreaOfEffect.valueOf(abilityDetails[5]), Integer.parseInt(abilityDetails[6]), Integer.parseInt(abilityDetails[7]));
                    break;
                case "HEL":
                    abt = new HealingAbility(abilityDetails[1], Integer.parseInt(abilityDetails[2]),
                            Integer.parseInt(abilityDetails[4]), Integer.parseInt(abilityDetails[3]),
                            AreaOfEffect.valueOf(abilityDetails[5]), Integer.parseInt(abilityDetails[6]), Integer.parseInt(abilityDetails[7]));
                    break;
            }
            availableAbilities.add(abt);
        }
    }

    public static void loadChampions(String filePath) throws Exception {
        BufferedReader br= new BufferedReader(new FileReader(filePath));
        for (String currentLine = br.readLine(); currentLine != null; currentLine = br.readLine()) {
            String[] championDetails = currentLine.split(",");
            Champion ch = null;
            switch (championDetails[0]) {
                case "H":
                    ch = new Hero(championDetails[1], Integer.parseInt(championDetails[2]),
                            Integer.parseInt(championDetails[3]), Integer.parseInt(championDetails[4]),
                            Integer.parseInt(championDetails[5]), Integer.parseInt(championDetails[6]),
                            Integer.parseInt(championDetails[7]));
                    break;
                case "A":
                    ch = new AntiHero(championDetails[1], Integer.parseInt(championDetails[2]),
                            Integer.parseInt(championDetails[3]), Integer.parseInt(championDetails[4]),
                            Integer.parseInt(championDetails[5]), Integer.parseInt(championDetails[6]),
                            Integer.parseInt(championDetails[7]));
                    break;
                case "V":
                    ch = new Villain(championDetails[1], Integer.parseInt(championDetails[2]),
                            Integer.parseInt(championDetails[3]), Integer.parseInt(championDetails[4]),
                            Integer.parseInt(championDetails[5]), Integer.parseInt(championDetails[6]),
                            Integer.parseInt(championDetails[7]));
                    break;
            }
            for (Ability abt : availableAbilities) {
                if (abt.getName().equals(championDetails[8]) || abt.getName().equals(championDetails[9]) || abt.getName().equals(championDetails[10]))
                    ch.getAbilities().add(abt);
            }
            availableChampions.add(ch);
        }
    }

}
