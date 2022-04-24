package object;

import main.GamePanel;

public class OBJ_Chest extends SuperObject{
    
    public OBJ_Chest(GamePanel gp){
        super(gp);
        name = "Chest";
        image = makeSprite("objects/treasureChest.png");
    }
    @Override
    public boolean executeAction() {
       gp.getGameUI().setGameFinished(true);
       gp.playSoundEffect("chestUnlock",1);
       return true;
    }
}