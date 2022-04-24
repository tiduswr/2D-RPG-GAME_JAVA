package object;

import animation.TimedAnimation;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class OBJ_GreatSword extends OBJ_Equipment{
    
    
    public OBJ_GreatSword(GamePanel gp) {
        super(gp);
        name = "GreatSword";
        image = makeSprite("objects/weapons/greatSword.png");
        attackPoints = 2;
        defensePoints = 0;
    }
    
    @Override
    public TimedAnimation[] greatSwordSprites(Rectangle attackArea){
        //HITBOX
        attackArea.width = 36;
        attackArea.height = 36;
        
        //Animations
        TimedAnimation atkUp = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/atk_u1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/atk_u2.png", gp.getTileSize(), gp.getTileSize()*2)}, 15, 20);
        TimedAnimation atkDown = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/atk_d1.png", gp.getTileSize(), gp.getTileSize()*2)
                                    ,makeSprite("player/atk_d2.png", gp.getTileSize(), gp.getTileSize()*2)}, 15, 20);
        TimedAnimation atkLeft = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/atk_l1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/atk_l2.png", gp.getTileSize()*2, gp.getTileSize())}, 15, 20);
        TimedAnimation atkRight = new TimedAnimation(
                new BufferedImage[]{makeSprite("player/atk_r1.png", gp.getTileSize()*2, gp.getTileSize())
                                    ,makeSprite("player/atk_r2.png", gp.getTileSize()*2, gp.getTileSize())}, 15, 20);
        TimedAnimation[] arr = {atkUp, atkDown, atkLeft, atkRight};
        
        return arr;
    }
    
}
