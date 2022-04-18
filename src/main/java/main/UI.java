package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import object.OBJ_Key;

public class UI {
    private GamePanel gp;
    private Font arial_40, arial_80B;
    private Graphics2D g2;
    
    //Messages
    private boolean messageOn = true;
    private String message = "";
    private int messageCounter = 0;
    private boolean gameFinished = false;
    
    //Timer
    private double playTime;
    private DecimalFormat dFormat = new DecimalFormat("#0.0");
    
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
        }
        
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
}
