package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.ListIterator;
import main.GamePanel;

public class ScrollingMessages {
    private GamePanel gp;
    private ArrayList<Message> messages;
    
    public ScrollingMessages(GamePanel gp) {
        this.gp = gp;
        messages = new ArrayList<>();
    }
    
    public void addMessage(Message msg){
        this.messages.add(msg);
    }
    
    public void drawMessages(Graphics2D g2){
        int x = gp.getTileSize()/2;
        int y = gp.getTileSize()*4;
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(Color.white);
        
        ListIterator<Message> iter = messages.listIterator();
        while(iter.hasNext()){
            Message m = iter.next();
            
            //Draw Shadow
            g2.setColor(Color.black);
            g2.drawString(m.getMessage(), x+2, y+2);
            //Draw message and sum 1 on counter
            g2.setColor(Color.white);
            g2.drawString(m.getMessage(), x, y);
            m.messageCounterPlusPlus();
            
            //Increase line to draw
            y += 24;
            
            //Remove Message after 3 seconds
            if(m.getMessageCounter() > 3*gp.getFPS()){
                iter.remove();
                y -= 24;
            }
        }
        
        g2.setColor(oldColor);
        g2.setFont(oldFont);
    }
    
}
