package model.world;

import model.effects.Stun;

import java.util.ArrayList;

public class AntiHero extends Champion {
    public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
        super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);
    }

    public void useLeaderAbility(ArrayList<Champion> targets) {
        Stun stn = new Stun(2);
        for (Champion target : targets) {
            stn.apply(target);
        }
    }
}
