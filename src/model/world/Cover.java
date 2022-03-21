package model.world;
import java.awt.*;
public class Cover {

    private int currentHP;
    private Point location;

    public Cover(int x, int y){
        Point location = new Point(x,y);
        int hp = (int) ((Math.random() * 1000) + 1);
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public Point getLocation() {
        return location;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }
}
