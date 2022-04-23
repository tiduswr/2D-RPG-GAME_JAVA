package entity;

import java.awt.AlphaComposite;
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
import main.Drawnable;
import main.GamePanel;
import main.WorldLocation;
import util.UtilityTool;
import tile.TileManager;

public abstract class Entity implements Drawnable{
    
    protected GamePanel gp;    
    
    //Entity sprites
    protected BufferedImage u1, u2, l1, l2, r1, r2, d1, d2;
    protected BufferedImage atkU1, atkU2, atkL1, atkL2, atkR1, atkR2, atkD1, atkD2;
    
    //Collision check and localization
    protected int worldX, worldY, speed;
    protected Rectangle solidArea, attackArea = new Rectangle(0, 0, 0, 0);
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected boolean collisionOn = false;
    protected Stroke collisionRectStroke = new BasicStroke(2);
    
    //Attacking
    protected boolean attacking = false;
    
    //IA
    protected int actionLockCounter;
    
    //Entity Basic
    protected String name;
    protected String direction;
    
    //Entity Type Handler
    protected EntityType type;
    
    //Invincible Handler
    protected int invincibleCounter = 0;
    protected boolean invincible = false;
    
    //Char stats
    protected String[] dialogues;
    protected int maxLife, life;
    
    public Entity(GamePanel gp, EntityType type){
        this.type = type;
        this.gp = gp;
        this.direction = "down";
        speed = 60/gp.getFPS();
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    
    public BufferedImage makeSprite(String imgPath){
        return makeSprite(imgPath, gp.getTileSize(), gp.getTileSize());
    }
    
    public BufferedImage makeSprite(String imgPath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;
        
        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream("/" + imgPath));
            scaledImage = uTool.scaleImage(scaledImage, width, height);
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
        //Check NPC collision
        gp.getcChecker().checkEntity(this, gp.getNpcs());
        //Check Monsters collision
        gp.getcChecker().checkEntity(this, gp.getMonsters());
        //Check Player Collision
        if(gp.getcChecker().checkPlayer(this) && this.type == EntityType.MONSTER){
            if(!gp.getPlayer().isInvincible()){
                //Damage teste on collision
                gp.getPlayer().doDamage(1);
                gp.getPlayer().setInvincible(true);
            }
        }
        
        checkDirection();
        controlSpriteAnimationSpeed();
        
        //Invincible damage frame
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > gp.getFPS()-(gp.getFPS()*0.2)){
                invincible = false;
                invincibleCounter = 0;
            }
        }
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
    
    @Override
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
        
        if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
            worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
            worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
            
            BufferedImage image = getSpriteDirection();
            if(invincible){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            
            if(gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
        }
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
        g2.setFont(oldFont);
    }
    
    protected BufferedImage getSpriteDirection(){
        BufferedImage image = null;
        switch(direction){
            case "up":
                if(attacking){
                    if(spriteNum == 1){image = atkU1;}
                    else if(spriteNum == 2){image = atkU2;}
                }else{
                    if(spriteNum == 1){image = u1;}
                    else if(spriteNum == 2){image = u2;}
                }
                break;
            case "down":
                if(attacking){
                    if(spriteNum == 1){image = atkD1;}
                    else if(spriteNum == 2){image = atkD2;}
                }else{
                    if(spriteNum == 1){image = d1;}
                    else if(spriteNum == 2){image = d2;}
                }
                break;
            case "left":
                if(attacking){
                    if(spriteNum == 1){image = atkL1;}
                    else if(spriteNum == 2){image = atkL2;}
                }else{
                    if(spriteNum == 1){image = l1;}
                    else if(spriteNum == 2){image = l2;}
                }
                break;
            case "right":
                if(attacking){
                    if(spriteNum == 1){image = atkR1;}
                    else if(spriteNum == 2){image = atkR2;}
                }else{
                    if(spriteNum == 1){image = r1;}
                    else if(spriteNum == 2){image = r2;}
                }
            default:
                break;
        }
        return image;
    }
    
    protected int spriteCounter = 0;
    public int spriteNum = 1;
    
    @Override
    public int getWorldX() {
        return worldX;
    }
    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }
    @Override
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
    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }
    
    public void resetSolidArea(){
        solidArea.x = solidAreaDefaultX;
        solidArea.y = solidAreaDefaultY;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityType getType() {
        return type;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public Rectangle getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(Rectangle attackArea) {
        this.attackArea = attackArea;
    }
    
    //Teste functions
    public void doDamage(int value){
        if(life > 0) life -= value;
    }
    
    @Override
    public int compareTo(Object o) {
        WorldLocation ext = (WorldLocation) o;
        return Integer.compare(getWorldY(), ext.getWorldY());
    }
    
    public enum EntityType{MONSTER, NPC, PLAYER;}
    
}
