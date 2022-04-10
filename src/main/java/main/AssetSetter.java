package main;

import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_SpeedUp;
import object.SuperObject;

public class AssetSetter {
    private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject(){
        spawnObject(new OBJ_Key(gp), 23, 7, 0);
        spawnObject(new OBJ_Key(gp), 23, 40, 1);
        spawnObject(new OBJ_Key(gp), 37, 7, 2);
        
        spawnObject(new OBJ_Door(gp), 10, 11, 3);
        spawnObject(new OBJ_Door(gp), 8, 28, 4);
        spawnObject(new OBJ_Door(gp), 12, 22, 5);
        
        spawnObject(new OBJ_Chest(gp), 10, 7, 6);
        
        spawnObject(new OBJ_SpeedUp(gp), 37, 42, 7);
    }
    
    private void spawnObject(SuperObject obj, int row, int col, int index){
        gp.getObj()[index] = obj;
        gp.getObj()[index].setWorldX(row * gp.getTileSize());
        gp.getObj()[index].setWorldY(col * gp.getTileSize());
    }
}
