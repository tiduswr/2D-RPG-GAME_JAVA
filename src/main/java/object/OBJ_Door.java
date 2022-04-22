package object;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Door extends SuperObject{
    
    public OBJ_Door(GamePanel gp){
        super(gp);
        name = "Door";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
        } catch (IOException ex) {
            Logger.getLogger(OBJ_Key.class.getName()).log(Level.SEVERE, null, ex);
        }
        collision = true;
        
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        if(gp.getPlayer().getQtdKeys() > 0){
            gp.getPlayer().removeKeys(1);
            gp.getGameUI().showMessage("Door Openned!");
            gp.playSoundEffect("doorUnlock",1);
            return true;
        }else{
            gp.getGameUI().showMessage("You need a Key!");
        }
        return false;
    }
}
