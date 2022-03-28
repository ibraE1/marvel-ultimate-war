package model.abilities;

public class Ability {

    private String name;
    private int manaCost;
    private int baseCooldown;
    private int currentCooldown;
    private int castRange;
    private int requiredActionPoints;
    private AreaOfEffect castArea;

    public Ability(String name,int cost, int baseCoolDown, int castRange, AreaOfEffect area , int required){
        this.name = name;
        this.baseCooldown = baseCoolDown;
        this.castRange = castRange;
        castArea = area;
        requiredActionPoints = required;
        manaCost = cost;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    public int getCastRange() {
        return castRange;
    }

    public AreaOfEffect getCastArea() {
        return castArea;
    }

    public int getRequiredActionPoints() {
        return requiredActionPoints;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int currentCooldown) {
        this.currentCooldown = currentCooldown;
    }

}