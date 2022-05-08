package animation;

import java.awt.image.BufferedImage;

public class TimedAnimation extends Animation{
    private int spriteCounter, standCounter, index;
    private final int FRAMES, RESET_FRAMES;
    
    public TimedAnimation(BufferedImage[] sprites, int frames){
        super(sprites);
        spriteCounter = 0;
        index = 0;
        standCounter = 0;
        this.FRAMES = frames;
        this.RESET_FRAMES = frames;
        running = true;
    }
    
    public int getCurentSpriteIndex(){
        return index;
    }
    
    public void forceReset(){
        index = 0;
        spriteCounter = 0;
    }
    
    @Override
    public BufferedImage getCurrentSprite(){
        return sprites[index];
    }
    
    @Override
    public void resetAnimation(){
        if(running){
            if(standCounter == RESET_FRAMES){
                index = 0;
                standCounter = 0;
            }
            standCounter++;
        }
    }
    
    @Override
    public void updateSprite(){
        if(running){
            if(spriteCounter >= FRAMES){
                if(index == sprites.length - 1){
                    index = 0;
                }else{
                    index++;
                }
                spriteCounter = 0;
            }
            spriteCounter++;
        }
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public int getStandCounter() {
        return standCounter;
    }

    public int getIndex() {
        return index;
    }

    public int getFRAMES() {
        return FRAMES;
    }

    public int getRESET_FRAMES() {
        return RESET_FRAMES;
    }
    
}
