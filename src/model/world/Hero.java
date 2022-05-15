package model.world;

import model.effects.Embrace;

import java.util.ArrayList;

public class Hero extends Champion {
    public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
        super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

    }

    public void useLeaderAbility(ArrayList<Champion> targets) {
        Embrace em = new Embrace(2);
        for (Champion target : targets) {
            em.apply(target);
        }
    }
}
