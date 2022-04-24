package main;

import entity.NPC_BlackMage;
import entity.NPC_WhiteMage;
import entity.monster.MON_GreenSlime;
import object.SuperObject;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(){
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
    
    public void spawnMonsters(){
        gp.getMonsters()[0] = new MON_GreenSlime(gp, 23, 36);
        gp.getMonsters()[1] = new MON_GreenSlime(gp, 23, 37);
        gp.getMonsters()[2] = new MON_GreenSlime(gp, 23, 38);
    }
    
}
