package main;

import entity.LifeBar;
import util.UtilityTool;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class UI {
    private GamePanel gp;
    private Font freePixel_40;
    private Graphics2D g2;
    
    //Messages
    private boolean messageOn = true;
    private String message = "";
    private int messageCounter = 0;
    private boolean gameFinished = false;
    
    //ui
    private Color dialogBlack = new Color(0,0,0, 210);
    private Color dialogWhite = new Color(255, 255, 255);
    private Stroke dialogStroke = new BasicStroke(5);
    private String currentDialog = "";
    
    //Title Screen
    private int titleCounter = 0;
    private BufferedImage logo, cursor;
    private Stroke titleStroke = new BasicStroke(4);
    private Stroke screenBorderStroke = new BasicStroke(10);
    private int titleCommand, qtdTitleCommands = 3;
    
    //LifeBar
    private LifeBar lifeBar;
    
    public UI(GamePanel gp){
        UtilityTool uTool = new UtilityTool();
        lifeBar = gp.getPlayer().getLifeBar();
        try {
            this.gp = gp;
            try {
                InputStream is = getClass().getResourceAsStream("/font/FreePixel.ttf");
                freePixel_40 = Font.createFont(Font.TRUETYPE_FONT, is);
            } catch (FontFormatException | IOException ex) {
                freePixel_40 = new Font("Arial", Font.PLAIN, 40);
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Read Logo
            logo = ImageIO.read(getClass().getResourceAsStream("/title/crystal.png"));
            logo = uTool.scaleImage(logo, gp.getTileSize(), gp.getTileSize());
            //Read cursor
            cursor = ImageIO.read(getClass().getResourceAsStream("/title/cursor.png"));
            cursor = uTool.scaleImage(cursor, gp.getTileSize(), gp.getTileSize());
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(freePixel_40);
        
        
        switch(gp.getGameState()){
            case PLAY_STATE:
                lifeBar.draw(g2);
                break;
            case PAUSE_STATE:
                drawPauseScreen();
                break;
            case DIALOG_STATE:
                drawDialogScreen();
                break;
            case TITLE_STATE:
                drawTitleScreen();
                break;
        }
        
    }
    
    private void drawTitleScreen(){
        //Draw Title
        String text = "FINAL FANTASY FAN";
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        g2.setColor(Color.black);
        g2.setStroke(titleStroke);
        
        //Animated Title
        int x = getXForCenteredText(text);
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
            x = getXForCenteredText(text);
            y += gp.getTileSize()*3.5;
            g2.drawString(text, x, y);
            //Draw cursor
            if(titleCommand == 0){
                g2.drawImage(cursor, x-gp.getTileSize() - 10, y-gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null);
            }
            
            text = "LOAD";
            x = getXForCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            //Draw cursor
            if(titleCommand == 1){
                g2.drawImage(cursor, x-gp.getTileSize() - 10, y-gp.getTileSize(), gp.getTileSize(), gp.getTileSize(), null); 
            }
            
            text = "QUIT";
            x = getXForCenteredText(text);
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
    
    private void drawDialogScreen(){
        Color oldColor = g2.getColor();
        Stroke oldStr = g2.getStroke();
        
        int x = gp.getTileSize()*2, 
            y = gp.getTileSize()/2, 
            width = gp.getScreenWidth() - gp.getTileSize()*4, 
            height = gp.getScreenHeight() - gp.getTileSize()*8;
        drawSubWindow(x, y, width, height);
        
        //Draw message
        x += gp.getTileSize();
        y += gp.getTileSize();
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        
        for(String line : currentDialog.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
        
        g2.setColor(oldColor);
        g2.setStroke(oldStr);
    }
    
    private void drawSubWindow(int x, int y, int width, int height){
        //Draw dialog area
        g2.setColor(dialogBlack);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        //Draw border
        g2.setColor(dialogWhite);
        g2.setStroke(dialogStroke);
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    private void drawPauseScreen(){
        String text = "PAUSED";
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60));
        int x = getXForCenteredText(text);
        int y = gp.getScreenHeight()/2;
        
        g2.drawString(text, x, y);
        g2.setFont(oldFont);
        g2.setColor(oldColor);
    }
    
    private int getXForCenteredText(String text){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.getWidth()/2 - length/2;
    }
    
    public boolean isGameFinished() {
        return gameFinished;
    }
    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
    public Font getfreePixel_40(){
        return this.freePixel_40;
    }

    public String getCurrentDialog() {
        return currentDialog;
    }

    public void setCurrentDialog(String currentDialog) {
        this.currentDialog = currentDialog;
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
