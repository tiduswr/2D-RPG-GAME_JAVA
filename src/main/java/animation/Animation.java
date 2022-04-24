package animation;

import java.awt.image.BufferedImage;

public class Animation {
    protected int index;
    protected final BufferedImage[] sprites;
    protected boolean running;
    
    public Animation(BufferedImage[] sprites){
        this.sprites = sprites;
        index = 0;
        running = true;
    }
    
    public void stop(){
        running = false;
    }
    
    public void start(){
        running = true;
    }
    
    public BufferedImage getCurrentSprite(){
        return sprites[index];
    }
    
    public void resetAnimation(){
        if(running){
            index = 0;
        }
    }
    
    public void updateSprite(){
        if(running){
            if(index == sprites.length - 1){
                index = 0;
            }else{
                index++;
            }
        }
    }
    
}
