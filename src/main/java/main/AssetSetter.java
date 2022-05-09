package main;

import entity.NPC_BlackMage;
import entity.NPC_WhiteMage;
import entity.monster.MON_GreenSlime;
import object.*;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(){
        spawnObject(new OBJ_Key(gp), 25, 23, 0);
        spawnObject(new OBJ_Potion(gp), 21, 19, 1);
        spawnObject(new OBJ_ManaPotion(gp), 26, 21, 2);
        spawnObject(new OBJ_Axe(gp), 33, 21, 2);
        spawnObject(new OBJ_IronShield(gp), 35, 21, 2);
    }
    
    private void spawnObject(SuperObject obj, int row, int col, int index){
        obj.setWorldX(row * gp.getTileSize());
        obj.setWorldY(col * gp.getTileSize());
        gp.addObject(obj);
    }
    
    public void setNPCS(){
        gp.addNpc(new NPC_BlackMage(gp, 21, 21));
        gp.addNpc(new NPC_WhiteMage(gp, 26, 21));
    }
    
    public void spawnMonsters(){
        gp.addMonster(new MON_GreenSlime(gp, 21, 38));
        gp.addMonster(new MON_GreenSlime(gp, 23, 42));
        gp.addMonster(new MON_GreenSlime(gp, 24, 37));
        gp.addMonster(new MON_GreenSlime(gp, 34, 42));
        gp.addMonster(new MON_GreenSlime(gp, 38, 42));
    }
    
}
