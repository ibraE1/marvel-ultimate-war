package engine;

import exceptions.*;
import model.abilities.*;
import model.effects.*;
import model.world.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private static ArrayList<Champion> availableChampions;
    private static ArrayList<Ability> availableAbilities;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private final Object[][] board;
    private final PriorityQueue turnOrder;
    private boolean firstLeaderAbilityUsed;
    private boolean secondLeaderAbilityUsed;
    private final static int BOARDWIDTH = 5;
    private final static int BOARDHEIGHT = 5;

    public Game(Player first, Player second) {
        firstPlayer = first;
        secondPlayer = second;
        availableChampions = new ArrayList<>();
        availableAbilities = new ArrayList<>();
        board = new Object[BOARDWIDTH][BOARDHEIGHT];
        turnOrder = new PriorityQueue(6);
        placeChampions();
        placeCovers();
        prepareChampionTurns();
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
        if (firstTeam.isEmpty())
            return secondPlayer;
        if (secondTeam.isEmpty())
            return firstPlayer;
        return null;
    }

    public boolean checkBorderMove(Point location, Direction d) {
        switch (d) {
            case UP:
                if (location.x == 4)
                    return true;
                break;
            case DOWN:
                if (location.x == 0)
                    return true;
                break;
            case LEFT:
                if (location.y == 0)
                    return true;
                break;
            case RIGHT:
                if (location.y == 4)
                    return true;
                break;
        }
        return false;
    }

    public boolean checkEmptyCell(Point location, Direction d) {
        switch (d) {
            case UP:
                if (board[location.x + 1][location.y] instanceof Damageable)
                    return false;
                break;
            case DOWN:
                if (board[location.x - 1][location.y] instanceof Damageable)
                    return false;
                break;
            case LEFT:
                if (board[location.x][location.y - 1] instanceof Damageable)
                    return false;
                break;
            case RIGHT:
                if (board[location.x][location.y + 1] instanceof Damageable)
                    return false;
                break;
        }
        return true;
    }

    public void move(Direction d) throws NotEnoughResourcesException, UnallowedMovementException {
        Champion champ = getCurrentChampion();
        int action = champ.getCurrentActionPoints();
        Point location = champ.getLocation();
        if (action <= 0)
            throw new NotEnoughResourcesException();
        else if (champ.getCondition() == Condition.ROOTED || checkBorderMove(location, d)
                || !checkEmptyCell(location, d))
            throw new UnallowedMovementException();
        else {
            champ.setCurrentActionPoints(action - 1);
            switch (d) {
                case UP:
                    champ.setLocation(new Point(location.x + 1, location.y));
                    board[location.x + 1][location.y] = champ;
                    break;
                case DOWN:
                    champ.setLocation(new Point(location.x - 1, location.y));
                    board[location.x - 1][location.y] = champ;
                    break;
                case LEFT:
                    champ.setLocation(new Point(location.x, location.y - 1));
                    board[location.x][location.y - 1] = champ;
                    break;
                case RIGHT:
                    champ.setLocation(new Point(location.x, location.y + 1));
                    board[location.x][location.y + 1] = champ;
                    break;
            }
            board[location.x][location.y] = null;
        }
    }

    public int calculateDamage(Champion attacker, Damageable defender) {
        int ad = attacker.getAttackDamage();
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
                damage += (int) (ad * 1.5);
            }
        } else if (attacker instanceof Villain) {
            if (def instanceof Villain) {
                damage += ad;
            } else {
                damage += (int) (ad * 1.5);
            }
        } else if (attacker instanceof AntiHero) {
            if (def instanceof AntiHero) {
                damage += ad;
            } else {
                damage += (int) (ad * 1.5);
            }
        }
        return damage;
    }

    public boolean checkFriendlyFire(Champion attacker, Damageable defender) {
        Champion def = (Champion) defender;
        ArrayList<Champion> allies;
        if (firstPlayer.getTeam().contains(attacker))
            allies = firstPlayer.getTeam();
        else
            allies = secondPlayer.getTeam();
        if (allies.contains(def))
            return true;
        return false;
    }

    public void attack(Direction d) throws NotEnoughResourcesException, ChampionDisarmedException, InvalidTargetException {
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
            switch (d) {
                case UP:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x][location.y + i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkFriendlyFire(champ, (Champion) dmg))
                                throw new InvalidTargetException();
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case DOWN:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x][location.y - i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkFriendlyFire(champ, (Champion) dmg))
                                throw new InvalidTargetException();
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case LEFT:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x - i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkFriendlyFire(champ, (Champion) dmg))
                                throw new InvalidTargetException();
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = 1; i <= range; i++) {
                        Object tile = board[location.x + i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkFriendlyFire(champ, (Champion) dmg))
                                throw new InvalidTargetException();
                            dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                            break;
                        }
                    }
                    break;
            }
            champ.setCurrentActionPoints(action - 2);
        }
    }

    public void executeHelper(Ability a, ArrayList<Damageable> targets) throws CloneNotSupportedException, AbilityUseException, NotEnoughResourcesException {
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();
        ArrayList<Damageable> enemies = new ArrayList<>();
        ArrayList<Damageable> covers = new ArrayList<>();
        ArrayList<Damageable> friendly = new ArrayList<>();
        ArrayList<Champion> champTeam = new ArrayList<>();
        ArrayList<Champion> enemyTeam = new ArrayList<>();
        if (firstTeam.contains(getCurrentChampion())) {
            champTeam = firstTeam;
            enemyTeam = secondTeam;
        } else if (secondTeam.contains(getCurrentChampion())) {
            champTeam = secondTeam;
            enemyTeam = firstTeam;
        }
        for (Damageable d : targets) {
            if (d instanceof Cover) {
                covers.add(d);
            } else if (champTeam.contains((Champion) d)) {
                friendly.add(d);
            } else if (enemyTeam.contains((Champion) d)) {
                enemies.add(d);
            }
        }
        if (a.getCurrentCooldown() == 0 && getCurrentChampion().getMana() >= a.getManaCost() && getCurrentChampion().getCurrentActionPoints() >= a.getRequiredActionPoints()) {
            if (a instanceof DamagingAbility || (a instanceof CrowdControlAbility && (((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF))) {
                if (!enemies.isEmpty()) {
                    a.execute(enemies);
                }
                if (!covers.isEmpty()) {
                    a.execute(covers);
                }
            } else if (a instanceof HealingAbility) {
                if (!friendly.isEmpty()) {
                    a.execute(friendly);
                }
            } else if (a instanceof CrowdControlAbility && (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
                if (!friendly.isEmpty()) {
                    a.execute(friendly);
                }
            }
            a.setCurrentCooldown(a.getBaseCooldown());
        } else if (a.getCurrentCooldown() > 0) {
            throw new AbilityUseException();
        } else if (getCurrentChampion().getMana() < a.getManaCost() || getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
            throw new NotEnoughResourcesException();
        }
    }

    public void castAbility(Ability a) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException {
        if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
            throw new NotEnoughResourcesException();
        } else {
            ArrayList<Damageable> targets = new ArrayList<>();
            ArrayList<Champion> firstTeam = firstPlayer.getTeam();
            ArrayList<Champion> secondTeam = secondPlayer.getTeam();
            ArrayList<Champion> champTeam = new ArrayList<>();
            ArrayList<Champion> enemyTeam = new ArrayList<>();

            if (firstTeam.contains(getCurrentChampion())) {
                champTeam = firstTeam;
                enemyTeam = secondTeam;
            } else if (secondTeam.contains(getCurrentChampion())) {
                champTeam = secondTeam;
                enemyTeam = firstTeam;
            }

            int x = getCurrentChampion().getLocation().x;
            int y = getCurrentChampion().getLocation().y;
            switch (a.getCastArea()) {
                case TEAMTARGET:
                    if (a instanceof CrowdControlAbility) {
                        if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                            targets.addAll(champTeam);
                        } else {
                            boolean flag = false;
                            if (firstTeam.contains(getCurrentChampion())) {
                                for (Champion d : secondTeam) {
                                    for (Effect e : d.getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            d.getAppliedEffects().remove(e);
                                        } else {
                                            flag = true;
                                            break;
                                        }
                                        break;
                                    }
                                    if (flag) {
                                        targets.add(d);
                                    }
                                }
                            } else if (secondTeam.contains(getCurrentChampion())) {
                                for (Champion d : firstTeam) {
                                    for (Effect e : d.getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            d.getAppliedEffects().remove(e);
                                        } else {
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        targets.add(d);
                                    }
                                }
                            }
                        }
                    } else if (a instanceof HealingAbility) {
                        for (Champion ch : champTeam) {
                            int x0 = ch.getLocation().x;
                            int y0 = ch.getLocation().y;
                            int distance = Math.abs(x - x0) + Math.abs(y - y0);
                            if (a.getCastRange() >= distance) {
                                targets.add(ch);
                            }
                        }
                    } else if (a instanceof DamagingAbility) {
                        for (Champion en : enemyTeam) {
                            boolean flag = false;
                            int x0 = en.getLocation().x;
                            int y0 = en.getLocation().y;
                            int distance = Math.abs(x - x0) + Math.abs(y - y0);
                            if (a.getCastRange() >= distance) {
//                                for (Effect eff : en.getAppliedEffects()) {
//                                    if (eff instanceof Shield) {
//                                        en.getAppliedEffects().remove(eff);
//                                    } else {
//                                        flag = true;
//                                        break;
//                                    }
//                                }
//                                if (flag) {
//                                    targets.add(en);
//                                }
                                targets.add(en);
                            }
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
                        if (a instanceof CrowdControlAbility) {
                            CrowdControlAbility cca = (CrowdControlAbility) a;
                            EffectType effT = cca.getEffect().getType();
                            if (effT == EffectType.BUFF) {
                                if (x < 4 && board[x + 1][y] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y])))
                                    targets.add((Damageable) board[x + 1][y]);
                                if (x < 4 && y < 4 && board[x + 1][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y + 1])))
                                    targets.add((Damageable) board[x + 1][y + 1]);
                                if (x < 4 && y > 0 && board[x + 1][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y - 1])))
                                    targets.add((Damageable) board[x + 1][y - 1]);
                                if (x > 0 && board[x - 1][y] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y])))
                                    targets.add((Damageable) board[x - 1][y]);
                                if (x > 0 && y < 4 && board[x - 1][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y + 1])))
                                    targets.add((Damageable) board[x - 1][y + 1]);
                                if (x > 0 && y > 0 && board[x - 1][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y - 1])))
                                    targets.add((Damageable) board[x - 1][y - 1]);
                                if (y < 4 && board[x][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y])))
                                    targets.add((Damageable) board[x + 1][y]);
                                if (y > 0 && board[x][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x][y - 1])))
                                    targets.add((Damageable) board[x][y - 1]);
                            } else if (effT == EffectType.DEBUFF) {
                                if (x < 4) {
                                    if (board[x + 1][y] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y])) {
                                        for (Effect e : ((Champion) board[x + 1][y]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x + 1][y]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x + 1][y]);
                                            }
                                        }
                                    }
                                    if (board[x + 1][y] instanceof Cover) {
                                        targets.add((Damageable) board[x + 1][y]);
                                    }
                                }
                                if (x < 4 && y < 4) {
                                    if (board[x + 1][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y + 1])) {
                                        for (Effect e : ((Champion) board[x + 1][y + 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x + 1][y + 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x + 1][y + 1]);
                                            }
                                        }
                                    }
                                    if (board[x + 1][y + 1] instanceof Cover) {
                                        targets.add((Damageable) board[x + 1][y + 1]);
                                    }
                                }
                                if (x < 4 && y > 0) {
                                    if (board[x + 1][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y - 1])) {
                                        for (Effect e : ((Champion) board[x + 1][y - 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x + 1][y - 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x + 1][y - 1]);
                                            }
                                        }
                                    }
                                    if (board[x + 1][y - 1] instanceof Cover) {
                                        targets.add((Damageable) board[x + 1][y - 1]);
                                    }
                                }
                                if (x > 0) {
                                    if (board[x - 1][y] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y])) {
                                        for (Effect e : ((Champion) board[x - 1][y]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x - 1][y]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x - 1][y]);
                                            }
                                        }
                                    }
                                    if (board[x - 1][y] instanceof Cover) {
                                        targets.add((Damageable) board[x - 1][y]);
                                    }
                                }
                                if (x > 0 && y < 4) {
                                    if (board[x - 1][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y + 1])) {
                                        for (Effect e : ((Champion) board[x - 1][y + 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x - 1][y + 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x - 1][y + 1]);
                                            }
                                        }
                                    }
                                    if (board[x - 1][y + 1] instanceof Cover) {
                                        targets.add((Damageable) board[x - 1][y + 1]);
                                    }
                                }
                                if (x > 0 && y > 0) {
                                    if (board[x - 1][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y - 1])) {
                                        for (Effect e : ((Champion) board[x - 1][y - 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x - 1][y - 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x - 1][y - 1]);
                                            }
                                        }
                                    }
                                    if (board[x - 1][y - 1] instanceof Cover) {
                                        targets.add((Damageable) board[x - 1][y - 1]);
                                    }
                                }
                                if (y < 4) {
                                    if (board[x][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x][y + 1])) {
                                        for (Effect e : ((Champion) board[x][y + 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x][y + 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x][y + 1]);
                                            }
                                        }
                                    }
                                    if (board[x][y + 1] instanceof Cover) {
                                        targets.add((Damageable) board[x][y + 1]);
                                    }
                                }
                                if (y > 0) {
                                    if (board[x][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x][y - 1])) {
                                        for (Effect e : ((Champion) board[x][y - 1]).getAppliedEffects()) {
                                            if (e instanceof Shield) {
                                                ((Champion) board[x][y - 1]).getAppliedEffects().remove(e);
                                            } else {
                                                targets.add((Damageable) board[x][y - 1]);
                                            }
                                        }
                                    }
                                    if (board[x][y - 1] instanceof Cover) {
                                        targets.add((Damageable) board[x][y - 1]);
                                    }
                                }
                            }
                        }

                        if (a instanceof HealingAbility) {
                            if (x < 4 && board[x + 1][y] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y])))
                                targets.add((Damageable) board[x + 1][y]);
                            if (x < 4 && y < 4 && board[x + 1][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y + 1])))
                                targets.add((Damageable) board[x + 1][y + 1]);
                            if (x < 4 && y > 0 && board[x + 1][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y - 1])))
                                targets.add((Damageable) board[x + 1][y - 1]);
                            if (x > 0 && board[x - 1][y] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y])))
                                targets.add((Damageable) board[x - 1][y]);
                            if (x > 0 && y < 4 && board[x - 1][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y + 1])))
                                targets.add((Damageable) board[x - 1][y + 1]);
                            if (x > 0 && y > 0 && board[x - 1][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x - 1][y - 1])))
                                targets.add((Damageable) board[x - 1][y - 1]);
                            if (y < 4 && board[x][y + 1] instanceof Damageable && (champTeam.contains((Champion) board[x + 1][y])))
                                targets.add((Damageable) board[x + 1][y]);
                            if (y > 0 && board[x][y - 1] instanceof Damageable && (champTeam.contains((Champion) board[x][y - 1])))
                                targets.add((Damageable) board[x][y - 1]);
                        }
                        if (a instanceof DamagingAbility) {
                            if (x < 4) {
                                if (board[x + 1][y] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y])) {
                                    for (Effect e : ((Champion) board[x + 1][y]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x + 1][y]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x + 1][y]);
                                        }
                                    }
                                }
                                if (board[x + 1][y] instanceof Cover) {
                                    targets.add((Damageable) board[x + 1][y]);
                                }
                            }
                            if (x < 4 && y < 4) {
                                if (board[x + 1][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y + 1])) {
                                    for (Effect e : ((Champion) board[x + 1][y + 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x + 1][y + 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x + 1][y + 1]);
                                        }
                                    }
                                }
                                if (board[x + 1][y + 1] instanceof Cover) {
                                    targets.add((Damageable) board[x + 1][y + 1]);
                                }
                            }
                            if (x < 4 && y > 0) {
                                if (board[x + 1][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x + 1][y - 1])) {
                                    for (Effect e : ((Champion) board[x + 1][y - 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x + 1][y - 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x + 1][y - 1]);
                                        }
                                    }
                                }
                                if (board[x + 1][y - 1] instanceof Cover) {
                                    targets.add((Damageable) board[x + 1][y - 1]);
                                }
                            }
                            if (x > 0) {
                                if (board[x - 1][y] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y])) {
                                    for (Effect e : ((Champion) board[x - 1][y]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x - 1][y]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x - 1][y]);
                                        }
                                    }
                                }
                                if (board[x - 1][y] instanceof Cover) {
                                    targets.add((Damageable) board[x - 1][y]);
                                }
                            }
                            if (x > 0 && y < 4) {
                                if (board[x - 1][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y + 1])) {
                                    for (Effect e : ((Champion) board[x - 1][y + 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x - 1][y + 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x - 1][y + 1]);
                                        }
                                    }
                                }
                                if (board[x - 1][y + 1] instanceof Cover) {
                                    targets.add((Damageable) board[x - 1][y + 1]);
                                }
                            }
                            if (x > 0 && y > 0) {
                                if (board[x - 1][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x - 1][y - 1])) {
                                    for (Effect e : ((Champion) board[x - 1][y - 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x - 1][y - 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x - 1][y - 1]);
                                        }
                                    }
                                }
                                if (board[x - 1][y - 1] instanceof Cover) {
                                    targets.add((Damageable) board[x - 1][y - 1]);
                                }
                            }
                            if (y < 4) {
                                if (board[x][y + 1] instanceof Champion && enemyTeam.contains((Champion) board[x][y + 1])) {
                                    for (Effect e : ((Champion) board[x][y + 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x][y + 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x][y + 1]);
                                        }
                                    }
                                }
                                if (board[x][y + 1] instanceof Cover) {
                                    targets.add((Damageable) board[x][y + 1]);
                                }
                            }
                            if (y > 0) {
                                if (board[x][y - 1] instanceof Champion && enemyTeam.contains((Champion) board[x][y - 1])) {
                                    for (Effect e : ((Champion) board[x][y - 1]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x][y - 1]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x][y - 1]);
                                        }
                                    }
                                }
                                if (board[x][y - 1] instanceof Cover) {
                                    targets.add((Damageable) board[x][y - 1]);
                                }
                            }
                        }
                    break;
            }
            executeHelper(a, targets);
            getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
            getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()
                    - a.getRequiredActionPoints());
        }
    }

    public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException {
        if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
            if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
                throw new NotEnoughResourcesException();
            } else { // <<<<<< ELSE ? >>>>>>>
                getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
                getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()
                        - a.getRequiredActionPoints());
                ArrayList<Damageable> targets = new ArrayList<>();
                ArrayList<Champion> firstTeam = firstPlayer.getTeam();
                ArrayList<Champion> secondTeam = secondPlayer.getTeam();
                ArrayList<Champion> enemyTeam = new ArrayList<>();
                if (firstTeam.contains(getCurrentChampion())) {
                    enemyTeam = secondTeam;
                } else if (secondTeam.contains(getCurrentChampion())) {
                    enemyTeam = firstTeam;
                }
                int x = getCurrentChampion().getLocation().x;
                int y = getCurrentChampion().getLocation().y;
                switch (d) {
                    case UP:
                        for (int i = y; i <= y + a.getCastRange() || i < getBoardheight(); i++) {
                            if (board[x][i] instanceof Damageable) {
                                if (board[x][i] instanceof Champion && enemyTeam.contains((Champion) board[x][i])) {
                                    for (Effect e : ((Champion) board[x][i]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x][i]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x][i]);
                                        }
                                    }
                                } else if ((board[x][i] instanceof Champion && !enemyTeam.contains((Champion) board[x][i])) || board[x][i] instanceof Cover) {
                                    targets.add((Damageable) board[x][i]);
                                }
                            }
                        }
                        break;
                    case RIGHT:
                        for (int i = x; i <= x + a.getCastRange() || i < getBoardwidth(); i++) {
                            if (board[i][y] instanceof Damageable) {
                                if (board[i][y] instanceof Champion && enemyTeam.contains((Champion) board[i][y])) {
                                    for (Effect e : ((Champion) board[i][y]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[i][y]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[i][y]);
                                        }
                                    }
                                } else if ((board[i][y] instanceof Champion && !enemyTeam.contains((Champion) board[i][y])) || board[i][y] instanceof Cover) {
                                    targets.add((Damageable) board[i][y]);
                                }
                            }
                        }
                        break;
                    case DOWN:
                        for (int i = y; i >= y - a.getCastRange() || i > 0; i--) {
                            if (board[x][i] instanceof Damageable) {
                                if (board[x][i] instanceof Champion && enemyTeam.contains((Champion) board[x][i])) {
                                    for (Effect e : ((Champion) board[x][i]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[x][i]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[x][i]);
                                        }
                                    }
                                } else if ((board[x][i] instanceof Champion && !enemyTeam.contains((Champion) board[x][i])) || board[x][i] instanceof Cover) {
                                    targets.add((Damageable) board[x][i]);
                                }
                            }
                        }
                        break;
                    case LEFT:
                        for (int i = x; i >= x - a.getCastRange() || i > 0; i--) {
                            if (board[i][y] instanceof Damageable) {
                                if (board[i][y] instanceof Champion && enemyTeam.contains((Champion) board[i][y])) {
                                    for (Effect e : ((Champion) board[i][y]).getAppliedEffects()) {
                                        if (e instanceof Shield) {
                                            ((Champion) board[i][y]).getAppliedEffects().remove(e);
                                        } else {
                                            targets.add((Damageable) board[i][y]);
                                        }
                                    }
                                } else if ((board[i][y] instanceof Champion && !enemyTeam.contains((Champion) board[i][y])) || board[i][y] instanceof Cover) {
                                    targets.add((Damageable) board[i][y]);
                                }
                            }
                        }
                        break;
                }
                executeHelper(a, targets);
            }
        }
    }

    public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException, InvalidTargetException {
        if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
            if (getCurrentChampion().getCurrentActionPoints() < a.getRequiredActionPoints()) {
                throw new NotEnoughResourcesException();
            } else {
                getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
                getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()
                        - a.getRequiredActionPoints());
                ArrayList<Damageable> targets = new ArrayList<>();
                ArrayList<Champion> firstTeam = firstPlayer.getTeam();
                ArrayList<Champion> secondTeam = secondPlayer.getTeam();
                ArrayList<Champion> enemyTeam = new ArrayList<>();
                if (firstTeam.contains(getCurrentChampion())) {
                    enemyTeam = secondTeam;
                } else if (secondTeam.contains(getCurrentChampion())) {
                    enemyTeam = firstTeam;
                }
                int x0 = getCurrentChampion().getLocation().x;
                int y0 = getCurrentChampion().getLocation().y;
                int distance = Math.abs(x - x0) + Math.abs(y - y0);
                if (distance <= a.getCastRange()) {
                    if (board[x][y] instanceof Champion && enemyTeam.contains((Champion) board[x][y]) && board[x][y] != getCurrentChampion()) {
                        boolean flag = false;
                        for (Effect e : ((Champion) board[x][y]).getAppliedEffects()) {
                            if (e instanceof Shield) {
                                ((Champion) board[x][y]).getAppliedEffects().remove(e);
                            } else {
                                flag = true;
                            }
                        }
                        if (flag) {
                            targets.add((Damageable) board[x][y]);
                        }
                    } else if ((board[x][y] instanceof Champion && !enemyTeam.contains((Champion) board[x][y])) || board[x][y] instanceof Cover) {
                        targets.add((Damageable) board[x][y]);
                    }
                    if (board[x][y] == getCurrentChampion()) {
                        throw new InvalidTargetException();
                    }
                }
                executeHelper(a, targets);
            }
        }
    }

    public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException {
        ArrayList<Champion> targets = new ArrayList<>();
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();
        if ((firstTeam.contains(getCurrentChampion()) && firstLeaderAbilityUsed) || (secondTeam.contains(getCurrentChampion()) && secondLeaderAbilityUsed)) {
            throw new LeaderAbilityAlreadyUsedException();
        } else {
            if (getCurrentChampion() == firstPlayer.getLeader() || getCurrentChampion() == secondPlayer.getLeader()) {
                if (getCurrentChampion() == firstPlayer.getLeader()) {
                    targets.addAll(secondTeam);
                } else if (getCurrentChampion() == secondPlayer.getLeader()) {
                    targets.addAll(firstTeam);
                }
                getCurrentChampion().useLeaderAbility(targets);

                if (getCurrentChampion() == firstPlayer.getLeader()) {
                    firstLeaderAbilityUsed = true;
                } else {
                    secondLeaderAbilityUsed = true;
                }

            } else {
                throw new LeaderNotCurrentException();
            }
        }
    }

    public void endTurn() {
        turnOrder.remove();
        for (Effect eff : getCurrentChampion().getAppliedEffects()) {
            if (!getCurrentChampion().getAppliedEffects().isEmpty()) {
                eff.setDuration(eff.getDuration() - 1);
                if (eff.getDuration() == 0) {
                    eff.remove(getCurrentChampion());
                }
            }
        }
        for (Ability a : getCurrentChampion().getAbilities()) {
            if (a.getCurrentCooldown() > 0) {
                a.setCurrentCooldown(a.getCurrentCooldown() - 1);
            } else {
                a.setCurrentCooldown(0);
            }
        }

        if (turnOrder.isEmpty()) {
            prepareChampionTurns();
        } else while (getCurrentChampion().getCondition() == Condition.INACTIVE) {
            turnOrder.remove();
            if (turnOrder.isEmpty())
                prepareChampionTurns();
        }
        getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getMaxActionPointsPerTurn());
    }

    private void prepareChampionTurns() {
        ArrayList<Champion> firstTeam = firstPlayer.getTeam();
        ArrayList<Champion> secondTeam = secondPlayer.getTeam();
        for (Champion champ : firstTeam) {
            if (champ.getCondition() != Condition.KNOCKEDOUT)
                turnOrder.insert(champ);
        }
        for (Champion champ : secondTeam) {
            if (champ.getCondition() != Condition.KNOCKEDOUT)
                turnOrder.insert(champ);
        }
    }
}