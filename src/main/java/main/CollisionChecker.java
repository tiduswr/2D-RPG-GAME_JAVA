package main;

import entity.Entity;

public class CollisionChecker {
    
    private GamePanel gp;
    
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    
    public void checkTile(Entity e){
        
        //Calcula os pontos do retangulo que forma a area de Colisão do personagem
        int eLWorldX = e.getWorldX() + e.getSolidArea().x;
        int eRWorldX = eLWorldX + e.getSolidArea().width;
        int eTWorldY = e.getWorldY() + e.getSolidArea().y;
        int eBWorldY = eTWorldY + e.getSolidArea().height;
        
        //Com base nos pixels dos calculos anteriores, é verificado a coluna e linha da tabela Tile do mapa
        int eLCol = eLWorldX/gp.getTileSize();
        int eRCol = eRWorldX/gp.getTileSize();
        int eTRow = eTWorldY/gp.getTileSize();
        int eBRow = eBWorldY/gp.getTileSize();
        
        int t1, t2;
        
        switch(e.getDirection()){
            case "up":
                eTRow = (eTWorldY - e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eTRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case "down":
                eBRow = (eBWorldY + e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eBRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case "left":
                eLCol = (eLWorldX - e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case "right":
                eRCol = (eRWorldX + e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eRCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eBRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
        }
    }
}
