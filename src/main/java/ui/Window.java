package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Window {
    private Color background = new Color(0, 0, 0, 200);
    private Color border = new Color(255, 255 ,255);
    private Stroke Stroke = new BasicStroke(5);
    private int x, y, width, height;

    public Window(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void drawWindow(Graphics2D g2){
        //Draw dialog area
        g2.setColor(background);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        //Draw border
        g2.setColor(border);
        g2.setStroke(Stroke);
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        this.border = border;
    }
    
    public Stroke getStroke() {
        return Stroke;
    }

    public void setStroke(Stroke Stroke) {
        this.Stroke = Stroke;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}
