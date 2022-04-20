package object;

import entity.Player;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import main.GamePanel;

public abstract class SuperObject implements Action{
    protected BufferedImage image;
    protected String name;
    protected boolean collision;
    protected int worldX, worldY;
    protected Rectangle solidArea;
    protected GamePanel gp;
    private int solidAreaDefaultX, solidAreaDefaultY;
    private final Stroke collisionRectStroke;
    
    public SuperObject(GamePanel gp){
        this.gp = gp;
        solidArea = new Rectangle(0,0,gp.getTileSize(),gp.getTileSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        collision = false;
        collisionRectStroke = new BasicStroke(2);
    }
    
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
            worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
            worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
            if(gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
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
    
    private void drawCollision(Graphics2D g2, int screenX, int screenY){
        //Desenha a colisão
        Stroke oldStroke = g2.getStroke();
        Font oldFont = g2.getFont();
        g2.setStroke(collisionRectStroke);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        g2.setStroke(oldStroke);
        g2.setFont(gp.getGameUI().getfreePixel_40().deriveFont(12F));
        g2.drawString("X: " + worldX/gp.getTileSize() + " Y: " + worldY/gp.getTileSize(), screenX, screenY - 1);
        g2.setFont(g2.getFont());
    }
}
