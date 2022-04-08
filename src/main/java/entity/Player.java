package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    private GamePanel gp;
    private KeyHandler keyH;
    
    private final int screenX, screenY;
    
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        
        this.screenX = gp.getScreenWidth()/2 - (gp.getTileSize()/2);
        this.screenY = gp.getScreenHeight()/2 - (gp.getTileSize()/2);
        
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
                worldY -= speed;
            }else if(keyH.downPressed){
                direction = "down";
                worldY += speed;
            }else if(keyH.leftPressed){
                direction = "left";
                worldX -= speed;
            }else if(keyH.rightPressed){
                direction = "right";
                worldX += speed;
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
}
