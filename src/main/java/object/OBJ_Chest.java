package object;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Chest extends SuperObject{
    
    public OBJ_Chest(GamePanel gp){
        super(gp);
        name = "Chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/treasureChest.png"));
        } catch (IOException ex) {
            Logger.getLogger(OBJ_Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public boolean executeAction() {
       gp.getGameUI().setGameFinished(true);
       gp.playSoundEffect("chestUnlock",1);
       return true;
    }
}