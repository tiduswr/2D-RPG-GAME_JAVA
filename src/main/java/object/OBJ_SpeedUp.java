package object;

import main.GamePanel;

public class OBJ_SpeedUp extends SuperObject{
    
    public OBJ_SpeedUp(GamePanel gp){
        super(gp);
        name = "SpeedUp";
        image = makeSprite("objects/speedUp.png");
    }
    public boolean executeAction() {
        super.executeAction();
        float newSpeed = gp.getPlayer().getStats().getSpeed() * 0.20f ;
        
        gp.getPlayer().getStats().setSpeed(gp.getPlayer().getStats().getSpeed() + (int) Math.ceil(newSpeed));
        gp.playSoundEffect("speedUp",1);
        return true;
    }
}