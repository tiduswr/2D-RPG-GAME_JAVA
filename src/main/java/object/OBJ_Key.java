package object;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Key extends SuperObject{
    public OBJ_Key(GamePanel gp){
        super(gp);
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        } catch (IOException ex) {
            Logger.getLogger(OBJ_Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        gp.getPlayer().addKeys(1);
        gp.playSoundEffect(6);
        gp.getGameUI().showMessage("You got a Key!");
        System.out.println(gp.getPlayer().getQtdKeys());
        return true;
    }
}
