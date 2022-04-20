package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class UI {
    private GamePanel gp;
    private Font arial_40, arial_80B;
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
    
    public UI(GamePanel gp){
        this.gp = gp;
        this.arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }
    
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(arial_40);
        
        
        switch(gp.getGameState()){
            case PLAY_STATE:
                break;
            case PAUSE_STATE:
                drawPauseScreen();
                break;
            case DIALOG_STATE:
                drawDialogScreen();
                break;
        }
        
    }
    
    public void drawDialogScreen(){
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
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
        
        for(String line : currentDialog.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
        
        g2.setColor(oldColor);
        g2.setStroke(oldStr);
    }
    
    public void drawSubWindow(int x, int y, int width, int height){
        //Draw dialog area
        g2.setColor(dialogBlack);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        //Draw border
        g2.setColor(dialogWhite);
        g2.setStroke(dialogStroke);
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    public void drawPauseScreen(){
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
    
    public int getXForCenteredText(String text){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.getWidth()/2 - length/2;
    }
    
    public boolean isGameFinished() {
        return gameFinished;
    }
    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
    public Font getFontArial40(){
        return this.arial_40;
    }

    public String getCurrentDialog() {
        return currentDialog;
    }

    public void setCurrentDialog(String currentDialog) {
        this.currentDialog = currentDialog;
    }
    
}
