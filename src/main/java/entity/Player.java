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
import object.Action;

public class Player extends Entity{
    private GamePanel gp;
    private KeyHandler keyH;
    private int qtdKeys = 0;
    
    private final int screenX, screenY;
    
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        
        this.screenX = gp.getScreenWidth()/2 - (gp.getTileSize()/2);
        this.screenY = gp.getScreenHeight()/2 - (gp.getTileSize()/2);
        
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
        speed = 4;
        direction = "down";
    }
    
    private void getPlayerImages(){
        try {
            u1 = ImageIO.read(getClass().getResourceAsStream("/player/u1.png"));
            u2 = ImageIO.read(getClass().getResourceAsStream("/player/u2.png"));
            l1 = ImageIO.read(getClass().getResourceAsStream("/player/l1.png"));
            l2 = ImageIO.read(getClass().getResourceAsStream("/player/l2.png"));
            r1 = ImageIO.read(getClass().getResourceAsStream("/player/r1.png"));
            r2 = ImageIO.read(getClass().getResourceAsStream("/player/r2.png"));
            d1 = ImageIO.read(getClass().getResourceAsStream("/player/d1.png"));
            d2 = ImageIO.read(getClass().getResourceAsStream("/player/d2.png"));
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(){
        
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){            
            //Get user input
            if(keyH.upPressed){
                direction = "up";
            }else if(keyH.downPressed){
                direction = "down";
            }else if(keyH.leftPressed){
                direction = "left";
            }else if(keyH.rightPressed){
                direction = "right";
            }
            
            //Check Tile Collision
            collisionOn = false;
            gp.getcChecker().checkTile(this);
            
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
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                }else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
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
        g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
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
