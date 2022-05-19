package model.abilities;

import model.effects.Effect;
import model.effects.Shield;
import model.world.Champion;
import model.world.Damageable;


import java.util.ArrayList;

public class DamagingAbility extends Ability {
    private int damageAmount;

    public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required, int damageAmount) {
        super(name, cost, baseCoolDown, castRadius, area, required);
        this.damageAmount = damageAmount;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }

    public void execute(ArrayList<Damageable> targets) {
        for (Damageable target : targets) {
            boolean shield = false;
            if (target instanceof Champion) {
                Champion t = (Champion) target;
                for (int i = 0; i < t.getAppliedEffects().size(); i++) {
                    Effect eft = t.getAppliedEffects().get(i);
                    if (eft instanceof Shield) {
                        i--;
                        eft.remove(t);
                        t.getAppliedEffects().remove(eft);
                        shield = true;
                    }
                }
            }
            if (!shield)
                target.setCurrentHP(target.getCurrentHP() - damageAmount);
        }
    }
}
