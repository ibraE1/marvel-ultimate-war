package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

import java.util.ArrayList;

public class PowerUp extends Effect {

    public PowerUp(int duration) {
        super("PowerUp", duration, EffectType.BUFF);
    }

    @Override
    public void apply(Champion c) {
        ArrayList<Ability> a = c.getAbilities();
        for (int i = 0; i < a.size(); i++) {
            Ability currAbility = a.get(i);
            if (currAbility instanceof DamagingAbility) {
                ((DamagingAbility) currAbility).setDamageAmount((int) (((DamagingAbility) currAbility).getDamageAmount() * 1.2));
            } else if (currAbility instanceof HealingAbility) {
                ((HealingAbility) currAbility).setHealAmount((int) (((HealingAbility) currAbility).getHealAmount() * 1.2));
            }
        }
    }

    @Override
    public void remove(Champion c) {
        ArrayList<Ability> a = c.getAbilities();
        for (int i = 0; i < a.size(); i++) {
            Ability currAbility = a.get(i);
            if (currAbility instanceof DamagingAbility) {
                ((DamagingAbility) currAbility).setDamageAmount((int) (((DamagingAbility) currAbility).getDamageAmount() / 1.2));
            } else if (currAbility instanceof HealingAbility) {
                ((HealingAbility) currAbility).setHealAmount((int) (((HealingAbility) currAbility).getHealAmount() / 1.2));
            }
        }
    }
}
