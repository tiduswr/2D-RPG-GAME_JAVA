package projectile;

import animation.TimedAnimation;
import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class PROJ_FireSpittle extends Projectile{
    public PROJ_FireSpittle(GamePanel gp, Entity user) {
        super(gp, user);
        name = "Fire Spittle";
        castSound = "spiting";
        speed = 5;
        cost = 1;
        solidArea.x = 8;
        solidArea.y = 14;
        solidArea.width = 27;
        solidArea.height = 27;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        framesVisible = 80;
        loadImages();
    }

    public int calculateAttack(int mag){
        return (int) (mag*(2.0f));
    }

    public void loadImages(){
        //Animations
        BufferedImage img = makeSprite("projectiles/fireSpittle.png");
        up = new TimedAnimation(new BufferedImage[]{img}, 12/(60/gp.getFPS()));
        down = new TimedAnimation(new BufferedImage[]{img}, 12/(60/gp.getFPS()));
        left = new TimedAnimation(new BufferedImage[]{img}, 12/(60/gp.getFPS()));
        right = new TimedAnimation(new BufferedImage[]{img}, 12/(60/gp.getFPS()));
    }

}
