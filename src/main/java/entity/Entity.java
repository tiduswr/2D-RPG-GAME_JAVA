package entity;

import entity.damage.Damage;
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

import main.GamePanel;
import interfaces.WorldLocation;
import animation.DyingAnimation;
import entity.damage.DamageAction;
import object.SuperObject;
import util.UtilityTool;
import tile.TileManager;
import interfaces.Drawable;

public abstract class Entity implements Drawable{
    protected GamePanel gp;    
    
    //Entity sprites
    protected TimedAnimation up, left, right, down, atkUp, atkDown, atkLeft, atkRight;
    protected DyingAnimation dyeAnim = new DyingAnimation(40);
    
    //Collision check and localization
    protected int worldX, worldY;
    protected Rectangle solidArea, attackArea = new Rectangle(0, 0, 0, 0);
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected boolean collisionOn = false;
    protected Stroke collisionRectStroke = new BasicStroke(2);
    
    //Attacking
    protected boolean attacking = false;
    
    //IA
    protected int actionLockCounter;
    
    //Entity Basic
    protected Direction direction;
    protected boolean dying = false, alive = true;
    protected LifeBar lifeBar;
    protected ManaBar manaBar;
    protected String[] dialogues;
    protected int adjustHpBarY = 0;
    protected boolean damageOnTouch, onScreen = false;

    //Invincible Handler
    protected int invincibleCounter = 0;
    protected boolean invincible = false;
    
    //Char stats
    protected Stats stats;
    
    public Entity(GamePanel gp, EntityType type){
        this.gp = gp;

        //Stats
        stats = new Stats();
        stats.type = type;
        stats.speed = 60/gp.getFPS();
        
        //Direction
        this.direction = Direction.DOWN;
        
        //Hitbox
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
    
    public void setAction(){};
    public void damageReaction(Direction dir){};
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

    public void randomChooseDrop(){};

    protected void dropItem(SuperObject o){
        gp.getAssetSetter().spawnObjectByWorldLocation(o, worldX, worldY);
    }

    public void update(){
        if(!alive){
            randomChooseDrop();
            gp.markDrawableForDispose(this);
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
            touchPlayer();

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

    protected void touchPlayer() {
        if(gp.getcChecker().checkPlayer(this) && stats.getType() == EntityType.MONSTER){
            if(!gp.getPlayer().isInvincible() && damageOnTouch){
                //Damage teste on collision
                gp.playSoundEffect("receiveDamage", 0.4f);
                gp.getPlayer().doDamage(new Damage(this, gp.getPlayer()), direction);
            }
        }
    }
    
    //Teste functions
    public void doDamage(DamageAction value, Direction dir){
        //Basic validations of every Entity
        if(!isInvincible()){
            setInvincible(true);
            damageReaction(dir);
            if(lifeBar != null) lifeBar.setDisplay(true);
            if(stats.life > 0 && value.calculateDamage() < stats.life){
                System.out.println(value.calculateDamage());
                stats.life -= value.calculateDamage();
            }else{
                stats.life = 0;
            }
            if(stats.life <= 0 && stats.getType() == EntityType.MONSTER){
                dying = true;
            }
        }
    }

    public void heal(int hp){
        int calculatedHp = hp + stats.getLife();
        if(calculatedHp >= stats.getMaxLife()) {
            stats.setLife(stats.getMaxLife());
        }else{
            stats.setLife(calculatedHp);
        }
    }

    public void recoverMp(int mp){
            int calculatedMp = mp + stats.getMana();
        if(calculatedMp >= stats.getMaxMana()) {
            stats.setMana(stats.getMaxMana());
        }else{
            stats.setMana(calculatedMp);
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
                    worldY -= stats.speed;
                    break;
                case DOWN:
                   worldY += stats.speed;
                   break;
                case LEFT:
                    worldX -= stats.speed;
                    break;
                case RIGHT:
                    worldX += stats.speed;
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
                lifeBar.setY(screenY+adjustHpBarY);
                lifeBar.draw(g2);
            }
            
            //Draw Sprite
            BufferedImage image = getSpriteDirection();
            if(invincible){
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            if(dying) alive = dyeAnim.update(g2);
            g2.drawImage(image, screenX, screenY,null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            onScreen = true;

            if(gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
        }else{
            onScreen = false;
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
    
    public void resetSolidArea(){
        solidArea.x = solidAreaDefaultX;
        solidArea.y = solidAreaDefaultY;
    }

    public Stats getStats() {
        return stats;
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

    public boolean isDamageOnTouch() {
        return damageOnTouch;
    }

    public void setDamageOnTouch(boolean damageOnTouch) {
        this.damageOnTouch = damageOnTouch;
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    @Override
    public int compareTo(Object o) {
        WorldLocation ext = (WorldLocation) o;
        return Integer.compare(getWorldY(), ext.getWorldY());
    }

    public ManaBar getManaBar() {
        return manaBar;
    }

    public enum EntityType{MONSTER, NPC, PLAYER;}
    
}
