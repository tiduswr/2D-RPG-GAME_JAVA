package object;

import main.GamePanel;

public class OBJ_ManaPotion extends SuperObject{
    public OBJ_ManaPotion(GamePanel gp){
        super(gp);
        name = "Small Mana Potion";
        description = "Restores 2 mp.";
        image = makeSprite("objects/manaPotion.png");
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        return true;
    }

    @Override
    public boolean executeInventoryAction() {
        gp.getPlayer().recoverMp(2);
        gp.playSoundEffect("cure", 0.4f);
        return true;
    }
}
