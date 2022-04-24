package animation;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class DyingAnimation {
    private int counter;
    private final int DURATION;
    
    public DyingAnimation(int frameDuration) {
        counter = 0;
        this.DURATION = frameDuration;
    }
    
    public boolean update(Graphics2D g2){
        counter++;
        if(counter > DURATION){
            return false;
        }else if(counter%5 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }else if(counter%10 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        return true;
    }
    
}
