package object;

import main.GamePanel;

public class OBJ_Key extends SuperObject{
    public OBJ_Key(GamePanel gp){
        super(gp);
        name = "Key";
        image = makeSprite("objects/key.png");
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        gp.getPlayer().addKeys(1);
        gp.playSoundEffect("keyGet",1);
        return true;
    }
}
