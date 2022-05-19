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
                def.getAppliedEffects().remove(eft);
                return 0;
            }
            if (eft instanceof Dodge) {
                if ( (int) (Math.random() * 2) == 0) {
                    eft.remove(def);
                    def.getAppliedEffects().remove(eft);
                    return 0;
                }
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

    public boolean checkNotAlly(Champion attacker, Damageable defender) {
        if (defender instanceof Cover)
            return true;
        else {
            Champion def = (Champion) defender;
            ArrayList<Champion> allies;
            if (firstPlayer.getTeam().contains(attacker))
                allies = firstPlayer.getTeam();
            else
                allies = secondPlayer.getTeam();
            return !allies.contains(def);
        }
    }

    public void handleKnockouts(Damageable dmg) {
        Point location = dmg.getLocation();
        PriorityQueue order = getTurnOrder();
        if (dmg.getCurrentHP() == 0) {
            board[location.x][location.y] = null;
            if (dmg instanceof Champion) {
                Champion champ = (Champion) dmg;
                PriorityQueue pq = new PriorityQueue(order.size());
                int size1 = order.size();
                for (int i = 0; i < size1; i++) {
                    Champion temp = (Champion) order.remove();
                    if (temp != champ)
                        pq.insert(temp);
                }
                int size2 = pq.size();
                for (int i = 0; i < size2; i++) {
                    order.insert(pq.remove());
                }
                if (firstPlayer.getTeam().contains(champ))
                    firstPlayer.getTeam().remove(champ);
                else
                    secondPlayer.getTeam().remove(champ);
            }
        }
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
                    for (int i = 1; i <= range && location.x + i < 5; i++) {
                        Object tile = board[location.x + i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkNotAlly(champ, dmg)) {
                                dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                                handleKnockouts(dmg);
                                break;
                            }
                        }
                    }
                    break;
                case DOWN:
                    for (int i = 1; i <= range && location.x - i > -1; i++) {
                        Object tile = board[location.x - i][location.y];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkNotAlly(champ, dmg)){
                                dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                                handleKnockouts(dmg);
                                break;
                            }
                        }
                    }
                    break;
                case LEFT:
                    for (int i = 1; i <= range && location.y - i > -1; i++) {
                        Object tile = board[location.x][location.y - i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkNotAlly(champ, dmg)) {
                                dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                                handleKnockouts(dmg);
                                break;
                            }
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = 1; i <= range && location.y + i < 5; i++) {
                        Object tile = board[location.x][location.y + i];
                        if (tile instanceof Damageable) {
                            Damageable dmg = (Damageable) tile;
                            if (checkNotAlly(champ, dmg)) {
                                dmg.setCurrentHP(dmg.getCurrentHP() - calculateDamage(champ, dmg));
                                handleKnockouts(dmg);
                                break;
                            }
                        }
                    }
                    break;
            }

        }
    }

    public boolean manhattan(Point casterLocation, Point targetLocation, int range) {
        return (Math.abs(casterLocation.x - targetLocation.x) + Math.abs(casterLocation.y - targetLocation.y)) <= range;
    }

    public boolean euclidean(Champion caster, Damageable target) {
        Point casterLocation = caster.getLocation();
        Point targetLocation = target.getLocation();
        return (Math.pow(casterLocation.x - targetLocation.x, 2) + Math.pow(casterLocation.y - targetLocation.y, 2)) <= 2;
    }

    public ArrayList<Cover> getCovers() {
        ArrayList<Cover> covers = new ArrayList<Cover>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] instanceof Cover)
                    covers.add((Cover) board[i][j]);
            }
        }
        return covers;
    }

    public void castAbility(Ability a) throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException {
        Champion champ = getCurrentChampion();
        ArrayList<Champion> allies;
        ArrayList<Champion> enemies;
        ArrayList<Cover> covers = getCovers();
        if (firstPlayer.getTeam().contains(champ)) {
            allies = firstPlayer.getTeam();
            enemies = secondPlayer.getTeam();
        } else {
            enemies = firstPlayer.getTeam();
            allies = secondPlayer.getTeam();
        }
        for (Effect eft : champ.getAppliedEffects()) {
            if (eft instanceof Silence)
                throw new AbilityUseException();
        }
        if (a.getCurrentCooldown() != 0)
            throw new AbilityUseException();
        if (champ.getCurrentActionPoints() < a.getRequiredActionPoints() || champ.getMana() < a.getManaCost())
            throw new NotEnoughResourcesException();
        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        int range = a.getCastRange();
        switch (a.getCastArea()) {
            case SELFTARGET:
                targets.add(champ);
                break;
            case TEAMTARGET:
                if (a instanceof DamagingAbility) {
                    for (Champion enemy : enemies) {
                        if (manhattan(champ.getLocation(), enemy.getLocation(), range))
                            targets.add(enemy);
                    }
                } else if (a instanceof HealingAbility) {
                    for (Champion ally : allies) {
                        if (manhattan(champ.getLocation(), ally.getLocation(), range))
                            targets.add(ally);
                    }
                } else if (a instanceof CrowdControlAbility) {
                    CrowdControlAbility cc = (CrowdControlAbility) a;
                    if (cc.getEffect().getType() == EffectType.BUFF) {
                        for (Champion ally : allies) {
                            if (manhattan(champ.getLocation(), ally.getLocation(), range))
                                targets.add(ally);
                        }
                    } else {
                        for (Champion enemy : enemies) {
                            if (manhattan(champ.getLocation(), enemy.getLocation(), range))
                                targets.add(enemy);
                        }
                    }
                }
                break;
            case SURROUND:
                if (a instanceof DamagingAbility) {
                    for (Champion enemy : enemies) {
                        if (euclidean(champ, enemy))
                            targets.add(enemy);
                    }
                    for (Cover cv : covers) {
                        if (euclidean(champ, cv))
                            targets.add(cv);
                    }
                } else if (a instanceof HealingAbility) {
                    for (Champion ally : allies) {
                        if (euclidean(champ, ally) && ally != champ)
                            targets.add(ally);
                    }
                } else if (a instanceof CrowdControlAbility) {
                    CrowdControlAbility cc = (CrowdControlAbility) a;
                    if (cc.getEffect().getType() == EffectType.BUFF) {
                        for (Champion ally : allies) {
                            if (euclidean(champ, ally) && ally != champ)
                                targets.add(ally);
                        }
                    } else {
                        for (Champion enemy : enemies) {
                            if (euclidean(champ, enemy))
                                targets.add(enemy);
                        }
                    }
                }
                break;
        }
        a.execute(targets);
        for (Damageable target : targets)
            handleKnockouts(target);
        a.setCurrentCooldown(a.getBaseCooldown());
        champ.setCurrentActionPoints(champ.getCurrentActionPoints() - a.getRequiredActionPoints());
        champ.setMana(champ.getMana() - a.getManaCost());
    }

    public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException {
        Champion champ = getCurrentChampion();
        ArrayList<Champion> allies;
        ArrayList<Champion> enemies;
        ArrayList<Cover> covers = getCovers();
        if (firstPlayer.getTeam().contains(champ)) {
            allies = firstPlayer.getTeam();
            enemies = secondPlayer.getTeam();
        } else {
            enemies = firstPlayer.getTeam();
            allies = secondPlayer.getTeam();
        }
        for (Effect eft : champ.getAppliedEffects()) {
            if (eft instanceof Silence)
                throw new AbilityUseException();
        }
        if (a.getCurrentCooldown() != 0)
            throw new AbilityUseException();
        if (champ.getCurrentActionPoints() < a.getRequiredActionPoints() || champ.getMana() < a.getManaCost())
            throw new NotEnoughResourcesException();
        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        int range = a.getCastRange();
        Point location = champ.getLocation();
        switch (d) {
            case UP:
                if (a instanceof DamagingAbility) {
                    for (int i = location.x + 1; i < 5; i++) {
                        Damageable target = (Damageable) board[i][location.y];
                        for (Champion enemy : enemies) {
                            if (target == enemy)
                                targets.add(enemy);
                        }
                        for (Cover cv : covers) {
                            if (target == cv)
                                targets.add(cv);
                        }
                    }
                } else if (a instanceof HealingAbility) {
                    for (int i = location.x + 1; i < 5; i++) {
                        Damageable target = (Damageable) board[i][location.y];
                        for (Champion ally : allies) {
                            if (target == ally)
                                targets.add(ally);
                        }
                    }
                } else if (a instanceof CrowdControlAbility) {
                    if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                        for (int i = location.x + 1; i < 5; i++) {
                            Damageable target = (Damageable) board[i][location.y];
                            for (Champion ally : allies) {
                                if (target == ally)
                                    targets.add(ally);
                            }
                        }
                    } else {
                        for (int i = location.x + 1; i < 5; i++) {
                            Damageable target = (Damageable) board[i][location.y];
                            for (Champion enemy : enemies) {
                                if (target == enemy)
                                    targets.add(enemy);
                            }
                        }
                    }
                }
                break;
            case DOWN:
                if (a instanceof DamagingAbility) {
                    for (int i = location.x - 1; i >= 0; i--) {
                        Damageable target = (Damageable) board[i][location.y];
                        for (Champion enemy : enemies) {
                            if (target == enemy)
                                targets.add(enemy);
                        }
                        for (Cover cv : covers) {
                            if (target == cv)
                                targets.add(cv);
                        }
                    }
                } else if (a instanceof HealingAbility) {
                    for (int i = location.x - 1; i >= 0; i--) {
                        Damageable target = (Damageable) board[i][location.y];
                        for (Champion ally : allies) {
                            if (target == ally)
                                targets.add(ally);
                        }
                    }
                } else if (a instanceof CrowdControlAbility) {
                    if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                        for (int i = location.x - 1; i >= 0; i--) {
                            Damageable target = (Damageable) board[i][location.y];
                            for (Champion ally : allies) {
                                if (target == ally)
                                    targets.add(ally);
                            }
                        }
                    } else {
                        for (int i = location.x - 1; i >= 0; i--) {
                            Damageable target = (Damageable) board[i][location.y];
                            for (Champion enemy : enemies) {
                                if (target == enemy)
                                    targets.add(enemy);
                            }
                        }
                    }
                }
                break;
            case LEFT:
                if (a instanceof DamagingAbility) {
                    for (int i = location.y - 1; i >= 0; i--) {
                        Damageable target = (Damageable) board[location.x][i];
                        for (Champion enemy : enemies) {
                            if (target == enemy)
                                targets.add(enemy);
                        }
                        for (Cover cv : covers) {
                            if (target == cv)
                                targets.add(cv);
                        }
                    }
                } else if (a instanceof HealingAbility) {
                    for (int i = location.y - 1; i >= 0; i--) {
                        Damageable target = (Damageable) board[location.x][i];
                        for (Champion ally : allies) {
                            if (target == ally)
                                targets.add(ally);
                        }
                    }
                } else if (a instanceof CrowdControlAbility) {
                    if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                        for (int i = location.y - 1; i >= 0; i--) {
                            Damageable target = (Damageable) board[location.x][i];
                            for (Champion ally : allies) {
                                if (target == ally)
                                    targets.add(ally);
                            }
                        }
                    } else {
                        for (int i = location.y - 1; i >= 0; i--) {
                            Damageable target = (Damageable) board[location.x][i];
                            for (Champion enemy : enemies) {
                                if (target == enemy)
                                    targets.add(enemy);
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                if (a instanceof DamagingAbility) {
                    for (int i = location.y + 1; i < 5; i++) {
                        Damageable target = (Damageable) board[location.x][i];
                        for (Champion enemy : enemies) {
                            if (target == enemy)
                                targets.add(enemy);
                        }
                        for (Cover cv : covers) {
                            if (target == cv)
                                targets.add(cv);
                        }
                    }
                } else if (a instanceof HealingAbility) {
                    for (int i = location.y + 1; i < 5; i++) {
                        Damageable target = (Damageable) board[location.x][i];
                        for (Champion ally : allies) {
                            if (target == ally)
                                targets.add(ally);
                        }
                    }
                } else if (a instanceof CrowdControlAbility) {
                    if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                        for (int i = location.y + 1; i < 5; i++) {
                            Damageable target = (Damageable) board[location.x][i];
                            for (Champion ally : allies) {
                                if (target == ally)
                                    targets.add(ally);
                            }
                        }
                    } else {
                        for (int i = location.y + 1; i < 5; i++) {
                            Damageable target = (Damageable) board[location.x][i];
                            for (Champion enemy : enemies) {
                                if (target == enemy)
                                    targets.add(enemy);
                            }
                        }
                    }
                }
                break;
        }
        a.execute(targets);
        for (Damageable target : targets)
            handleKnockouts(target);
        a.setCurrentCooldown(a.getBaseCooldown());
        champ.setCurrentActionPoints(champ.getCurrentActionPoints() - a.getRequiredActionPoints());
        champ.setMana(champ.getMana() - a.getManaCost());
    }

    public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, CloneNotSupportedException, AbilityUseException, InvalidTargetException {
        Champion champ = getCurrentChampion();
        ArrayList<Champion> allies;
        ArrayList<Champion> enemies;
        ArrayList<Cover> covers = getCovers();
        if (firstPlayer.getTeam().contains(champ)) {
            allies = firstPlayer.getTeam();
            enemies = secondPlayer.getTeam();
        } else {
            enemies = firstPlayer.getTeam();
            allies = secondPlayer.getTeam();
        }
        for (Effect eft : champ.getAppliedEffects()) {
            if (eft instanceof Silence)
                throw new AbilityUseException();
        }
        if (a.getCurrentCooldown() != 0)
            throw new AbilityUseException();
        if (champ.getCurrentActionPoints() < a.getRequiredActionPoints() || champ.getMana() < a.getManaCost())
            throw new NotEnoughResourcesException();
        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        int range = a.getCastRange();
        Damageable target = (Damageable) board[x][y];
        if (a instanceof DamagingAbility) {
            if (!manhattan(champ.getLocation(), new Point(x, y), range))
                throw new AbilityUseException();
            if (target == champ)
                throw new InvalidTargetException();
            for (Champion enemy : enemies) {
                if (target == enemy)
                    targets.add(enemy);
            }
            for (Cover cv : covers) {
                if (target == cv)
                    targets.add(cv);
            }
        } else if (target instanceof Cover)
            throw new InvalidTargetException();
        else if (a instanceof HealingAbility) {
            if (target == null)
                throw new InvalidTargetException();
            if (!manhattan(champ.getLocation(), new Point(x, y), range))
                throw new AbilityUseException();
            for (Champion ally : allies) {
                if (target == ally)
                    targets.add(ally);
            }
        } else if (a instanceof CrowdControlAbility) {
            if (!manhattan(champ.getLocation(), new Point(x, y), range))
                throw new AbilityUseException();
            if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF) {
                for (Champion enemy : enemies) {
                    if (target == enemy)
                        throw new InvalidTargetException();
                }
                for (Champion ally : allies) {
                    if (target == ally)
                        targets.add(ally);
                }
            } else {
                if (target == champ)
                    throw new InvalidTargetException();
                for (Champion enemy : enemies) {
                    if (target == enemy)
                        targets.add(enemy);
                }
            }
        }
        a.execute(targets);
        handleKnockouts(target);
        a.setCurrentCooldown(a.getBaseCooldown());
        champ.setCurrentActionPoints(champ.getCurrentActionPoints() - a.getRequiredActionPoints());
        champ.setMana(champ.getMana() - a.getManaCost());
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