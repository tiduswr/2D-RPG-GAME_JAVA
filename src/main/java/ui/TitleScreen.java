package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import main.GamePanel;
import main.GameState;
import util.UtilityTool;

public class TitleScreen {
    
    private GamePanel gp;
    private int titleCounter = 0;
    private BufferedImage logo, cursor;
    private Stroke titleStroke = new BasicStroke(4);
    private Stroke screenBorderStroke = new BasicStroke(10);
    private int titleCommand, qtdTitleCommands = 3;
    
    public TitleScreen(GamePanel gp) {
        this.gp = gp;
        UtilityTool uTool = new UtilityTool();
        try {
            //Read Logo
            logo = ImageIO.read(getClass().getResourceAsStream("/title/crystal.png"));
            cursor = ImageIO.read(getClass().getResourceAsStream("/title/cursor.png"));
        } catch (IOException ex) {
            Logger.getLogger(TitleScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        logo = uTool.scaleImage(logo, gp.getTileSize(), gp.getTileSize());
        cursor = uTool.scaleImage(cursor, gp.getTileSize(), gp.getTileSize());
    }
    
    public void drawTitleScreen(Graphics2D g2){
        //Draw Title
        String text = "FINAL FANTASY FAN";
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        g2.setColor(Color.black);
        g2.setStroke(titleStroke);
        
        //Animated Title
        int x = getXForCenteredText(text, g2);
        int y = gp.getTileSize(); y += titleCounter;
        g2.setColor(Color.gray);
        g2.drawString(text, x-3, y+3);
        g2.setColor(Color.black);
        g2.drawString(text, x, y);
        g2.drawLine(x + 5, y + 9, x + g2.getFontMetrics(g2.getFont()).stringWidth(text) - 5, y + 9);
        
        //Draw Border Screen
        g2.setStroke(screenBorderStroke);
        g2.drawRoundRect(0, 0, gp.getWidth(), gp.getHeight(), 30, 30);
        
        if(titleCounter < gp.getTileSize()*2){
            titleCounter++;
        }else{        
            //Draw logo
            x = gp.getScreenWidth()/2 - gp.getTileSize();
            y = gp.getTileSize()*4;
            g2.drawImage(logo, x, y, gp.getTileSize()*2, gp.getTileSize()*2, null);

            //Menu Options
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NEW GAME";
            x = getXForCenteredText(text, g2);
            y += gp.getTileSize()*3.5;
            g2.drawString(text, x, y);
            //Draw cursor
            if(titleCommand == 0){
                g2.drawImage(cursor, x-gp.getTileSize() - 10, y-gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null);
            }
            
            text = "LOAD";
            x = getXForCenteredText(text, g2);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            //Draw cursor
            if(titleCommand == 1){
                g2.drawImage(cursor, x-gp.getTileSize() - 10, y-gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null); 
            }
            
            text = "QUIT";
            x = getXForCenteredText(text, g2);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            //Draw cursor
            if(titleCommand == 2){
                g2.drawImage(cursor, x-gp.getTileSize() - 10, y-gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null); 
            }
        }
        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }
    
    private int getXForCenteredText(String text, Graphics2D g2){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.getWidth()/2 - length/2;
    }
    
    private void cancelTitleLogoAnimation(){
        if(titleCounter < gp.getTileSize()*2) titleCounter = gp.getTileSize()*2;
    }
    
    public void executeMenuAction(){
        cancelTitleLogoAnimation();
        switch(titleCommand){
            case 0:
                gp.playSoundEffect("selected", 1);
                gp.setGameState(GameState.PLAY_STATE);
                gp.stopMusic();
                gp.playMusic("worldTheme");
                break;
            case 1:
                
                break;
            case 2:
                gp.playSoundEffect("selected", 1);
                gp.setGameThread(null);
                SwingUtilities.getWindowAncestor(gp).dispose();
                gp.stopMusic();
                break;
        }
    }
    
    public void upTitleCommand() {
        cancelTitleLogoAnimation();
        gp.playSoundEffect("cursor", 0.3f);
         if(titleCommand > 0) titleCommand--;
    }

    public void downTitleCommand() {
        cancelTitleLogoAnimation();
        gp.playSoundEffect("cursor", 0.3f);
        if(titleCommand < qtdTitleCommands - 1) titleCommand++;
    }
    
}
