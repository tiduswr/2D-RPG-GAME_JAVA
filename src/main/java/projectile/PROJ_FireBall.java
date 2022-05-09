package projectile;

import animation.TimedAnimation;
import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class PROJ_FireBall extends Projectile{
    public PROJ_FireBall(GamePanel gp, Entity user) {
        super(gp, user);
        name = "FireBall";
        speed = 5;
        cost = 1;
        framesVisible = 80;
        loadImages();
    }

    public int calculateAttack(int mag){
        return (int) (mag*(3.0f));
    }



    public void loadImages(){
        //Animations
        up = new TimedAnimation(new BufferedImage[]{makeSprite("projectiles/fireball_u1.png"),
                makeSprite("projectiles/fireball_u2.png")}, 12/(60/gp.getFPS()));
        down = new TimedAnimation(new BufferedImage[]{makeSprite("projectiles/fireball_d1.png"),
                makeSprite("projectiles/fireball_d2.png")}, 12/(60/gp.getFPS()));
        left = new TimedAnimation(new BufferedImage[]{makeSprite("projectiles/fireball_l1.png"),
                makeSprite("projectiles/fireball_l2.png")}, 12/(60/gp.getFPS()));
        right = new TimedAnimation(new BufferedImage[]{makeSprite("projectiles/fireball_r1.png"),
                makeSprite("projectiles/fireball_r2.png")}, 12/(60/gp.getFPS()));
    }

}
