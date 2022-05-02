package object;

import main.GamePanel;

public class OBJ_WoodShield extends  OBJ_Equipment{
    
    public OBJ_WoodShield(GamePanel gp) {
        super(gp);
        name = "Wood Shield";
        description = "A simple Shield made with wood.";
        image = makeSprite("objects/armor/woodShield.png");
        attackPoints = 0;
        defensePoints = 1;
    }
    
}
