package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_WhiteMage extends Entity{
    
    public NPC_WhiteMage(GamePanel gp, int row, int col){
        super(gp);
        this.worldX = gp.getTileSize()*row;
        this.worldY = gp.getTileSize()*col;
        this.direction = "down";
        speed = 1;
        getImages();
        createDialogues();
    }
    
    private void getImages(){
        u1 = makePlayerSprite("npc/whiteMage_u1.png");
        u2 = makePlayerSprite("npc/whiteMage_u2.png");
        l1 = makePlayerSprite("npc/whiteMage_l1.png");
        l2 = makePlayerSprite("npc/whiteMage_l2.png");
        r1 = makePlayerSprite("npc/whiteMage_r1.png");
        r2 = makePlayerSprite("npc/whiteMage_r2.png");
        d1 = makePlayerSprite("npc/whiteMage_d1.png");
        d2 = makePlayerSprite("npc/whiteMage_d2.png");
    }
    
    private void createDialogues(){
        dialogues = new String[4];
        
        dialogues[0] = "Que Hydalin te abençoe!";
        dialogues[1] = "Você percebeu que eu fico batendo \na cara na parede direto?";
        dialogues[2] = "O Mago Negro fica olhando pra \nmim direto";
        dialogues[3] = "Eu posso te curar se você precisar!";
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
    
    @Override
    public void speak(){
        super.speak();
        
        Random rand = new Random();
        int i = rand.nextInt(dialogues.length);
        
        gp.getGameUI().setCurrentDialog(dialogues[i]);
    }
    
}
