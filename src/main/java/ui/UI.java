package ui;

import entity.LifeBar;
import util.UtilityTool;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.GamePanel;

public class UI {
    private GamePanel gp;
    private Font freePixel_40;
    private Graphics2D g2;
    
    //Messages
    private boolean gameFinished = false;
    
    //Components
    private DialogUI dialogUi;
    private TitleScreen titleScreen;
    private StatusScreen statusScreen;
    private LifeBar lifeBar;
    private ScrollingMessages scrollMsg;
    
    public UI(GamePanel gp){
        UtilityTool uTool = new UtilityTool();
        lifeBar = gp.getPlayer().getLifeBar();
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/font/FreePixel.ttf");
            freePixel_40 = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException ex) {
            freePixel_40 = new Font("Arial", Font.PLAIN, 40);
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //TitleScreen Handler
        titleScreen = new TitleScreen(gp);
        
        //Dialog handler
        int x = gp.getTileSize()*2, 
            y = gp.getTileSize()/2, 
            width = gp.getScreenWidth() - gp.getTileSize()*4, 
            height = gp.getScreenHeight() - gp.getTileSize()*8;
        dialogUi = new DialogUI(gp, x, y, width, height);
        
        //Scrolling messages
        scrollMsg = new ScrollingMessages(gp);
        
        //Status Screen Handler
        x = gp.getTileSize()/2; 
        y = gp.getTileSize()/2;
        width = gp.getTileSize()*5; 
        height = gp.getTileSize()*9;
        statusScreen = new StatusScreen(gp, x, y, width, height);
    }
    
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(freePixel_40);
        switch(gp.getGameState()){
            case PLAY_STATE:
                lifeBar.draw(g2);
                scrollMsg.drawMessages(g2);
                break;
            case PAUSE_STATE:
                drawPauseScreen();
                break;
            case DIALOG_STATE:
                dialogUi.drawDialogScreen(g2);
                break;
            case TITLE_STATE:
                titleScreen.drawTitleScreen(g2);
                break;
            case STATUS_WINDOW_STATE:
                statusScreen.draw(g2);
        }
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
        return dialogUi.getCurrentDialog();
    }

    public void setCurrentDialog(String currentDialog) {
        dialogUi.setCurrentDialog(currentDialog);
    }

    public TitleScreen getTitleScreen() {
        return titleScreen;
    }

    public ScrollingMessages getScrollMsg() {
        return scrollMsg;
    }
    
}
