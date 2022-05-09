package event;

import entity.damage.FixedDamage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;

import entity.Direction;
import main.GamePanel;
import main.GameState;

public final class EventHandler {
    private GamePanel gp;
    private EventRect eventRect[][];
    private final HashMap<String ,Event> events;
    private int previousEventX = 0, previousEventY = 0;
    private boolean canTouchEvent = true;
    
    public EventHandler(GamePanel gp) {
        this.events = new HashMap<>();
        this.gp = gp;
        
        //Initialize event Rectangles
        eventRect = new EventRect[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        for(int i = 0; i < gp.getMaxWorldCol(); i++){
            for(int j = 0; j < gp.getMaxWorldRow(); j++){
                eventRect[i][j] = new EventRect();
                eventRect[i][j].x = 23;
                eventRect[i][j].y = 23;
                eventRect[i][j].width = 2;
                eventRect[i][j].height = 2;
                eventRect[i][j].setDefaultEventX(eventRect[i][j].x);
                eventRect[i][j].setDefaultEventY(eventRect[i][j].y);
            }
        }
        
        //Event test
        Event damageTeste = new Event(24, 20, Direction.ANY){
            @Override
            public void execute() {
                gp.setGameState(GameState.DIALOG_STATE);
                gp.getGameUI().setCurrentDialog("Você levou uma topada! kkkkk");
                gp.getPlayer().doDamage(new FixedDamage(1), Direction.UP);
                setCanTouchEvent(false);
            }
        };
        addEvent(damageTeste, "damageTeste");
        
        Event healingTeste = new Event(23, 12, Direction.UP){
            @Override
            public void execute() {
                if(gp.getKeyH().iszPressed()){
                    gp.getPlayer().setAtkCanceled(true);
                    gp.getGameUI().setCurrentDialog("Você bebeu da fonte... Você se sente recuperado!");
                    gp.setGameState(GameState.DIALOG_STATE);
                    gp.getPlayer().resetLife();
                    gp.getPlayer().getStats().setMana(gp.getPlayer().getStats().getMaxMana());
                    gp.playSoundEffect("cure", 0.5f);
                }
            }
        };
        addEvent(healingTeste, "healingTeste");
        
        Event teleportTeste = new Event(27, 16, Direction.RIGHT){
            @Override
            public void execute() {
                gp.getGameUI().setCurrentDialog("Você teleportou!");
                gp.getPlayer().setWorldX(gp.getTileSize()*37);
                gp.getPlayer().setWorldY(gp.getTileSize()*10);
                gp.setGameState(GameState.DIALOG_STATE);
                eventRect[getEventCol()][getEventRow()].setEventDone(true);
            }
        };
        addEvent(teleportTeste, "teleportTeste");
        
    }
    
    public void checkEvent(){
        int xDistance = Math.abs(gp.getPlayer().getWorldX() - previousEventX);
        int yDistance = Math.abs(gp.getPlayer().getWorldY() - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.getTileSize()/2){
            canTouchEvent = true;
        }
        
        for(Event e : events.values()){
            if(hit(e)){
                if(canTouchEvent){
                    e.execute();
                }
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
            EventRect checked = eventRect[e.getEventCol()][e.getEventRow()];
            
            int worldX = e.getEventCol()*gp.getTileSize();
            int worldY = e.getEventRow()*gp.getTileSize();
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
            
            if(!(screenX > gp.getWidth() + gp.getTileSize() || screenX < gp.getTileSize()*-1 || 
                    screenY > gp.getHeight() + gp.getTileSize() || screenY < gp.getTileSize()*-1))   {
                
                int x = screenX + checked.x;
                int y = screenY + checked.y;
                
                Color oldColor = g2.getColor();
                g2.setColor(Color.red);
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12F));
                g2.drawString("Event:", x, y-13);
                g2.drawString("X: " + worldX/gp.getTileSize() + " Y: " + worldY/gp.getTileSize(), x, y-3);
                g2.fillRect(x, y, checked.width, checked.height);
                g2.setColor(oldColor);
                
            }
        }
    }
    
    public boolean hit(Event e){
        boolean hit = false;
        EventRect checked = eventRect[e.getEventCol()][e.getEventRow()];
        
        if(!checked.isEventDone()){
            //Set collision rectangle of player
            gp.getPlayer().getSolidArea().x += gp.getPlayer().getWorldX();
            gp.getPlayer().getSolidArea().y += gp.getPlayer().getWorldY();
            //Set collision rectangle of event
            checked.x += e.getEventCol()*gp.getTileSize();
            checked.y += e.getEventRow()*gp.getTileSize();

            if(gp.getPlayer().getSolidArea().intersects(checked)){
                if(gp.getPlayer().getDirection() == e.getReqDirection() 
                        || e.getReqDirection() == Direction.ANY){
                    hit = true;
                    previousEventX = gp.getPlayer().getWorldX();
                    previousEventY = gp.getPlayer().getWorldY();
                }
            }

            gp.getPlayer().resetSolidArea();
            checked.x = checked.getDefaultEventX();
            checked.y = checked.getDefaultEventY();
        }
        
        return hit;
    }

    public boolean isCanTouchEvent() {
        return canTouchEvent;
    }

    public void setCanTouchEvent(boolean canTouchEvent) {
        this.canTouchEvent = canTouchEvent;
    }
    
}
