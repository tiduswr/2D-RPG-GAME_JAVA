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
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        if(gp.getPlayer().getQtdKeys() > 0){
            gp.getPlayer().removeKeys(1);
            return true;
        }
        return false;
    }
}
