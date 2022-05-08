package object;

import animation.TimedAnimation;
import main.GamePanel;

import java.awt.*;

public abstract class OBJ_Weapon extends SuperObject{
    protected int attackPoints, defensePoints;

    public OBJ_Weapon(GamePanel gp) {
        super(gp);
        defensePoints = 0;
        attackPoints = 0;
    }
    
    public TimedAnimation[] getSprites(Rectangle attackArea){return null;};
    public int getAttackPoints(){return attackPoints;};
    public int getDefensePoints(){return defensePoints;};

    @Override
    public boolean executeAction() {
        super.executeAction();
        return true;
    }

    @Override
    public boolean executeInventoryAction() {
        gp.getPlayer().equipWeapon(this);
        return false;
    }

}
