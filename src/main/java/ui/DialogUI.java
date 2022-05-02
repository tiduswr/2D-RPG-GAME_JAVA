package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import main.GamePanel;
import util.UtilityTool;

public class DialogUI {
    private Window window;
    private String currentDialog = "";
    private boolean stopMusic = false;
    private int xOffset, yOffset, linesDistance;
    private GamePanel gp;
    
    public DialogUI(GamePanel gp, int x, int y, int width, int height) {
        this.gp = gp;
        this.window = new Window(x, y, width, height);
        xOffset = gp.getTileSize()/2;
        yOffset = gp.getTileSize()-8;
        linesDistance = 40;
    }
    
    public void drawDialogScreen(Graphics2D g2){
        Color oldColor = g2.getColor();
        Stroke oldStr = g2.getStroke();
        
        window.drawWindow(g2);
        
        //Draw message
        int x = xOffset + window.getX();
        int y = yOffset + window.getY();
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        
        UtilityTool.drawStringMultiLine(g2, currentDialog, window.getWidth()-xOffset*2, x, y);
//        for(String line : currentDialog.split("\n")){
//            g2.drawString(line, x, y);
//            y += linesDistance;
//        }
        
        g2.setColor(oldColor);
        g2.setStroke(oldStr);
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getLinesDistance() {
        return linesDistance;
    }

    public void setLinesDistance(int linesDistance) {
        this.linesDistance = linesDistance;
    }
    
    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public String getCurrentDialog() {
        return currentDialog;
    }

    public void setCurrentDialog(String currentDialog) {
        this.currentDialog = currentDialog;
    }

    public boolean isStopMusic() {
        return stopMusic;
    }

    public void setStopMusic(boolean stopMusic) {
        this.stopMusic = stopMusic;
    }
    
}
