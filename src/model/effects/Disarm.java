package model.effects;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {

    public Disarm(int duration) {
        super("Disarm", duration, EffectType.DEBUFF);

    }

    public void apply(Champion c) {
        int dmg = c.getAttackDamage();
        c.setAttackDamage(0);
        DamagingAbility punch = new DamagingAbility("Punch", 0,1,1, AreaOfEffect.SINGLETARGET,1,50);
        c.getAbilities().add(punch);
        if (p.getCurrentCooldown() == 0) {

        }
    }

    public void remove(Champion c, int dmg) {
        c.getAbilities().remove(c.getAbilities().size()-1);
        // c.setAttackDamage();
    }

}
