package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import util.UtilityTool;

public final class TileManager {
    protected GamePanel gp;
    protected Tile[] tile;
    protected int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[50];
        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        getTileImage();
        loadMap("/maps/worldMapv2.txt");
    }
    
    public int getMapTileNum(int row, int col){
        return mapTileNum[row][col];
    }
    
    public Tile getTile(int tileNum){
        return tile[tileNum];
    }
    
    public void getTileImage(){
        
        makeTile(0, "grass00.png", false);
        makeTile(1, "grass01.png", false);
        makeTile(2, "water00.png", true);
        makeTile(3, "water01.png", true);
        makeTile(4, "water02.png", true);
        makeTile(5, "water03.png", true);
        makeTile(6, "water04.png", true);
        makeTile(7, "water05.png", true);
        makeTile(8, "water06.png", true);
        makeTile(9, "water07.png", true);
        makeTile(10, "water08.png", true);
        makeTile(11, "water09.png", true);
        makeTile(12, "water10.png", true);
        makeTile(13, "water11.png", true);
        makeTile(14, "water12.png", true);
        makeTile(15, "water13.png", true);
        makeTile(16, "road00.png", false);
        makeTile(17, "road01.png", false);
        makeTile(18, "road02.png", false);
        makeTile(19, "road03.png", false);
        makeTile(20, "road04.png", false);
        makeTile(21, "road05.png", false);
        makeTile(22, "road06.png", false);
        makeTile(23, "road07.png", false);
        makeTile(24, "road08.png", false);
        makeTile(25, "road09.png", false);
        makeTile(26, "road10.png", false);
        makeTile(27, "road11.png", false);
        makeTile(28, "road12.png", false);
        makeTile(29, "earth.png", false);
        makeTile(30, "wall.png", true);
        makeTile(31, "tree.png", true);
        
    }
    
    private void makeTile(int index, String imgName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        
        try {
            tile[index] = new Tile();
            tile[index].setImage(ImageIO.read(getClass().getResourceAsStream("/tiles/" + imgName)));
            tile[index].setImage(uTool.scaleImage(tile[index].image, gp.getTileSize(), gp.getTileSize()));
            tile[index].setCollision(collision);
        } catch (IOException ex) {
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void loadMap(String mapName){
        try{
            InputStream is = getClass().getResourceAsStream(mapName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()){
            
                String line = br.readLine();
                
                while(col < gp.getMaxWorldCol()){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.getMaxWorldCol()){
                    col = 0;
                    row++;
                }
            }
            
        }catch(IOException | NumberFormatException e){
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;
        
        while(worldCol < gp.getMaxWorldCol() && worldRow < gp.getMaxWorldRow()){
            int tileNum = mapTileNum[worldCol][worldRow];
            
            int worldX = worldCol * gp.getTileSize();
            int worldY = worldRow * gp.getTileSize();
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
            
            //Stop move the camera to prevent blank tiles
            if(gp.getPlayer().getScreenX() > gp.getPlayer().getWorldX()){
                screenX = worldX;
            }
            if(gp.getPlayer().getScreenY() > gp.getPlayer().getWorldY()){
                screenY = worldY;
            }
            int rightOffset = gp.getScreenWidth() - gp.getPlayer().getScreenX();
            if(rightOffset > gp.getWorldWidth() - gp.getPlayer().getWorldX()){
                screenX = gp.getScreenWidth() - (gp.getWorldWidth() - worldX);
            }
            int bottomOffset = gp.getScreenHeight() - gp.getPlayer().getScreenY();
            if(bottomOffset > gp.getWorldHeight() - gp.getPlayer().getWorldY()){
                screenY = gp.getScreenHeight() - (gp.getWorldHeight() - worldY);
            }
            
            //Draw tiles conditions
            if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
                worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
                worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            
            }else if(gp.getPlayer().getScreenX() > gp.getPlayer().getWorldX() || 
                gp.getPlayer().getScreenY() > gp.getPlayer().getWorldY() ||
                rightOffset > gp.getWorldWidth() - gp.getPlayer().getWorldX() ||
                bottomOffset > gp.getWorldHeight() - gp.getPlayer().getWorldY()){
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                
            }
            worldCol++;
            
            if(worldCol == gp.getMaxWorldCol()){
                worldCol = 0;
                worldRow++;
            }
        }
        
    }
    
}
