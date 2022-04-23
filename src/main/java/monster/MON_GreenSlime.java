package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;

public class MON_GreenSlime extends Entity{
    public MON_GreenSlime(GamePanel gp, int col, int row){
        super(gp, EntityType.MONSTER);
        name = "GreenSlime";
        speed = 60/gp.getFPS();
        maxLife = 4;
        life = maxLife;
        worldX = col*gp.getTileSize();
        worldY = row*gp.getTileSize();
        
        solidArea.x = 3;
        solidArea.y = 17;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImages();
    }
    
    private void getImages(){
        u1 = makeSprite("monsters/slime_u1.png");
        u2 = makeSprite("monsters/slime_u2.png");
        l1 = makeSprite("monsters/slime_l1.png");
        l2 = makeSprite("monsters/slime_l2.png");
        r1 = makeSprite("monsters/slime_r1.png");
        r2 = makeSprite("monsters/slime_r2.png");
        d1 = makeSprite("monsters/slime_d1.png");
        d2 = makeSprite("monsters/slime_d2.png");
    }
    
    @Override
    public void setAction(){
        actionLockCounter++;
        
        if(actionLockCounter == 120){
            Random rand = new Random();
            int i = rand.nextInt(100)+1;

            if(i <=25){
                direction = "up";
            }else if(i > 25 && i <= 50){
                direction = "down";
            }else if(i > 50 && i <= 75){
                direction = "left";
            }else if(i > 75 && i <= 100){
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
    
}
