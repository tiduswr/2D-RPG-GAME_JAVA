package entity;

import animation.TimedAnimation;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class NPC_WhiteMage extends Entity{
    
    public NPC_WhiteMage(GamePanel gp, int row, int col){
        super(gp, EntityType.NPC);
        this.worldX = gp.getTileSize()*row;
        this.worldY = gp.getTileSize()*col;
        this.direction = Direction.DOWN;
        stats.name = "White Mage";
        getImages();
        createDialogues();
    }
    
    private void getImages(){
        //Animations
        up = new TimedAnimation(new BufferedImage[]{makeSprite("npc/whiteMage_u1.png"),
            makeSprite("npc/whiteMage_u2.png")}, 12/(60/gp.getFPS()));
        down = new TimedAnimation(new BufferedImage[]{makeSprite("npc/whiteMage_d1.png"),
            makeSprite("npc/whiteMage_d2.png")}, 12/(60/gp.getFPS()));
        left = new TimedAnimation(new BufferedImage[]{makeSprite("npc/whiteMage_l1.png"),
            makeSprite("npc/whiteMage_l2.png")}, 12/(60/gp.getFPS()));
        right = new TimedAnimation(new BufferedImage[]{makeSprite("npc/whiteMage_r1.png"),
            makeSprite("npc/whiteMage_r2.png")}, 12/(60/gp.getFPS()));
    }
    
    private void createDialogues(){
        dialogues = new String[4];
        
        dialogues[0] = "Que Hydalin te abençoe!";
        dialogues[1] = "Você percebeu que eu fico batendo a cara na parede direto?";
        dialogues[2] = "O Mago Negro fica olhando pra mim direto";
        dialogues[3] = "Eu posso te curar se você precisar!";
    }
    
    @Override
    public void setAction() {
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
    public void speak(){
        super.speak();
        
        Random rand = new Random();
        int i = rand.nextInt(dialogues.length);
        
        gp.getGameUI().setCurrentDialog(dialogues[i]);
    }
    
}
