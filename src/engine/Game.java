package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import exceptions.ChampionDisarmedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.*;
import model.effects.*;
import model.world.*;

public class Game {
    private static ArrayList<Champion> availableChampions;
    private static ArrayList<Ability> availableAbilities;
    private Player firstPlayer;
    private Player secondPlayer;
    private Object[][] board;
    private PriorityQueue turnOrder;
    private boolean firstLeaderAbilityUsed;
    private boolean secondLeaderAbilityUsed;
    private final static int BOARDWIDTH = 5;
    private final static int BOARDHEIGHT = 5;

    public Game(Player first, Player second) {
        firstPlayer = first;
        secondPlayer = second;
        availableChampions = new ArrayList<Champion>();
        availableAbilities = new ArrayList<Ability>();
        board = new Object[BOARDWIDTH][BOARDHEIGHT];
        turnOrder = new PriorityQueue(6);
        placeChampions();
        placeCovers();
    }

    public static void loadAbilities(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        while (line != null) {
            String[] content = line.split(",");
            Ability a = null;
            AreaOfEffect ar = null;
            switch (content[5]) {
                case "SINGLETARGET":
                    ar = AreaOfEffect.SINGLETARGET;
                    break;
                case "TEAMTARGET":
                    ar = AreaOfEffect.TEAMTARGET;
                    break;
                case "SURROUND":
                    ar = AreaOfEffect.SURROUND;
                    break;
                case "DIRECTIONAL":
                    ar = AreaOfEffect.DIRECTIONAL;
                    break;
                case "SELFTARGET":
                    ar = AreaOfEffect.SELFTARGET;
                    break;
            }
            Effect e = null;
            if (content[0].equals("CC")) {
                switch (content[7]) {
                    case "Disarm":
                        e = new Disarm(Integer.parseInt(content[8]));
                        break;
                    case "Dodge":
                        e = new Dodge(Integer.parseInt(content[8]));
                        break;
                    case "Embrace":
                        e = new Embrace(Integer.parseInt(content[8]));
                        break;
                    case "PowerUp":
                        e = new PowerUp(Integer.parseInt(content[8]));
                        break;
                    case "Root":
                        e = new Root(Integer.parseInt(content[8]));
                        break;
                    case "Shield":
                        e = new Shield(Integer.parseInt(content[8]));
                        break;
                    case "Shock":
                        e = new Shock(Integer.parseInt(content[8]));
                        break;
                    case "Silence":
                        e = new Silence(Integer.parseInt(content[8]));
                        break;
                    case "SpeedUp":
                        e = new SpeedUp(Integer.parseInt(content[8]));
                        break;
                    case "Stun":
                        e = new Stun(Integer.parseInt(content[8]));
                        break;
                }
            }
            switch (content[0]) {
                case "CC":
                    a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
                            Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
                    break;
                case "DMG":
                    a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
                            Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
                    break;
                case "HEL":
                    a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
                            Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
                    break;
            }
            availableAbilities.add(a);
            line = br.readLine();
        }
        br.close();
    }

