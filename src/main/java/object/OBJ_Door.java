package object;

import main.GamePanel;

public class OBJ_Door extends SuperObject{
    
    public OBJ_Door(GamePanel gp){
        super(gp);
        name = "Door";
        image = makeSprite("objects/door.png");
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
            gp.playSoundEffect("doorUnlock",1);
            return true;
        }
        return false;
    }
}
