package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.OBJ_Key;

public class LifeBar {
    private BufferedImage lifeBar1, lifeBar2, lifeBar3;
    private Entity e;
    private int x, y;
    private final int RELATIVE_START_X = 5;
    private final int RELATIVE_START_Y = 5;
    private final int HEIGHT = 8;
    private final int WIDTH = 56;
    private GamePanel gp;
    
    public LifeBar(GamePanel gp, Entity e, int x, int y){
        this.e = e;
        this.x = x;
        this.y = y;
        this.gp = gp;
        
        try {
            lifeBar1 = ImageIO.read(getClass().getResourceAsStream("/objects/lifeBar1.png"));
            lifeBar2 = ImageIO.read(getClass().getResourceAsStream("/objects/lifeBar2.png"));
            lifeBar3 = ImageIO.read(getClass().getResourceAsStream("/objects/lifeBar3.png"));
        } catch (IOException ex) {
            Logger.getLogger(OBJ_Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void draw(Graphics2D g2){
        int auxX = x;
        Color oldColor = g2.getColor();
        
        //Draw Life Bar Sprites
        g2.drawImage(lifeBar1, auxX, y, gp.getTileSize(), gp.getTileSize(), null);
        auxX += gp.getTileSize();
        g2.drawImage(lifeBar2, auxX, y, gp.getTileSize(), gp.getTileSize(), null);
        auxX += gp.getTileSize();
        g2.drawImage(lifeBar2, auxX, y, gp.getTileSize(), gp.getTileSize(), null);
        auxX += gp.getTileSize();
        g2.drawImage(lifeBar3, auxX, y, gp.getTileSize(), gp.getTileSize(), null);
        
        //Draw life bar hp
        int sizeBar = (int)((float)WIDTH*calculateLifeBarSize());
        g2.setColor(Color.red);
        g2.fillRect((x + ((RELATIVE_START_X-1)*gp.getScale())), (y + (RELATIVE_START_Y-1)*gp.getScale()), 
                sizeBar*gp.getScale(), HEIGHT*gp.getScale());
        g2.setColor(oldColor);
    }
    
    private float calculateLifeBarSize(){
        return (float)e.getLife()/e.getMaxLife();
    }
    
}
