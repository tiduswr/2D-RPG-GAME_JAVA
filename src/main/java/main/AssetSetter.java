package main;

import entity.NPC_BlackMage;
import entity.NPC_WhiteMage;
import object.OBJ_Door;
import object.SuperObject;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(){
        spawnObject(new OBJ_Door(gp), 21, 22, 0);
    }
    
    private void spawnObject(SuperObject obj, int row, int col, int index){
        gp.getObj()[index] = obj;
        gp.getObj()[index].setWorldX(row * gp.getTileSize());
        gp.getObj()[index].setWorldY(col * gp.getTileSize());
    }
    
    public void setNPCS(){
        gp.getNpcs()[0] = new NPC_BlackMage(gp, 21, 21);
        gp.getNpcs()[1] = new NPC_WhiteMage(gp, 26, 21);
    }
    
}
