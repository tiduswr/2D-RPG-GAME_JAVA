package main;

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
}
