package ui;

import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import main.GamePanel;

public class StatusScreen {
    private Window window;
    private GamePanel gp;
    private int xOffset, yOffset, linesDistance;
    
    public StatusScreen(GamePanel gp, int x, int y, int width, int height) {
        this.gp = gp;
        this.window = new Window(x, y, width, height);
        xOffset = x + 20;
        yOffset = y + gp.getTileSize();
        linesDistance = 32;
    }
    
    public void draw(Graphics2D g2){
        window.drawWindow(g2);
        
        //Draw status
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32f));
        int auxY = yOffset;
        int auxX = (window.getX() + window.getWidth()) - 20;
        Player p = gp.getPlayer();

        drawLineOnTheRight(g2, auxX, auxY, "Level", String.valueOf(p.getStats().getLevel()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Life", String.valueOf(p.getStats().getLife() + "/" + p.getStats().getMaxLife()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Str", String.valueOf(p.getStats().getStr()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Mag", String.valueOf(p.getStats().getMag()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Dex", String.valueOf(p.getStats().getDex()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Atk", String.valueOf(p.getStats().getAtk()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Def", String.valueOf(p.getStats().getDef()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Exp", String.valueOf(p.getStats().getExp()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Next Lvl", String.valueOf(p.getStats().getNextLvlExp()));
        auxY += linesDistance;
        drawLineOnTheRight(g2, auxX, auxY, "Gil", String.valueOf(p.getStats().getGil()));
        auxY += linesDistance + 20;
        drawLineOnTheRight(g2, auxX, auxY, "Weapon", "");
        auxY += linesDistance + 15;
        drawLineOnTheRight(g2, auxX, auxY, "Shield", "");
        
        //Draw equips
        g2.drawImage(p.getCurWeapon().getImage(), auxX - gp.getTileSize(), auxY - gp.getTileSize() - 25, null);
        auxY += gp.getTileSize();
        g2.drawImage(p.getCurShield().getImage(), auxX - gp.getTileSize(), auxY - gp.getTileSize() - 25, null);
        
    }
    
    private void drawLineOnTheRight(Graphics2D g2, int x, int y,String title, String value){
        g2.drawString(title, xOffset, y);
        x = getXForRightText(g2, value, x);
        g2.drawString(value, x, y);
    }
    
    private int getXForRightText(Graphics2D g2, String text, int tailX){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
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
    
}
