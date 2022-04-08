package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tile.TileManager;

public final class GamePanel extends JPanel implements Runnable{
    //Screen Settings
    final int originalTileSize = 16; //16x16 Size of Player Character, NPCS, Enemys, Itens...
    final int scale = 3;
    
    final int tileSize = originalTileSize * scale; // Scale 16x16 to 48x48
    final int maxScreenCol = 16; //Horizontal Size
    final int maxScreenRow = 12; //Vertical Size
    final int screenWidth = tileSize * maxScreenCol; //768 Pixels
    final int screenHeight = tileSize * maxScreenRow;// 576 Pixels
    
    private KeyHandler keyH = new KeyHandler();
    private Thread gameThread;
    private Player player = new Player(this, keyH);
    private TileManager tileM = new TileManager(this);
    
    //World Settings
    private final int maxWorldCol = 50;
    private final int maxWorldRow = 50;
    private final int worldWidth = tileSize * maxWorldCol;
    private final int worldHeight = tileSize * maxWorldRow;
    
    //FPS
    private final int FPS = 60;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        
        //Game Loop Delta Strategy
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null){            
            
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            
            if(delta >= 1){
                update();
                repaint();
                delta --;
                drawCount++;
            }
            
            if(timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
            
        }
    }
    
    public void update(){
        player.update();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }
    
    //Getters
    public int getOriginalTileSize() {
        return originalTileSize;
    }
    public int getScale() {
        return scale;
    }
    public int getTileSize() {
        return tileSize;
    }
    public int getMaxScreenCol() {
        return maxScreenCol;
    }
    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
    public KeyHandler getKeyH() {
        return keyH;
    }
    public Player getPlayer() {
        return player;
    }
    public int getFPS() {
        return FPS;
    }
    public int getMaxWorldCol() {
        return maxWorldCol;
    }
    public int getMaxWorldRow() {
        return maxWorldRow;
    }
    public int getWorldWidth() {
        return worldWidth;
    }
    public int getWorldHeight() {
        return worldHeight;
    }
}
