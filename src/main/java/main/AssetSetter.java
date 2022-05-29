package main;

import entity.NPC_BlackMage;
import entity.NPC_WhiteMage;
import entity.monster.MON_FireSnake;
import entity.monster.MON_GreenSlime;
import object.*;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(){
        spawnObject(new OBJ_Key(gp), 25, 23);
        spawnObject(new OBJ_Potion(gp), 21, 19);
        spawnObject(new OBJ_ManaPotion(gp), 26, 21);
        spawnObject(new OBJ_Axe(gp), 33, 21);
        spawnObject(new OBJ_IronShield(gp), 35, 21);
        spawnObject(new OBJ_Coin(gp, 50), 37, 21);
    }
    
    public void spawnObject(SuperObject obj, int row, int col){
        obj.setWorldX(row * gp.getTileSize());
        obj.setWorldY(col * gp.getTileSize());
        gp.addObject(obj);
    }

    public void spawnObjectByWorldLocation(SuperObject obj, int worldX, int worldY){
        obj.setWorldX(worldX);
        obj.setWorldY(worldY);
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
        gp.addMonster(new MON_FireSnake(gp, 34, 42));
        gp.addMonster(new MON_FireSnake(gp, 38, 42));
    }
    
}
