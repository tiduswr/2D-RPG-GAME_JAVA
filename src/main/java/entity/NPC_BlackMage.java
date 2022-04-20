package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_BlackMage extends Entity{
    
    public NPC_BlackMage(GamePanel gp, int row, int col){
        super(gp);
        this.worldX = gp.getTileSize()*row;
        this.worldY = gp.getTileSize()*col;
        this.direction = "down";
        speed = 1;
        getImages();
    }
    
    private void getImages(){
        u1 = makePlayerSprite("npc/blackMage_u1.png");
        u2 = makePlayerSprite("npc/blackMage_u2.png");
        l1 = makePlayerSprite("npc/blackMage_l1.png");
        l2 = makePlayerSprite("npc/blackMage_l2.png");
        r1 = makePlayerSprite("npc/blackMage_r1.png");
        r2 = makePlayerSprite("npc/blackMage_r2.png");
        d1 = makePlayerSprite("npc/blackMage_d1.png");
        d2 = makePlayerSprite("npc/blackMage_d2.png");
    }
    
    @Override
    public void setAction() {
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
