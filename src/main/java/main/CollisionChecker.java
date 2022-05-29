package main;

import entity.Entity;
import object.SuperObject;
import org.jetbrains.annotations.NotNull;
import projectile.Projectile;

import java.util.ArrayList;

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
            case UP:
                eTRow = (eTWorldY - e.getStats().getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eTRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case DOWN:
                eBRow = (eBWorldY + e.getStats().getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eBRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case LEFT:
                eLCol = (eLWorldX - e.getStats().getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                e.setCollisionOn(gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision());
                break;
            case RIGHT:
                eRCol = (eRWorldX + e.getStats().getSpeed())/gp.getTileSize();
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
                    case UP: e.getSolidArea().y -= e.getStats().getSpeed(); break;
                    case DOWN: e.getSolidArea().y += e.getStats().getSpeed(); break;
                    case LEFT: e.getSolidArea().x -= e.getStats().getSpeed(); break;
                    case RIGHT: e.getSolidArea().x += e.getStats().getSpeed(); break;
                }                
                if(e.getSolidArea().intersects(o.getSolidArea())){
                    if(o.isCollision()){
                        e.setCollisionOn(true);
                    }
                    if(player){
                        index = i;
                    }
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
    
    //NPC or Monster Collision
    public int checkEntity(Entity e, ArrayList<Entity> targets){
        int i = 0;
        int index = -1;
        
        for(Entity t : targets){
            if(t != null && t != e){
                //Get entity solid area position
                e.getSolidArea().x = e.getWorldX() + e.getSolidArea().x;
                e.getSolidArea().y = e.getWorldY() + e.getSolidArea().y;
                
                //Get object solid area pos
                t.getSolidArea().x = t.getWorldX() + t.getSolidArea().x;
                t.getSolidArea().y = t.getWorldY() + t.getSolidArea().y;
                
                switch(e.getDirection()){
                    case UP: e.getSolidArea().y -= e.getStats().getSpeed(); break;
                    case DOWN: e.getSolidArea().y += e.getStats().getSpeed(); break;
                    case LEFT: e.getSolidArea().x -= e.getStats().getSpeed(); break;
                    case RIGHT: e.getSolidArea().x += e.getStats().getSpeed(); break;
                }          
                if(e.getSolidArea().intersects(t.getSolidArea()) && t != e){
                    e.setCollisionOn(true);
                    index = i;
                }
                
                //Reset solid area position
                e.getSolidArea().x = e.getSolidAreaDefaultX();
                e.getSolidArea().y = e.getSolidAreaDefaultY();
                t.getSolidArea().x = t.getSolidAreaDefaultX();
                t.getSolidArea().y = t.getSolidAreaDefaultY();
                
            }
            i++;
        }
        return index;
    }

    //NPC or Monster Collision
    public int checkProjectile(Projectile e, ArrayList<Entity> targets){
        int i = 0;
        int index = -1;

        for(Entity t : targets){
            if(t != null){
                //Get entity solid area position
                e.getSolidArea().x = e.getWorldX() + e.getSolidArea().x;
                e.getSolidArea().y = e.getWorldY() + e.getSolidArea().y;

                //Get object solid area pos
                t.getSolidArea().x = t.getWorldX() + t.getSolidArea().x;
                t.getSolidArea().y = t.getWorldY() + t.getSolidArea().y;

                switch(e.getDirection()){
                    case UP: e.getSolidArea().y -= e.getSpeed(); break;
                    case DOWN: e.getSolidArea().y += e.getSpeed(); break;
                    case LEFT: e.getSolidArea().x -= e.getSpeed(); break;
                    case RIGHT: e.getSolidArea().x += e.getSpeed(); break;
                }
                if(e.getSolidArea().intersects(t.getSolidArea())){
                    e.setCollisionOn(true);
                    index = i;
                }

                //Reset solid area position
                e.getSolidArea().x = e.getSolidAreaDefaultX();
                e.getSolidArea().y = e.getSolidAreaDefaultY();
                t.getSolidArea().x = t.getSolidAreaDefaultX();
                t.getSolidArea().y = t.getSolidAreaDefaultY();

            }
            i++;
        }
        return index;
    }

    public boolean checkProjectileHitPlayer(Projectile e){
        boolean contact = false;
        if(e != null){
            Entity t = gp.getPlayer();

            //Get entity solid area position
            e.getSolidArea().x = e.getWorldX() + e.getSolidArea().x;
            e.getSolidArea().y = e.getWorldY() + e.getSolidArea().y;

            //Get object solid area pos
            t.getSolidArea().x = t.getWorldX() + t.getSolidArea().x;
            t.getSolidArea().y = t.getWorldY() + t.getSolidArea().y;

            switch(e.getDirection()){
                case UP:
                    e.getSolidArea().y -= e.getSpeed(); break;
                case DOWN:
                    e.getSolidArea().y += e.getSpeed(); break;
                case LEFT:
                    e.getSolidArea().x -= e.getSpeed(); break;
                case RIGHT:
                    e.getSolidArea().x += e.getSpeed(); break;
            }
            if(e.getSolidArea().intersects(t.getSolidArea())){
                e.setCollisionOn(true);
                contact = true;
            }

            //Reset solid area position
            e.getSolidArea().x = e.getSolidAreaDefaultX();
            e.getSolidArea().y = e.getSolidAreaDefaultY();
            t.getSolidArea().x = t.getSolidAreaDefaultX();
            t.getSolidArea().y = t.getSolidAreaDefaultY();
        }
        return contact;
    }

    public boolean checkProjectileHitWall(Projectile e){
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
            case UP:
                eTRow = (eTWorldY - e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eTRow);
                return gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision();
            case DOWN:
                eBRow = (eBWorldY + e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eBRow);
                return gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision();
            case LEFT:
                eLCol = (eLWorldX - e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eLCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eLCol, eBRow);
                return gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision();
            case RIGHT:
                eRCol = (eRWorldX + e.getSpeed())/gp.getTileSize();
                t1 = gp.getTileM().getMapTileNum(eRCol, eTRow);
                t2 = gp.getTileM().getMapTileNum(eRCol, eBRow);
                return gp.getTileM().getTile(t1).isCollision() || gp.getTileM().getTile(t2).isCollision();
        }
        return false;
    }

    public boolean checkPlayer(Entity e){
        boolean contact = false;
        if(e != null){
            Entity t = gp.getPlayer();
            
            //Get entity solid area position
            e.getSolidArea().x = e.getWorldX() + e.getSolidArea().x;
            e.getSolidArea().y = e.getWorldY() + e.getSolidArea().y;

            //Get object solid area pos
            t.getSolidArea().x = t.getWorldX() + t.getSolidArea().x;
            t.getSolidArea().y = t.getWorldY() + t.getSolidArea().y;

            switch(e.getDirection()){
                case UP:
                    e.getSolidArea().y -= e.getStats().getSpeed(); break;
                case DOWN:
                    e.getSolidArea().y += e.getStats().getSpeed(); break;
                case LEFT:
                    e.getSolidArea().x -= e.getStats().getSpeed(); break;
                case RIGHT:
                    e.getSolidArea().x += e.getStats().getSpeed(); break;
            }                
            if(e.getSolidArea().intersects(t.getSolidArea())){
                e.setCollisionOn(true);
                contact = true;
            }            
            
            //Reset solid area position
            e.getSolidArea().x = e.getSolidAreaDefaultX();
            e.getSolidArea().y = e.getSolidAreaDefaultY();
            t.getSolidArea().x = t.getSolidAreaDefaultX();
            t.getSolidArea().y = t.getSolidAreaDefaultY();
        }
        return contact;
    }
    
}