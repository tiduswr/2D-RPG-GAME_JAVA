package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.Action;
import tile.TileManager;

public class Player extends Entity{
    
    private KeyHandler keyH;
    private int qtdKeys = 0;
    private int standCounter = 0;
    
    private final int screenX, screenY;
    
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        
        this.screenX = gp.getScreenWidth()/2 - (gp.getTileSize()/2);
        this.screenY = gp.getScreenHeight()/2 - (gp.getTileSize()/2);
        speed = 240/gp.getFPS();
        
        //Rectangle that define the pixels of Collision in Player
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 12;
        solidArea.width = 32;
        solidArea.height = 32;
        setSolidAreaDefaultX(solidArea.x);
        setSolidAreaDefaultY(solidArea.y);
        
        setDefaultValues();
        getPlayerImages();
    }
    
    private void setDefaultValues(){
        worldX = gp.getTileSize() * 23;
        worldY = gp.getTileSize() * 21;
        direction = "down";
    }
    
    private void getPlayerImages(){
        u1 = makePlayerSprite("u1.png");
        u2 = makePlayerSprite("u2.png");
        l1 = makePlayerSprite("l1.png");
        l2 = makePlayerSprite("l2.png");
        r1 = makePlayerSprite("r1.png");
        r2 = makePlayerSprite("r2.png");
        d1 = makePlayerSprite("d1.png");
        d2 = makePlayerSprite("d2.png");
    }
    
    public BufferedImage makePlayerSprite(String imgName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;
        
        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream("/player/" + imgName));
            scaledImage = uTool.scaleImage(scaledImage, gp.getTileSize(), gp.getTileSize());
        } catch (IOException ex) {
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return scaledImage;
    }
    
    public void update(){
        
        if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()){            
            //Get user input
            if(keyH.isUpPressed()){
                direction = "up";
            }else if(keyH.isDownPressed()){
                direction = "down";
            }else if(keyH.isLeftPressed()){
                direction = "left";
            }else if(keyH.isRightPressed()){
                direction = "right";
            }
            
            //Check Tile Collision
            collisionOn = false;
            //aaaadgp.getcChecker().checkTile(this);
            
            //Check object collision
            int objIndex = gp.getcChecker().checkObject(this, true);
            pickUpObject(objIndex);
                    
            //Only move if collisionOn is true
            switch(direction){
                case "up":
                    if(!collisionOn) worldY -= speed;
                    break;
                case "down":
                   if(!collisionOn) worldY += speed;
                   break;
                case "left":
                    if(!collisionOn) worldX -= speed;
                    break;
                case "right":
                    if(!collisionOn) worldX += speed;
                    break;
            }
            
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
        }else{
            standCounter++;
            if(standCounter == 20){
                standCounter = 0;
                spriteNum = 1;
            }
        }
    
    }
    
    public void pickUpObject(int i){
        if(i != -1){
            Action a = gp.getObj()[i];
            if(a.executeAction()) gp.getObj()[i] = null;
        }
    }
    
    public void draw(Graphics2D g2){
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
        
        //Check location of player to draw a tile
        //Implemented to dont show blank tiles of map
        int x = screenX;
        int y = screenY;
        if(screenX > worldX){
            x = worldX;
        }
        if(screenY > worldY){
            y = worldY;
        }
        int rightOffset = gp.getScreenWidth() - getScreenX();
        if(rightOffset > gp.getWorldWidth() - getWorldX()){
            x = gp.getScreenWidth() - (gp.getWorldWidth() - worldX);
        }
        int bottomOffset = gp.getScreenHeight() - getScreenY();
        if(bottomOffset > gp.getWorldHeight() - getWorldY()){
            y = gp.getScreenHeight() - (gp.getWorldHeight() - worldY);
        }
        
        //Draw player
        g2.drawImage(image, x, y, null);
        if (gp.getKeyH().debugMode()) drawCollision(g2, x, y);
    }
    
    public int getScreenX() {
        return screenX;
    }
    public int getScreenY() {
        return screenY;
    }
    public int getQtdKeys() {
        return qtdKeys;
    }
    public void addKeys(int value){
        qtdKeys += value;
    }
    public void removeKeys(int value){
        qtdKeys -= value;
    }
}
