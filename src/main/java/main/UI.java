package main;

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
import tile.TileManager;

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
    private BufferedImage logo;
    
    public UI(GamePanel gp){
        UtilityTool uTool = new UtilityTool();
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
            logo = null;
            logo = ImageIO.read(getClass().getResourceAsStream("/title/crystal.png"));
            logo = uTool.scaleImage(logo, gp.getTileSize(), gp.getTileSize());
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
        String title = "FINAL FANTASY FAN";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        g2.setColor(Color.white);
        
        int x = getXForCenteredText(title);
        int y = gp.getTileSize(); y += titleCounter;
        
        //Animated Title
        if(titleCounter < gp.getTileSize()*2){
            g2.drawString(title, x, y);
            titleCounter++;
        }else{
            g2.drawString(title, x, y);
        }
        
        //Draw logo
        x = gp.getScreenWidth()/2 - gp.getTileSize();
        y = gp.getTileSize()*4;
        g2.drawImage(logo, x, y, gp.getTileSize()*2, gp.getTileSize()*2, null);
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
}
