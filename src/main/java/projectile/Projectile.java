package projectile;

import animation.TimedAnimation;
import entity.Entity;
import entity.Direction;
import entity.damage.FixedDamage;
import entity.damage.MagicDamage;
import interfaces.Drawable;
import interfaces.WorldLocation;
import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import tile.TileManager;
import util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Projectile implements Drawable {
    protected GamePanel gp;
    protected Entity user;
    //Sprites
    protected TimedAnimation up, left, right, down, atkUp, atkDown, atkLeft, atkRight;

    //Collision check and localization
    protected int worldX, worldY;
    protected Rectangle solidArea, attackArea = new Rectangle(0, 0, 0, 0);
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected boolean collisionOn = false;
    protected Stroke collisionRectStroke = new BasicStroke(2);

    //Entity Basic
    protected Direction direction;
    protected String name = "";
    protected int speed, cost, framesVisible, recastTime, recastTimeCounter;
    protected int framesVisibleCount;
    protected boolean visible = false;
    protected String hitSound = "fireBallDamage", castSound = "fireball";

    public Projectile(GamePanel gp, Entity user) {
        this.gp = gp;
        this.user = user;

        //Direction
        this.direction = Direction.DOWN;

        recastTime = 60; //1 Second

        //Hitbox
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public boolean set(int worldX, int worldY, Direction dir, boolean visible){
        if(recastTimeCounter <= 0){
            this.worldX = worldX;
            this.worldY = worldY;
            this.direction = dir;
            this.visible = visible;
            framesVisibleCount = framesVisible;
            recastTimeCounter = recastTime;
            if(!castSound.equalsIgnoreCase("") && user.isOnScreen()) gp.playSoundEffect(castSound, 1f);
            return true;
        }
        return false;
    }

    public void recastTimeUpdate(){
        if(recastTimeCounter > 0) recastTimeCounter--;
    }

    public void update(){
        framesVisibleCount--;
        if(framesVisibleCount <= 0){
            visible = false;
            gp.markDrawableForDispose(this);
        }else{
            //Check collision
            if(user == gp.getPlayer()){
                int monsterIndex = gp.getcChecker().checkProjectile(this, gp.getMonsters());
                if(monsterIndex != -1){
                    framesVisibleCount = 0;
                    recastTime = 0;
                    visible = false;
                    gp.markDrawableForDispose(this);
                    Entity e = gp.getMonsters().get(monsterIndex);
                    e.doDamage(new MagicDamage(calculateAttack(user.getStats().getMag()), e), direction);
                    gp.getPlayer().checkExpFromMonster(e);
                    if(!hitSound.equalsIgnoreCase("")) gp.playSoundEffect(hitSound, 1f);
                }
            }else if(user.getStats().getType() == Entity.EntityType.MONSTER){
                boolean hitPlayer = gp.getcChecker().checkProjectileHitPlayer(this);
                if(hitPlayer) {
                    framesVisibleCount = 0;
                    recastTime = 0;
                    visible = false;
                    gp.markDrawableForDispose(this);
                    gp.getPlayer().doDamage(new MagicDamage(calculateAttack(user.getStats().getMag()), gp.getPlayer()),
                                                direction);
                    if(!hitSound.equalsIgnoreCase("")) gp.playSoundEffect(hitSound, 1f);
                }
            }

            if(gp.getcChecker().checkProjectileHitWall(this)){
                framesVisibleCount = 0;
                recastTime = 0;
                visible = false;
                gp.markDrawableForDispose(this);
            }

            switch(direction){
                case UP:
                    worldY -= speed;
                    up.updateSprite();
                    break;
                case DOWN:
                    worldY += speed;
                    down.updateSprite();
                    break;
                case LEFT:
                    worldX -= speed;
                    left.updateSprite();
                    break;
                case RIGHT:
                    worldX += speed;
                    right.updateSprite();
                    break;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
        if (worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
            worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
            worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {
            //Draw Sprite
            BufferedImage image = getSpriteDirection();
            g2.drawImage(image, screenX, screenY, null);
            if (gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
        }
    }

    protected void drawCollision(Graphics2D g2, int screenX, int screenY) {
        //Desenha a colisão
        Stroke oldStroke = g2.getStroke();
        Font oldFont = g2.getFont();
        g2.setStroke(collisionRectStroke);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        g2.setStroke(oldStroke);
        g2.setFont(gp.getGameUI().getfreePixel_40().deriveFont(12F));
        g2.drawString("X: " + worldX / gp.getTileSize() + " Y: " + worldY / gp.getTileSize(), screenX, screenY - 1);
        g2.setFont(oldFont);
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

    protected BufferedImage getSpriteDirection() {
        BufferedImage image = null;
        switch (direction) {
            case UP:
                image = up.getCurrentSprite();
                break;
            case DOWN:
                image = down.getCurrentSprite();
                break;
            case LEFT:
                image = left.getCurrentSprite();
                break;
            case RIGHT:
                image = right.getCurrentSprite();
            default:
                break;
        }
        return image;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int calculateAttack(int mag){
        return 0;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        WorldLocation ext = (WorldLocation) o;
        return Integer.compare(getWorldY(), ext.getWorldY());
    }
}
