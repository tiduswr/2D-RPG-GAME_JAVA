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
    private Entity e;
    private int x, y;
    private final int RELATIVE_START_X = 5;
    private final int RELATIVE_START_Y = 5;
    private final int HEIGHT = 8;
    private int WIDTH;
    private final int SIZE;
    private final int SCALE;
    private GamePanel gp;
    private boolean display;
    private boolean hiddenMode = false;
    private int FRAMES = 0;
    private int hiddenCounter = 0;
    
    public LifeBar(GamePanel gp, Entity e, int x, int y, int scale){
        this.e = e;
        this.x = x;
        this.y = y;
        this.gp = gp;
        this.SIZE = gp.getOriginalTileSize() * scale;
        SCALE = scale;
        display = false;
        WIDTH = gp.getTileSize();
    }
    
    public LifeBar(GamePanel gp, Entity e, int x, int y){
        this(gp, e, x, y, gp.getScale());
    }
    
    public LifeBar(GamePanel gp, int WIDTH, Entity e, int x, int y){
        this(gp, e, x, y, gp.getScale());
        this.WIDTH = WIDTH;
    }
    
    public void setHiddenControl(int secondsOnScreen){
        if(secondsOnScreen > 0){
            hiddenMode = true;
            FRAMES = secondsOnScreen * gp.getFPS();
            hiddenCounter = 0;
        }else{
            hiddenMode = false;
            FRAMES = 0;
            hiddenCounter = 0;
        }
    }
    
    public void update(){
        if(display && hiddenMode){
            hiddenCounter++;
            if(hiddenCounter >= FRAMES){
                display = false;
                hiddenCounter = 0;
            }
        }
    }
    
    private void resetHiddenCounter(){
        if(hiddenMode) hiddenCounter = 0;
    }
    
    public void draw(Graphics2D g2){
        if(display){
            int auxX = x;
            Color oldColor = g2.getColor();
            
            int offset = 2*SCALE;
            g2.setColor(Color.black);
            g2.fillRoundRect((x + ((RELATIVE_START_X-1)*SCALE))-offset/2, (y + (RELATIVE_START_Y-1)*SCALE)-offset/2, 
                    WIDTH*SCALE+offset, HEIGHT*SCALE+offset, 4, 4);
            
            //Draw life bar hp
            int sizeBar = (int)((float)WIDTH*calculateLifeBarSize());
            g2.setColor(Color.red);
            g2.fillRect((x + ((RELATIVE_START_X-1)*SCALE)), (y + (RELATIVE_START_Y-1)*SCALE), 
                    sizeBar*SCALE, HEIGHT*SCALE);
            g2.setColor(oldColor);
        }
    }
    
    private float calculateLifeBarSize(){
        return (float)e.getStats().getLife()/e.getStats().getMaxLife();
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
        resetHiddenCounter();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
