package object;

import main.GamePanel;

public class OBJ_Key extends SuperObject{
    public OBJ_Key(GamePanel gp){
        super(gp);
        name = "Key";
        description = "A simple Key.";
        image = makeSprite("objects/key.png");
    }
    @Override
    public boolean executeAction() {
        super.executeAction();
        return true;
    }
}
