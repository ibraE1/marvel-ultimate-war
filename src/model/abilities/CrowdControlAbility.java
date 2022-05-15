package model.abilities;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

import java.util.ArrayList;

public class CrowdControlAbility extends Ability {
    private Effect effect;

    public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
                               Effect effect) {
        super(name, cost, baseCoolDown, castRadius, area, required);
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    public void execute(ArrayList<Damageable> targets) {
        for (Damageable target : targets) {
            Champion champ = (Champion) target;
            effect.apply(champ);
        }
    }
}
