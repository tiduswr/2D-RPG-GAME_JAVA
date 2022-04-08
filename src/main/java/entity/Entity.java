package entity;

import java.awt.image.BufferedImage;

public class Entity {
    protected int worldX, worldY, speed;
    
    protected BufferedImage u1, u2, l1, l2, r1, r2, d1, d2;
    protected String direction;
    
    protected int spriteCounter = 0;
    public int spriteNum = 1;

    public int getWorldX() {
        return worldX;
    }
    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }
    public int getWorldY() {
        return worldY;
    }
    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
