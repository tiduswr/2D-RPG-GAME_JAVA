package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import util.UtilityTool;
import tile.TileManager;

public abstract class Entity {
    protected int worldX, worldY, speed;
    
    protected GamePanel gp;
    protected BufferedImage u1, u2, l1, l2, r1, r2, d1, d2;
    protected String direction;
    protected Rectangle solidArea;
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected boolean collisionOn = false;
    protected Stroke collisionRectStroke = new BasicStroke(2);
    protected int actionLockCounter;
    
    //Char stats
    protected String[] dialogues;
    protected int maxLife, life;
    
    public Entity(GamePanel gp){
        this.gp = gp;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    public BufferedImage makePlayerSprite(String imgPath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;
        
        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream("/" + imgPath));
            scaledImage = uTool.scaleImage(scaledImage, gp.getTileSize(), gp.getTileSize());
        } catch (IOException ex) {
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return scaledImage;
    }
    
    public void setAction(){}
    public void speak(){
    
        switch(gp.getPlayer().getDirection()){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    
    };
    public void update(){
        setAction();
        
        //Check tile Collision
        checkTileCollision();
        //Check object collision
        checkObjectCollision();
        //Check Player Collision
        gp.getcChecker().checkPlayer(this);
        //Check NPC collision
        gp.getcChecker().checkEntity(this, gp.getNpcs());
        
        checkDirection();
        controlSpriteAnimationSpeed();
    }
    
    protected int checkObjectCollision(){
        return gp.getcChecker().checkObject(this, false);
    }
    
    protected void checkTileCollision(){
        //Check Tile Collision
        collisionOn = false;
        gp.getcChecker().checkTile(this);
    }
    
    protected void controlSpriteAnimationSpeed(){
        //Update sprites
        spriteCounter++;
        if(spriteCounter > (int) 12/(60/gp.getFPS())){
            if(spriteNum == 1){
                spriteNum = 2;
            }else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    protected void checkDirection(){
        //Only move if collisionOn is true
        if(!collisionOn){
            switch(direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                   worldY += speed;
                   break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
    }
    
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
            worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
            worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
            
            BufferedImage image = getSpriteDirection();
            
            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
            if(gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
        }
    }
    
    protected BufferedImage getSpriteDirection(){
        BufferedImage image = null;
        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = u1;
                }else if(spriteNum == 2){
                    image = u2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = d1;
                }else if(spriteNum == 2){
                    image = d2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = l1;
                }else if(spriteNum == 2){
                    image = l2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = r1;
                }else if(spriteNum == 2){
                    image = r2;
                }
                break;
            default:
                break;
        }
        return image;
    }
    
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
    public Rectangle getSolidArea() {
        return solidArea;
    }
    public boolean isCollisionOn() {
        return collisionOn;
    }
    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }
    public String getDirection() {
        return direction;
    }
    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }
    public void setSolidAreaDefaultX(int solidAreaDefaultX) {
        this.solidAreaDefaultX = solidAreaDefaultX;
    }
    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }
    public void setSolidAreaDefaultY(int solidAreaDefaultY) {
        this.solidAreaDefaultY = solidAreaDefaultY;
    }
    
    protected void drawCollision(Graphics2D g2, int screenX, int screenY){
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

    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }
    
}
