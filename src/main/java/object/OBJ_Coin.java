package object;

import animation.TimedAnimation;
import main.GamePanel;
import ui.Message;

import java.awt.*;

public class OBJ_Coin extends SuperObject{
    private final int qtdCoins;

    public OBJ_Coin(GamePanel gp, int qtdCoins){
        super(gp);
        name = "Gil";
        description = "Its good to buy items.";
        this.qtdCoins = qtdCoins;
        getSprite();
        objType = ObjectType.PICKUP_ONLY;
    }

    private void getSprite(){
        if(qtdCoins < 10){
            image = makeSprite("objects/coin/coin1.png");
        }else if(qtdCoins < 20){
            image = makeSprite("objects/coin/coin2.png");
        }else if(qtdCoins < 30){
            image = makeSprite("objects/coin/coin3.png");
        }else if(qtdCoins < 40){
            image = makeSprite("objects/coin/coin4.png");
        }else if(qtdCoins < 50){
            image = makeSprite("objects/coin/coin5.png");
        }else{
            image = makeSprite("objects/coin/coin6.png");
        }
    }

    @Override
    public boolean executeAction() {
        int currentGil = gp.getPlayer().getStats().getGil();
        int updatedGil = currentGil + qtdCoins;
        gp.getPlayer().getStats().setGil(updatedGil);
        gp.getGameUI().getScrollMsg().addMessage(new Message("Earned " + updatedGil + " Gil."));
        return true;
    }
}
