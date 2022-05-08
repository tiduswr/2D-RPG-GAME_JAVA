package object;

import main.GamePanel;

public class OBJ_Potion extends SuperObject{
    public OBJ_Potion(GamePanel gp){
        super(gp);
        name = "Small Potion";
        description = "A potion that restore 3 hp.";
        image = makeSprite("objects/potion.png");
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        return true;
    }

    @Override
    public boolean executeInventoryAction() {
        gp.getPlayer().heal(3);
        gp.playSoundEffect("cure", 0.4f);
        return true;
    }
}
