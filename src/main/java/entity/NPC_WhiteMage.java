package entity;

import java.util.Random;
import main.GamePanel;

public class NPC_WhiteMage extends Entity{
    
    public NPC_WhiteMage(GamePanel gp, int row, int col){
        super(gp, EntityType.NPC);
        this.worldX = gp.getTileSize()*row;
        this.worldY = gp.getTileSize()*col;
        this.direction = "down";
        name = "White Mage";
        getImages();
        createDialogues();
    }
    
    private void getImages(){
        u1 = makeSprite("npc/whiteMage_u1.png");
        u2 = makeSprite("npc/whiteMage_u2.png");
        l1 = makeSprite("npc/whiteMage_l1.png");
        l2 = makeSprite("npc/whiteMage_l2.png");
        r1 = makeSprite("npc/whiteMage_r1.png");
        r2 = makeSprite("npc/whiteMage_r2.png");
        d1 = makeSprite("npc/whiteMage_d1.png");
        d2 = makeSprite("npc/whiteMage_d2.png");
    }
    
    private void createDialogues(){
        dialogues = new String[4];
        
        dialogues[0] = "Que Hydalin te abençoe!";
        dialogues[1] = "Você percebeu que eu fico batendo \na cara na parede direto?";
        dialogues[2] = "O Mago Negro fica olhando pra \nmim direto";
        dialogues[3] = "Eu posso te curar se você\nprecisar!";
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
