package object;

import entity.Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class SuperObject implements Action{
    protected BufferedImage image;
    protected String name;
    protected boolean collision;
    protected int worldX, worldY;
    protected Rectangle solidArea;
    protected GamePanel gp;
    private int solidAreaDefaultX, solidAreaDefaultY;
    
    public SuperObject(GamePanel gp){
        this.gp = gp;
        solidArea = new Rectangle(0,0,gp.getTileSize(),gp.getTileSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        collision = false;
    }
    
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
            worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
            worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }
    public String getName() {
        return name;
    }
    public boolean isCollision() {
        return collision;
    }
    public int getWorldX() {
        return worldX;
    }
    public int getWorldY() {
        return worldY;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }
    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }
    public Rectangle getSolidArea() {
        return solidArea;
    }
    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }
    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    @Override
    public boolean executeAction() {
        return false;
    }
    
}
