package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public final class TileManager {
    protected GamePanel gp;
    protected Tile[] tile;
    protected int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        getTileImage();
        loadMap("/maps/world01.txt");
    }
    
    public int getMapTileNum(int row, int col){
        return mapTileNum[row][col];
    }
    
    public Tile getTile(int tileNum){
        return tile[tileNum];
    }
    
    public void getTileImage(){
        makeTile(0, "grass.png", false);
        makeTile(1, "wall.png", true);
        makeTile(2, "water.png", true);
        makeTile(3, "earth.png", false);
        makeTile(4, "tree.png", true);
        makeTile(5, "sand.png", false);
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
            
            if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
                worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
                worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){
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
