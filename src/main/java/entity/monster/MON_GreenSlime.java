package entity.monster;

import animation.TimedAnimation;
import entity.Direction;
import entity.Entity;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.damage.Damage;
import main.GamePanel;
import object.OBJ_Coin;
import ui.Message;

public class MON_GreenSlime extends Entity{
    public MON_GreenSlime(GamePanel gp, int col, int row){
        super(gp, EntityType.MONSTER);
        stats.setName("GreenSlime");
        stats.setSpeed(60/gp.getFPS());
        stats.setMaxLife(12);
        stats.setLife(stats.getMaxLife());
        worldX = col*gp.getTileSize();
        worldY = row*gp.getTileSize();

        //Touch damage
        damageOnTouch = true;

        //Stats
        stats.setAtk(5);
        stats.setDef(0);
        stats.setExp(2);
        
        solidArea.x = 3;
        solidArea.y = 17;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImages();
    }
    
    private void getImages(){
        //Animations
        up = new TimedAnimation(new BufferedImage[]{makeSprite("monsters/slime_u1.png"),
            makeSprite("monsters/slime_u2.png")}, 12/(60/gp.getFPS()));
        down = new TimedAnimation(new BufferedImage[]{makeSprite("monsters/slime_d1.png"),
            makeSprite("monsters/slime_d2.png")}, 12/(60/gp.getFPS()));
        left = new TimedAnimation(new BufferedImage[]{makeSprite("monsters/slime_l1.png"),
            makeSprite("monsters/slime_l2.png")}, 12/(60/gp.getFPS()));
        right = new TimedAnimation(new BufferedImage[]{makeSprite("monsters/slime_r1.png"),
            makeSprite("monsters/slime_r2.png")}, 12/(60/gp.getFPS()));
    }

    @Override
    public void setAction(){
        actionLockCounter++;
        
        if(actionLockCounter == 120){
            Random rand = new Random();
            int i = rand.nextInt(100)+1;

            if(i <=25){
                direction = Direction.UP;
            }else if(i > 25 && i <= 50){
                direction = Direction.DOWN;
            }else if(i > 50 && i <= 75){
                direction = Direction.LEFT;
            }else if(i > 75 && i <= 100){
                direction = Direction.RIGHT;
            }
            actionLockCounter = 0;
        }
    }

    @Override
    public void randomChooseDrop() {
        Random rand = new Random();
        int i = rand.nextInt(25)+1;
        dropItem(new OBJ_Coin(gp, i));
    }

    @Override
    public void damageReaction(Direction dir){
        actionLockCounter = 0;
        direction = dir;
    }
    
}
