package entity.monster;

import animation.TimedAnimation;
import entity.Direction;
import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin;
import projectile.PROJ_FireSpittle;
import projectile.Projectile;
import ui.Message;

import java.awt.image.BufferedImage;
import java.util.Random;

public class MON_FireSnake extends Entity{
    protected Projectile projectile;

    public MON_FireSnake(GamePanel gp, int col, int row){
        super(gp, EntityType.MONSTER);
        stats.setName("Fire Snake");
        stats.setSpeed(60/gp.getFPS());
        stats.setMaxLife(20);
        stats.setMag(3);
        stats.setLife(stats.getMaxLife());
        worldX = col*gp.getTileSize();
        worldY = row*gp.getTileSize();
        projectile = new PROJ_FireSpittle(gp, this);

        //Adjust hp bar
        adjustHpBarY = -20;

        //Stats
        stats.setAtk(7);
        stats.setDef(2);
        stats.setExp(4);
        
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
        up = new TimedAnimation(new BufferedImage[]{
                makeSprite("monsters/fireSnake/fireSnake_u1.png"),
                makeSprite("monsters/fireSnake/fireSnake_u2.png"),
                makeSprite("monsters/fireSnake/fireSnake_u3.png"),
                makeSprite("monsters/fireSnake/fireSnake_u4.png")},
                12/(60/gp.getFPS()));

        down = new TimedAnimation(new BufferedImage[]{
                makeSprite("monsters/fireSnake/fireSnake_d1.png"),
                makeSprite("monsters/fireSnake/fireSnake_d2.png"),
                makeSprite("monsters/fireSnake/fireSnake_d3.png"),
                makeSprite("monsters/fireSnake/fireSnake_d4.png")},
                12/(60/gp.getFPS()));

        left = new TimedAnimation(new BufferedImage[]{
                makeSprite("monsters/fireSnake/fireSnake_l1.png"),
                makeSprite("monsters/fireSnake/fireSnake_l2.png"),
                makeSprite("monsters/fireSnake/fireSnake_l3.png"),
                makeSprite("monsters/fireSnake/fireSnake_l4.png")},
                12/(60/gp.getFPS()));

        right = new TimedAnimation(new BufferedImage[]{
                makeSprite("monsters/fireSnake/fireSnake_r1.png"),
                makeSprite("monsters/fireSnake/fireSnake_r2.png"),
                makeSprite("monsters/fireSnake/fireSnake_r3.png"),
                makeSprite("monsters/fireSnake/fireSnake_r4.png")},
                12/(60/gp.getFPS()));
    }
    
    @Override
    public void setAction(){
        actionLockCounter++;
        Random rand = new Random();
        int i = rand.nextInt(100)+1;

        if(actionLockCounter == 120){
            if(i <=25){
                direction = Direction.UP;
            }else if(i <= 50){
                direction = Direction.DOWN;
            }else if(i <= 75){
                direction = Direction.LEFT;
            }else {
                direction = Direction.RIGHT;
            }
            actionLockCounter = 0;
        }

        //Fireball
        projectile.recastTimeUpdate();
        if(!projectile.isVisible() && i > 99){
            if(projectile.set(worldX, worldY, direction, true)){
                gp.addProjectile(projectile);
            }
        }

    }

    @Override
    public void randomChooseDrop() {
        Random rand = new Random();
        int i = rand.nextInt(40)+26;
        dropItem(new OBJ_Coin(gp, i));
    }

    @Override
    public void damageReaction(Direction dir){
        actionLockCounter = 0;
        switch (dir){
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.LEFT;
                break;
        }
    }
    
}
