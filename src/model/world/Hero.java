package model.world;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

import java.util.ArrayList;

public class Hero extends Champion {
    public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
        super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

    }

    public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
        for (Champion target : targets) {
            for (int i = 0; i < target.getAppliedEffects().size(); i++) {
                Effect eft = target.getAppliedEffects().get(i);
                if (eft.getType() == EffectType.DEBUFF) {
                    eft.remove(target);
                    target.getAppliedEffects().remove(eft);
                    i--;
                }
            }
            Embrace em = new Embrace(2);
            Embrace e = (Embrace) em.clone();
            e.apply(target);
            target.getAppliedEffects().add(e);
        }
    }
}
