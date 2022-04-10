package main;

import entity.Entity;
import object.SuperObject;

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
    
    public int checkObject(Entity e, boolean player){
        
        int i = 0;
        int index = -1;
        
        for(SuperObject o : gp.getObj()){
            if(o != null){
                //Get entity solid area position
                e.getSolidArea().x = e.getWorldX() + e.getSolidArea().x;
                e.getSolidArea().y = e.getWorldY() + e.getSolidArea().y;
                
                //Get object solid area pos
                o.getSolidArea().x = o.getWorldX() + o.getSolidArea().x;
                o.getSolidArea().y = o.getWorldY() + o.getSolidArea().y;
                
                switch(e.getDirection()){
                    case "up":
                        e.getSolidArea().y -= e.getSpeed();
                        if(e.getSolidArea().intersects(o.getSolidArea())){
                            if(o.isCollision()){
                                e.setCollisionOn(true);
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        e.getSolidArea().y += e.getSpeed();
                        if(e.getSolidArea().intersects(o.getSolidArea())){
                            if(o.isCollision()){
                                e.setCollisionOn(true);
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        e.getSolidArea().x -= e.getSpeed();
                        if(e.getSolidArea().intersects(o.getSolidArea())){
                            if(o.isCollision()){
                                e.setCollisionOn(true);
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        e.getSolidArea().x += e.getSpeed();
                        if(e.getSolidArea().intersects(o.getSolidArea())){
                            if(o.isCollision()){
                                e.setCollisionOn(true);
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                }                
                
                //Reset solid area position
                e.getSolidArea().x = e.getSolidAreaDefaultX();
                e.getSolidArea().y = e.getSolidAreaDefaultY();
                o.getSolidArea().x = o.getSolidAreaDefaultX();
                o.getSolidArea().y = o.getSolidAreaDefaultY();
                
            }
            i++;
        }
        return index;
    }
}
