package entity;

import java.awt.image.BufferedImage;

public class Entity {
    protected int x, y, speed;
    
    protected BufferedImage u1, u2, l1, l2, r1, r2, d1, d2;
    protected String direction;
    
    protected int spriteCounter = 0;
    public int spriteNum = 1;
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
}
