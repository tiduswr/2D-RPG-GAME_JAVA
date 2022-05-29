package object;

import interfaces.Action;

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

import interfaces.InventoryAction;
import main.GamePanel;
import interfaces.WorldLocation;
import tile.TileManager;
import util.UtilityTool;
import interfaces.Drawable;

public abstract class SuperObject implements Action, InventoryAction, Drawable {
    public enum ObjectType{CAN_BE_STORED, PICKUP_ONLY}

    protected BufferedImage image;
    protected String name = "???", description = "No description!";
    protected boolean collision;
    protected int worldX, worldY;
    protected Rectangle solidArea;
    protected GamePanel gp;
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected final Stroke collisionRectStroke;
    protected ObjectType objType = ObjectType.CAN_BE_STORED;

    public SuperObject(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        collision = false;
        collisionRectStroke = new BasicStroke(2);
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        if (worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
                worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
                worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {
            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
            if (gp.getKeyH().debugMode()) drawCollision(g2, screenX, screenY);
        }
    }

    public BufferedImage makeSprite(String imgPath) {
        return makeSprite(imgPath, gp.getTileSize(), gp.getTileSize());
    }

    public BufferedImage makeSprite(String imgPath, int width, int height) {
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

    public BufferedImage getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public boolean isCollision() {
        return collision;
    }

    @Override
    public int getWorldX() {
        return worldX;
    }

    @Override
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

    public ObjectType getObjType() {
        return objType;
    }

    public void setObjType(ObjectType objType) {
        this.objType = objType;
    }

    @Override
    //If true, remove the object from the world and add to inventory
    public boolean executeAction() {
        return false;
    }

    public boolean executeInventoryAction(){
        return false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private void drawCollision(Graphics2D g2, int screenX, int screenY) {
        //Desenha a colisão
        Stroke oldStroke = g2.getStroke();
        Font oldFont = g2.getFont();
        g2.setStroke(collisionRectStroke);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        g2.setStroke(oldStroke);
        g2.setFont(gp.getGameUI().getfreePixel_40().deriveFont(12F));
        g2.drawString("X: " + worldX / gp.getTileSize() + " Y: " + worldY / gp.getTileSize(), screenX, screenY - 1);
        g2.setFont(g2.getFont());
    }

    @Override
    public int compareTo(Object o) {
        WorldLocation ext = (WorldLocation) o;
        return Integer.compare(getWorldY(), ext.getWorldY());
    }

}
