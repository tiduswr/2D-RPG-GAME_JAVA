package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;

public final class GamePanel extends JPanel implements Runnable{
    //Screen Settings
    final int originalTileSize = 16; //16x16 Size of Player Character, NPCS, Enemys, Itens...
    final int scale = 3;
    
    //Window Settings
    final int tileSize = originalTileSize * scale; // Scale 16x16 to 48x48
    final int maxScreenCol = 16; //Horizontal Size
    final int maxScreenRow = 12; //Vertical Size
    final int screenWidth = tileSize * maxScreenCol; //768 Pixels
    final int screenHeight = tileSize * maxScreenRow;// 576 Pixels
        
    //World Settings
    private final int maxWorldCol = 50;
    private final int maxWorldRow = 50;
    
    //FPS
    private final int FPS = 60;
    
    //System Game
    private Thread gameThread;
    private Sound music = new Sound();
    private Sound soundEffect = new Sound();
    private KeyHandler keyH = new KeyHandler();
    private CollisionChecker cChecker = new CollisionChecker(this);
    private TileManager tileM = new TileManager(this);
    private UI ui = new UI(this);
    private boolean showCollision = true;
    
    //Player Sttings
    private Player player = new Player(this, keyH);
    private AssetSetter assetSetter = new AssetSetter(this);
    private SuperObject obj[] = new SuperObject[10]; //Pode ser mostrados até 10 objetos por vez no jogo
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        setupGame();
    }
    
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void setupGame(){
        assetSetter.setObject();
        playMusic(0);
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
        
        //TILE
        tileM.draw(g2);
        
        //OBJECT
        for(SuperObject o : obj){
            if(o != null){
                o.draw(g2);
            }
        }
        
        //PLAYER
        player.draw(g2);
        
        //UI
        ui.draw(g2);
        
        g2.dispose();
    }
    
    //Sound Handlers
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    
    public void stopMusic(){
        music.stop();
    }
    
    public void playSoundEffect(int i){
        soundEffect.setFile(i);
        soundEffect.play();
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
    public CollisionChecker getcChecker() {
        return cChecker;
    }
    public TileManager getTileM() {
        return tileM;
    }
    public AssetSetter getAssetSetter() {
        return assetSetter;
    }
    public SuperObject[] getObj() {
        return obj;
    }
    public UI getGameUI(){
        return this.ui;
    }
    public Thread getGameThread() {
        return gameThread;
    }
    public Thread setGameThread(Thread value) {
        return gameThread = value;
    }
    public boolean showCollision(){
        return showCollision;
    }
}
