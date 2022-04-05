package model.world;

import model.abilities.Ability;
import model.effects.Effect;

import java.awt.*;
import java.util.ArrayList;

public class Champion {

    private String name;
    private int maxHP;
    private int currentHP;
    private int mana;
    private int maxActionPointsPerTurn;
    private int currentActionPoints;
    private int attackRange;
    private int attackDamage;
    private int speed;
    private ArrayList<Ability> abilities;
    private ArrayList<Effect> appliedEffects;
    private Condition condition;
    private Point location;

    public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
        this.name = name;
        this.maxHP = maxHP;
        this.mana = mana;
        this.speed = speed;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        condition = Condition.ACTIVE;
        maxActionPointsPerTurn = maxActions;
        currentHP = maxHP;
        currentActionPoints = maxActions;
        abilities = new ArrayList<Ability>(3);
        appliedEffects = new ArrayList<Effect>();
    }

    public String getName() {
        return name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxActionPointsPerTurn() {
        return maxActionPointsPerTurn;
    }

    public int getCurrentActionPoints() {
        return currentActionPoints;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getSpeed() {
        return speed;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public ArrayList<Effect> getAppliedEffects() {
        return appliedEffects;
    }

    public Condition getCondition() {
        return condition;
    }

    public Point getLocation() {
        return location;
    }

    public void setCurrentHP(int currentHP) {
        if (currentHP >= 0 && currentHP <= maxHP) {
            this.currentHP = currentHP;
        } else if (currentHP < 0) {
            this.currentHP = 0;
        } else {
            this.currentHP = maxHP;
        }
    }

    public void setMana(int mana) { this.mana = mana; }

    public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
        this.maxActionPointsPerTurn = maxActionPointsPerTurn;
    }

    public void setCurrentActionPoints(int currentActionPoints) {
        if(currentActionPoints > maxActionPointsPerTurn) {
            this.currentActionPoints = maxActionPointsPerTurn;
        }else if(currentActionPoints < 0){
            this.currentActionPoints = 0;
        }else{
            this.currentActionPoints = currentActionPoints;
        }
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

}
