package ui;

import main.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Debug {
    private int debugTimeDrawCounter = -1;
    private long lastPassed;
    private long sumDrawTime = 0;
    private GamePanel gp;
    
    public Debug(GamePanel gp){
        this.gp = gp;
    }    
    
    public void draw(Graphics2D g2, long drawStart, long drawEnd){
        long passed;
        
        //Calculate the drawing process time
        debugTimeDrawCounter++;
        sumDrawTime = sumDrawTime + drawEnd - drawStart;
        if(debugTimeDrawCounter == gp.getFPS() || debugTimeDrawCounter == -1){
            passed = sumDrawTime/debugTimeDrawCounter;
            lastPassed = passed;
            debugTimeDrawCounter = 0;
            sumDrawTime = 0;
        }else{
            passed = lastPassed;
        }
        
        //Set the fonts
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();
        g2.setColor(Color.white);
        g2.setFont(gp.getGameUI().getfreePixel_40());
        g2.setFont(g2.getFont().deriveFont(20F));
        
        //Draw drawing time
        g2.drawString("Drawing Average Time: " + passed + " nanoseconds", 10, gp.getScreenHeight() - 10);
        //Draw FPS
        g2.drawString("FPS: " + gp.getFpsCounter() , 10, gp.getScreenHeight() - 30);        
        
        //Restore old configurations
        g2.setFont(oldFont);
        g2.setColor(oldColor);
    }
    
}
