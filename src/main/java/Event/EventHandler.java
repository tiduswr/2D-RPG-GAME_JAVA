package Event;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import main.GamePanel;
import main.GameState;

public final class EventHandler {
    private GamePanel gp;
    private Rectangle eventRect;
    private int eventDefaultX, eventDefaultY;
    private final HashMap<String ,Event> events;
    
    public EventHandler(GamePanel gp) {
        this.events = new HashMap<>();
        this.gp = gp;
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventDefaultX = eventRect.x;
        eventDefaultY = eventRect.y;
        
        //Event test
        Event damageTeste = new Event(24, 20, "right"){
            @Override
            public void execute() {
                gp.setGameState(GameState.DIALOG_STATE);
                gp.getGameUI().setCurrentDialog("Você levou uma topada!");
                gp.getPlayer().doDamage(1);
            }
        };
        addEvent(damageTeste, "damageTeste");
        
        Event healingTeste = new Event(23, 12, "up"){
            @Override
            public void execute() {
                if(gp.getKeyH().iszPressed()){
                    gp.getGameUI().setCurrentDialog("Você bebeu da fonte...\nVocê se sente recuperado!");
                    gp.setGameState(GameState.DIALOG_STATE);
                    gp.getPlayer().resetLife();
                    gp.playSoundEffect("cure", 0.5f);
                }
            }
        };
        addEvent(healingTeste, "healingTeste");
        
        Event teleportTeste = new Event(27, 16, "right"){
            @Override
            public void execute() {
                gp.getGameUI().setCurrentDialog("Você teleportou!");
                gp.getPlayer().setWorldX(gp.getTileSize()*37);
                gp.getPlayer().setWorldY(gp.getTileSize()*10);
                gp.setGameState(GameState.DIALOG_STATE);
            }
        };
        addEvent(teleportTeste, "teleportTeste");
        
    }
    
    public void checkEvent(){
        for(Event e : events.values()){
            if(hit(e)){
                e.execute();
            }
        }        
    }
    
    public void addEvent(Event e, String name){
        events.put(name, e);
    }
    
    public void removeEvent(String name){
        events.remove(name);
    }
    
    public void draw(Graphics2D g2){
        for(Event e : events.values()){
            int worldX = e.getEventCol()*gp.getTileSize();
            int worldY = e.getEventRow()*gp.getTileSize();
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
            
            if(!(screenX > gp.getWidth() + gp.getTileSize() || screenX < gp.getTileSize()*-1 || 
                    screenY > gp.getHeight() + gp.getTileSize() || screenY < gp.getTileSize()*-1))   {
                
                int x = screenX + eventRect.x;
                int y = screenY + eventRect.y;
                
                Color oldColor = g2.getColor();
                g2.setColor(Color.red);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
                g2.drawString("Event:", x, y-13);
                g2.drawString("X: " + worldX/gp.getTileSize() + " Y: " + worldY/gp.getTileSize(), x, y-3);
                g2.fillRect(x, y, eventRect.width, eventRect.height);
                g2.setColor(oldColor);
                
            }
        }
    }
    
    public boolean hit(Event e){
        boolean hit = false;
        
        //Set collision rectangle of player
        gp.getPlayer().getSolidArea().x += gp.getPlayer().getWorldX();
        gp.getPlayer().getSolidArea().y += gp.getPlayer().getWorldY();
        //Set collision rectangle of event
        eventRect.x += e.getEventCol()*gp.getTileSize();
        eventRect.y += e.getEventRow()*gp.getTileSize();
        
        if(gp.getPlayer().getSolidArea().intersects(eventRect)){
            System.out.println(gp.getPlayer().getSolidArea().intersects(eventRect));
            if(gp.getPlayer().getDirection().equalsIgnoreCase(e.getReqDirection()) 
                    || e.getReqDirection().equalsIgnoreCase("any")){
                hit = true;
            }
        }
        
        gp.getPlayer().resetSolidArea();
        resetEventRect();
        
        return hit;
    }
    
    private void resetEventRect(){
        eventRect.x = eventDefaultX;
        eventRect.y = eventDefaultY;
    }
    
}
