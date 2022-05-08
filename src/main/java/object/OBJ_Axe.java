package object;

import animation.TimedAnimation;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OBJ_Axe extends OBJ_Weapon{

    public OBJ_Axe(GamePanel gp) {
        super(gp);
        name = "GreatSword";
        description = "A axe, its good to cut things.";
        image = makeSprite("objects/weapons/axe.png");
        attackPoints = 1;
        defensePoints = 0;
    }
    
    @Override
    public TimedAnimation[] getSprites(Rectangle attackArea){
        //HITBOX
        attackArea.width = 36;
        attackArea.height = 36;
        
        //Animations
        TimedAnimation atkUp = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/axe_u1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/axe_u2.png", gp.getTileSize(), gp.getTileSize()*2)}, 7);
        TimedAnimation atkDown = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/axe_d1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/axe_d2.png", gp.getTileSize(), gp.getTileSize()*2)}, 7);
        TimedAnimation atkLeft = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/axe_l1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/axe_l2.png", gp.getTileSize()*2, gp.getTileSize())}, 7);
        TimedAnimation atkRight = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/axe_r1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/axe_r2.png", gp.getTileSize()*2, gp.getTileSize())}, 7);
        TimedAnimation[] arr = {atkUp, atkDown, atkLeft, atkRight};
        
        return arr;
    }
    
}
