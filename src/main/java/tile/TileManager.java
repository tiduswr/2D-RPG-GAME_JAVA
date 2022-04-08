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

public final class TileManager {
    protected GamePanel gp;
    protected Tile[] tile;
    protected int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.getMaxScreenCol()][gp.getMaxScreenRow()];
        getTileImage();
        loadMap("/maps/map01.txt");
    }
    
    public void getTileImage(){
        tile[0] = new Tile();
        try {
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
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
            
            while(col < gp.getMaxScreenCol() && row < gp.getMaxScreenRow()){
            
                String line = br.readLine();
                
                while(col < gp.getMaxScreenCol()){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.getMaxScreenCol()){
                    col = 0;
                    row++;
                }
            }
            
        }catch(IOException | NumberFormatException e){
            Logger.getLogger(TileManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void draw(Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        
        while(col < gp.getMaxScreenCol() && row < gp.getMaxScreenRow()){
            int tileNum = mapTileNum[col][row];
            
            g2.drawImage(tile[tileNum].image, x, y, gp.getTileSize(), gp.getTileSize(), null);
            col++;
            x += gp.getTileSize();
            
            if(col == gp.getMaxScreenCol()){
                col = 0;
                x = 0;
                row++;
                y += gp.getTileSize();
            }
        }
        
    }
    
}
