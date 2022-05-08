package object;

import animation.TimedAnimation;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class OBJ_GreatSword extends OBJ_Weapon{

    public OBJ_GreatSword(GamePanel gp) {
        super(gp);
        name = "GreatSword";
        description = "A Great Sword made with iron.";
        image = makeSprite("objects/weapons/greatSword.png");
        attackPoints = 2;
        defensePoints = 0;
    }
    
    @Override
    public TimedAnimation[] getSprites(Rectangle attackArea){
        //HITBOX
        attackArea.width = 36;
        attackArea.height = 36;
        
        //Animations
        TimedAnimation atkUp = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/greatSword_u1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/greatSword_u2.png", gp.getTileSize(), gp.getTileSize()*2)}, 15);
        TimedAnimation atkDown = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/greatSword_d1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/greatSword_d2.png", gp.getTileSize(), gp.getTileSize()*2)}, 15);
        TimedAnimation atkLeft = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/greatSword_l1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/greatSword_l2.png", gp.getTileSize()*2, gp.getTileSize())}, 15);
        TimedAnimation atkRight = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/greatSword_r1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/greatSword_r2.png", gp.getTileSize()*2, gp.getTileSize())}, 15);
        TimedAnimation[] arr = {atkUp, atkDown, atkLeft, atkRight};
        
        return arr;
    }
}
