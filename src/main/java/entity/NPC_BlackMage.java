package entity;

import animation.TimedAnimation;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class NPC_BlackMage extends Entity{
    
    public NPC_BlackMage(GamePanel gp, int row, int col){
        super(gp, EntityType.NPC);
        this.worldX = gp.getTileSize()*row;
        this.worldY = gp.getTileSize()*col;
        this.direction = Direction.DOWN;
        stats.name = "Black Mage";
        getImages();
        createDialogues();
    }
    
    private void createDialogues(){
        dialogues = new String[4];
        
        dialogues[0] = "Olá Warrior of Darkness!";
        dialogues[1] = "Porque estamos aqui?";
        dialogues[2] = "A Maga Branca é bem gata não acha?";
        dialogues[3] = "To com fome :(";
    }
    
    private void getImages(){
        //Animations
        up = new TimedAnimation(new BufferedImage[]{makeSprite("npc/blackMage_u1.png"),
            makeSprite("npc/blackMage_u2.png")}, 12/(60/gp.getFPS()));
        down = new TimedAnimation(new BufferedImage[]{makeSprite("npc/blackMage_d1.png"),
            makeSprite("npc/blackMage_d2.png")}, 12/(60/gp.getFPS()));
        left = new TimedAnimation(new BufferedImage[]{makeSprite("npc/blackMage_l1.png"),
            makeSprite("npc/blackMage_l2.png")}, 12/(60/gp.getFPS()));
        right = new TimedAnimation(new BufferedImage[]{makeSprite("npc/blackMage_r1.png"),
            makeSprite("npc/blackMage_r2.png")}, 12/(60/gp.getFPS()));
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
