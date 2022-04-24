package entity;

import animation.TimedAnimation;
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
import monster.DyingAnimation;
import util.UtilityTool;
import tile.TileManager;

public abstract class Entity implements Drawnable{
    
    public enum Direction{UP, DOWN, LEFT, RIGHT, ANY};
    
    protected GamePanel gp;    
    
    //Entity sprites
    protected TimedAnimation up, left, right, down, atkUp, atkDown, atkLeft, atkRight;
    protected DyingAnimation dyeAnim = new DyingAnimation(40);
    
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
    protected Direction direction;
    protected boolean dying = false, alive = true;
    protected LifeBar lifeBar;
    
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
        this.direction = Direction.DOWN;
        speed = 60/gp.getFPS();
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        //Life bar
        if(type == EntityType.MONSTER) {
            lifeBar = new LifeBar(gp, this, 0, 0, 1);
            lifeBar.setHiddenControl(5);
        }
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
    public void damageReaction(){};
    public void speak(){
    
        switch(gp.getPlayer().getDirection()){
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.LEFT;
                break;
        }
    
    };
    public void update(){
        if(!alive){
            gp.removeMonster(this);
        }else if(!dying){
            if(lifeBar != null) lifeBar.update();
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
                    gp.playSoundEffect("receiveDamage", 0.4f);
                    gp.getPlayer().doDamage(1);
                }
            }

            checkDirection();
            up.updateSprite();
            down.updateSprite();
            left.updateSprite();
            right.updateSprite();
            if(attacking){
                atkUp.updateSprite();
                atkDown.updateSprite();
                atkLeft.updateSprite();
                atkRight.updateSprite();
            }

            //Invincible damage frame
            if(invincible){
                invincibleCounter++;
                if(invincibleCounter > gp.getFPS()-(gp.getFPS()*0.2)){
                    invincible = false;
                    invincibleCounter = 0;
                }
            }
        }
    }
    
    //Teste functions
    public void doDamage(int value){
        //Basic validations of every Entity
        if(!isInvincible()){
            setInvincible(true);
            damageReaction();
            if(lifeBar != null) lifeBar.setDisplay(true);
            if(life > 0) life -= value;
            if(life <= 0 && this.getType() == EntityType.MONSTER){
                dying = true;
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
    
    protected void checkDirection(){
        //Only move if collisionOn is true
        if(!collisionOn){
            switch(direction){
                case UP:
                    worldY -= speed;
                    break;
                case DOWN:
                   worldY += speed;
                   break;
                case LEFT:
                    worldX -= speed;
                    break;
                case RIGHT:
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
            
            //Draw Life Bar
            if(lifeBar != null){
                lifeBar.setX(screenX);
                lifeBar.setY(screenY);
                lifeBar.draw(g2);
            }
            
            //Draw Sprite
            BufferedImage image = getSpriteDirection();
            if(invincible){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            if(dying) alive = dyeAnim.update(g2);
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
            case UP:
                if(attacking){
                    image = atkUp.getCurrentSprite();
                }else{
                    image = up.getCurrentSprite();
                }
                break;
            case DOWN:
                if(attacking){
                    image = atkDown.getCurrentSprite();
                }else{
                    image = down.getCurrentSprite();
                }
                break;
            case LEFT:
                if(attacking){
                    image = atkLeft.getCurrentSprite();
                }else{
                    image = left.getCurrentSprite();
                }
                break;
            case RIGHT:
                if(attacking){
                    image = atkRight.getCurrentSprite();
                }else{
                    image = right.getCurrentSprite();
                }
            default:
                break;
        }
        return image;
    }
    
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
    public Direction getDirection() {
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

    public boolean isDying() {
        return dying;
    }

    public boolean isAlive() {
        return alive;
    }

    public LifeBar getLifeBar() {
        return lifeBar;
    }
    
    @Override
    public int compareTo(Object o) {
        WorldLocation ext = (WorldLocation) o;
        return Integer.compare(getWorldY(), ext.getWorldY());
    }
    
    public enum EntityType{MONSTER, NPC, PLAYER;}
    
}