    public static void loadChampions(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        while (line != null) {
            String[] content = line.split(",");
            Champion c = null;
            switch (content[0]) {
                case "A":
                    c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
                            Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
                            Integer.parseInt(content[7]));
                    break;
                case "H":
                    c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
                            Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
                            Integer.parseInt(content[7]));
                    break;
                case "V":
                    c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
                            Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
                            Integer.parseInt(content[7]));
                    break;
            }
            c.getAbilities().add(findAbilityByName(content[8]));
            c.getAbilities().add(findAbilityByName(content[9]));
            c.getAbilities().add(findAbilityByName(content[10]));
            availableChampions.add(c);
            line = br.readLine();
        }
        br.close();
    }

    private static Ability findAbilityByName(String name) {
        for (Ability a : availableAbilities) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }

    public void placeCovers() {
        int i = 0;
        while (i < 5) {
            int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
            int y = (int) (Math.random() * BOARDHEIGHT);

            if (board[x][y] == null) {
                board[x][y] = new Cover(x, y);
                i++;
            }
        }
    }

    public void placeChampions() {
        int i = 1;
        for (Champion c : firstPlayer.getTeam()) {
            board[0][i] = c;
            c.setLocation(new Point(0, i));
            i++;
        }
        i = 1;
        for (Champion c : secondPlayer.getTeam()) {
            board[BOARDHEIGHT - 1][i] = c;
            c.setLocation(new Point(BOARDHEIGHT - 1, i));
            i++;
        }
    }

    public static ArrayList<Champion> getAvailableChampions() {
        return availableChampions;
    }

    public static ArrayList<Ability> getAvailableAbilities() {
        return availableAbilities;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public Object[][] getBoard() {
        return board;
    }

    public PriorityQueue getTurnOrder() {
        return turnOrder;
    }

    public boolean isFirstLeaderAbilityUsed() {
        return firstLeaderAbilityUsed;
    }

    public boolean isSecondLeaderAbilityUsed() {
        return secondLeaderAbilityUsed;
    }

    public static int getBoardwidth() {
        return BOARDWIDTH;
    }

    public static int getBoardheight() {
        return BOARDHEIGHT;
    }

    public Champion getCurrentChampion() {
        return (Champion) turnOrder.peekMin();
    }

    public Player checkGameOver() {
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();
        boolean firstFlag = true;
        boolean secondFlag = true;
        for (Champion champ : firstTeam) {
            if (champ.getCurrentHP() != 0) {
                firstFlag = false;
                break;
            }
        }
        for (Champion champ : secondTeam) {
            if (champ.getCurrentHP() != 0) {
                secondFlag = false;
                break;
            }
        }
        if (!firstFlag)
            return firstPlayer;
        else if (!secondFlag)
            return secondPlayer;
        else return null;
    }

    public void move(Direction d) throws NotEnoughResourcesException, UnallowedMovementException {
        Champion champ = getCurrentChampion();
        int action = champ.getCurrentActionPoints();
        Point location = champ.getLocation();
        if (action <= 0)
            throw new NotEnoughResourcesException();
        else if (champ.getCondition() == Condition.ROOTED){
            throw new UnallowedMovementException();
        } else {
            champ.setCurrentActionPoints(action - 1);
            switch (d) {
                case UP:
                    champ.setLocation(new Point(location.x, location.y+1));
                    break;
                case DOWN:
                    champ.setLocation(new Point(location.x, location.y-1));
                    break;
                case LEFT:
                    champ.setLocation(new Point(location.x-1, location.y));
                    break;
                case RIGHT:
                    champ.setLocation(new Point(location.x+1, location.y-1));
                    break;
            }
        }
    }

    public int calculateDamage(Champion attacker, Damageable defender) {
        int ad  = attacker.getAttackDamage();
        if (defender instanceof Cover)
            return ad;
        Champion def = (Champion) defender;
        for (Effect eft : def.getAppliedEffects()) {
            if (eft instanceof Shield) {
                eft.remove(def);
                return 0;
            }
            if (eft instanceof Dodge) {
                if (Math.random() < 0.5)
                    return 0;
            }
        }
        int damage = 0;
        if (attacker instanceof Hero) {
            if (def instanceof Hero) {
                damage += ad;
            } else {
                damage += (int) (ad*1.5);
            }
        } else if (attacker instanceof Villain) {
            if (def instanceof Villain) {
                damage += ad;
            } else {
                damage += (int) (ad*1.5);
            }
        } else if (attacker instanceof AntiHero) {
            if (def instanceof AntiHero) {
                damage += ad;
            } else {
                damage += (int) (ad*1.5);
            }
        }
        return damage;
    }

    public void attack(Direction d) throws NotEnoughResourcesException, ChampionDisarmedException {
        Champion champ = getCurrentChampion();
        int action = champ.getCurrentActionPoints();
        Point location = champ.getLocation();
        int range = champ.getAttackRange();
        for (Effect eft : champ.getAppliedEffects()) {
            if (eft instanceof Disarm)
                throw new ChampionDisarmedException();
        }
        if (action <= 1)
            throw new NotEnoughResourcesException();
        else {
            champ.setCurrentActionPoints(action - 2);
            switch (d) {
                case UP:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x][location.y+i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case DOWN:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x][location.y-i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case LEFT:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x-i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x+i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
            }
        }
    }

    public void castAbility(Ability a) throws NotEnoughResourcesException {
        if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
            throw new NotEnoughResourcesException();
        }
        getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
        getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()
                - a.getRequiredActionPoints());

        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();


        switch (a.getCastArea()) {

            case TEAMTARGET:
                if (a instanceof CrowdControlAbility) {

                    if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {

                        if (firstTeam.contains(getCurrentChampion())) {
                            targets.addAll(secondTeam);
                        } else {
                            targets.addAll(firstTeam);
                        }

                    } else {

                        if (firstTeam.contains(getCurrentChampion())) {
                            for (Damageable d : secondTeam) {
                                Champion c = (Champion) d;
                                for (Effect e : c.getAppliedEffects()) {
                                    if (e instanceof Shield) {
                                        c.getAppliedEffects().remove(e);
                                    } else {
                                        targets.add(d);
                                    }
                                }
                            }
                        } else {
                            for (Damageable d : firstTeam) {
                                Champion c = (Champion) d;
                                for (Effect e : c.getAppliedEffects()) {
                                    if (e instanceof Shield) {
                                        c.getAppliedEffects().remove(e);
                                    } else {
                                        targets.add(d);
                                    }
                                }
                            }
                        }
                    }
                } else if (a instanceof HealingAbility) {
                    if (firstTeam.contains(getCurrentChampion())) {
                        targets.addAll(firstTeam);
                    } else {
                        targets.addAll(secondTeam);
                    }
                } else {
                    if (firstTeam.contains(getCurrentChampion())) {
                        targets.addAll(secondTeam);
                    } else {
                        targets.addAll(firstTeam);
                    }
                }
                break;


            case SELFTARGET:
                if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) ||
                a instanceof HealingAbility) {
                    targets.add(getCurrentChampion());
                }
                break;


            case SURROUND:
                int x = getCurrentChampion().getLocation().x;
                int y = getCurrentChampion().getLocation().y;

                ArrayList<Champion> champTeam = new ArrayList<>();
                ArrayList<Champion> enemyTeam = new ArrayList<>();
                if (firstTeam.contains(getCurrentChampion())) {
                    champTeam = firstTeam;
                    enemyTeam = secondTeam;
                } else if (secondTeam.contains(getCurrentChampion())){
                    champTeam = secondTeam;
                    enemyTeam = firstTeam;
                }

                if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) || a instanceof HealingAbility) { // Player Team

                    if (x != 4 && board[x+1][y] instanceof Damageable && ((champTeam.contains((Champion) board[x+1][y])) || board[x+1][y] instanceof Cover))
                        targets.add((Damageable) board[x+1][y]);

                    if (x != 4 && y != 4 && board[x+1][y+1] instanceof Damageable && ((champTeam.contains((Champion) board[x+1][y+1])) || board[x+1][y+1] instanceof Cover))
                        targets.add((Damageable) board[x+1][y+1]);

                    if (x != 4 && y != 0 && board[x+1][y-1] instanceof Damageable && ((champTeam.contains((Champion) board[x+1][y-1]) || board[x+1][y-1] instanceof Cover)))
                        targets.add((Damageable) board[x+1][y-1]);

                    if (x != 0 && board[x-1][y] instanceof Damageable && ((champTeam.contains((Champion) board[x-1][y]) || board[x-1][y] instanceof Cover)))
                        targets.add((Damageable) board[x-1][y]);

                    if (x != 0 && y != 4 && board[x-1][y+1] instanceof Damageable && ((champTeam.contains((Champion) board[x-1][y+1]) || board[x-1][y+1] instanceof Cover)))
                        targets.add((Damageable) board[x-1][y+1]);

                    if (x != 0 && y != 0 && board[x-1][y-1] instanceof Damageable && ((champTeam.contains((Champion) board[x-1][y-1]) || board[x-1][y-1] instanceof Cover)))
                        targets.add((Damageable) board[x-1][y-1]);

                    if (y != 4 && board[x][y+1] instanceof Damageable && ((champTeam.contains((Champion) board[x+1][y]) || board[x+1][y] instanceof Cover)))
                        targets.add((Damageable) board[x+1][y]);

                    if (y != 0 && board[x][y-1] instanceof Damageable && ((champTeam.contains((Champion) board[x][y-1]) || board[x][y-1] instanceof Cover)))
                        targets.add((Damageable) board[x][y-1]);

                }

                if ((a instanceof CrowdControlAbility && ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF) || a instanceof DamagingAbility) { // Enemy Team

                    if (x != 4) {
                        if (board[x+1][y] instanceof Champion && enemyTeam.contains((Champion) board[x+1][y])) {
                            for (Effect e : ((Champion) board[x + 1][y]).getAppliedEffects()) {
                                if (e instanceof Shield) {
                                    ((Champion) board[x + 1][y]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x + 1][y]);
                                }
                            }
                        }
                        if (board[x+1][y] instanceof Cover) {
                            targets.add((Damageable) board[x + 1][y]);
                        }
                    }

                    if (x != 4 && y != 4) {
                        if (board[x+1][y+1] instanceof Champion && enemyTeam.contains((Champion) board[x+1][y+1])) {
                            for (Effect e : ((Champion) board[x+1][y+1]).getAppliedEffects()) {
                                if (e instanceof Shield) {
                                    ((Champion) board[x+1][y+1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x+1][y+1]);
                                }
                            }
                        }
                        if (board[x+1][y+1] instanceof Cover) {
                            targets.add((Damageable) board[x+1][y+1]);
                        }
                    }


                    if (x != 4 && y != 0) {
                        if (board[x+1][y-1] instanceof Champion && enemyTeam.contains((Champion) board[x+1][y-1])) {
                            for (Effect e : ((Champion) board[x+1][y-1]).getAppliedEffects()) {
                                if (e instanceof Shield) {
                                    ((Champion) board[x+1][y-1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x+1][y-1]);
                                }
                            }
                        }
                        if (board[x+1][y-1] instanceof Cover) {
                            targets.add((Damageable) board[x+1][y-1]);
                        }
                    }

                    if (x != 0) {
                        if (board[x-1][y] instanceof Champion && enemyTeam.contains((Champion) board[x-1][y])) {
                            for (Effect e : ((Champion) board[x-1][y]).getAppliedEffects()) {
                                if (e instanceof Shield) {
                                    ((Champion) board[x-1][y]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x-1][y]);
                                }
                            }
                        }
                        if (board[x-1][y] instanceof Cover) {
                            targets.add((Damageable) board[x-1][y]);
                        }
                    }

                    if (x != 0 && y != 4 ) {
                        if (board[x-1][y+1] instanceof Champion && enemyTeam.contains((Champion) board[x-1][y+1])) {
                            for (Effect e : ((Champion) board[x-1][y+1]).getAppliedEffects()) {
                                if (e instanceof Shield) {
                                    ((Champion) board[x-1][y+1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x-1][y+1]);
                                }
                            }
                        }
                        if (board[x-1][y+1] instanceof Cover) {
                            targets.add((Damageable) board[x-1][y+1]);
                        }
                    }

                    if (x != 0 && y != 0) {
                        if (board[x-1][y-1] instanceof Champion && enemyTeam.contains((Champion) board[x-1][y-1])) {
                            for (Effect e : ((Champion) board[x-1][y-1]).getAppliedEffects()){
                                if (e instanceof Shield){
                                    ((Champion) board[x-1][y-1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x-1][y-1]);
                                }
                            }
                        }
                        if (board[x-1][y-1] instanceof Cover) {
                            targets.add((Damageable) board[x-1][y-1]);
                        }
                    }

                    if (y != 4) {
                        if (board[x][y+1] instanceof Champion && enemyTeam.contains((Champion) board[x][y+1])) {
                            for (Effect e : ((Champion) board[x][y+1]).getAppliedEffects()){
                                if (e instanceof Shield){
                                    ((Champion) board[x][y+1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x][y+1]);
                                }
                            }
                        }
                        if (board[x][y+1] instanceof Cover) {
                            targets.add((Damageable) board[x][y+1]);
                        }
                    }

                    if (y != 0) {
                        if (board[x][y-1] instanceof Champion && enemyTeam.contains((Champion) board[x][y-1])) {
                            for (Effect e : ((Champion) board[x][y-1]).getAppliedEffects()){
                                if (e instanceof Shield){
                                    ((Champion) board[x][y-1]).getAppliedEffects().remove(e);
                                } else {
                                    targets.add((Damageable) board[x][y-1]);
                                }
                            }
                        }
                        if (board[x][y-1] instanceof Cover) {
                            targets.add((Damageable) board[x][y-1]);
                        }
                    }
                }

                break;

        }

        if (!targets.isEmpty()) {
            a.execute(targets);
        }
    }

    public void useLeaderAbility() throws LeaderNotCurrentException {
        ArrayList<Champion> targets = new ArrayList<Champion>();
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();

        if (getCurrentChampion() == firstPlayer.getLeader() || getCurrentChampion() == secondPlayer.getLeader()) {
            if (getCurrentChampion() == firstPlayer.getLeader()) {
                targets.addAll(secondTeam);
            } else if (getCurrentChampion() == secondPlayer.getLeader()) {
                targets.addAll(firstTeam);
            }
            getCurrentChampion().useLeaderAbility(targets);
        } else {
            throw new LeaderNotCurrentException();
        }



    }
}
