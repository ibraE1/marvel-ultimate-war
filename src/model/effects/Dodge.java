package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

    public Dodge(int duration) {
        super("Dodge", duration, EffectType.BUFF);

    }

    public void apply(Champion c) {
        c.setSpeed((int) (c.getSpeed() * 1.05));
        int health = c.getCurrentHP();
    }

    public void remove(Champion c) {
        int random = (int) (Math.random() * 2);
        if (random == 1)  {
            String hima = "clown"; // placeholder
        }
        c.setSpeed((int) (c.getSpeed() / 1.05));
    }

}