package object;

import main.GamePanel;

public class OBJ_IronShield extends  OBJ_Equipment{

    public OBJ_IronShield(GamePanel gp) {
        super(gp);
        name = "Iron Shield";
        description = "A simple Shield made with iron.";
        image = makeSprite("objects/armor/ironShield.png");
        attackPoints = 0;
        defensePoints = 2;
    }
    
}
