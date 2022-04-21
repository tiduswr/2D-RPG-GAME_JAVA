package object;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_SpeedUp extends SuperObject{
    
    public OBJ_SpeedUp(GamePanel gp){
        super(gp);
        name = "SpeedUp";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/speedUp.png"));
        } catch (IOException ex) {
            Logger.getLogger(OBJ_Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean executeAction() {
        super.executeAction();
        float newSpeed = gp.getPlayer().getSpeed() * 0.20f ;
        
        gp.getPlayer().setSpeed(gp.getPlayer().getSpeed() + (int) Math.ceil(newSpeed));
        gp.getGameUI().showMessage("Player Speed Increased!");
        gp.playSoundEffect("speedUp",1);
        return true;
    }
}