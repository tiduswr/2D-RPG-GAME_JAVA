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

        //Find key
        OBJ_Key key = null;
        for(SuperObject o : gp.getPlayer().getInventory()){
            if(o instanceof OBJ_Key) {
                key = (OBJ_Key) o;
                break;
            }
        }
        if(key != null){
            gp.playSoundEffect("doorUnlock",1);
            gp.getPlayer().removeItem(key);
            gp.removeObject(this);
        }

        return false;
    }
}
