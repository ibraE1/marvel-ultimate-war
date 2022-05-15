package model.world;

import java.util.ArrayList;

public class Villain extends Champion {
    public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
        super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);
    }

    public void useLeaderAbility(ArrayList<Champion> targets) {
        for (Champion target : targets) {
            if ((target.getCurrentHP() / target.getMaxHP()) == 1/3) {
                target.setCondition(Condition.KNOCKEDOUT);
            }
        }
    }
}
