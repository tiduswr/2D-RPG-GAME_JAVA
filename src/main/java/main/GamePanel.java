package main;

import ui.UI;
import interfaces.Drawnable;
import event.EventHandler;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
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
    private final int worldWidth = tileSize * maxWorldCol;
    private final int worldHeight = tileSize * maxWorldRow;
    
    //FPS
    private final int FPS = 60;
    private int fpsCounter;
    
    //DEBUG   
    private Debug debug = new Debug(this);
    
    //INPUT
    private KeyHandler keyH = new KeyHandler(this);
    
    //Entity and Object Settings
    private Player player = new Player(this, keyH);
    private AssetSetter assetSetter = new AssetSetter(this);
    private final int MAX_NPC = 10, MAX_MONSTER = 20, MAX_OBJECTS = 10;
    private SuperObject obj[] = new SuperObject[MAX_OBJECTS]; //Pode ser mostrados até 10 objetos por vez no jogo
    private Entity[] npcs = new Entity[MAX_NPC];
    private Entity[] monsters = new Entity[MAX_MONSTER];
    private ArrayList<Drawnable> renderOrder = new ArrayList<>();
    
    //System Game
    private Thread gameThread;
    private Sound music = new Sound();
    private Sound soundEffect = new Sound();
    private CollisionChecker cChecker = new CollisionChecker(this);
    private TileManager tileM = new TileManager(this);
    private UI ui = new UI(this);
    private EventHandler eHandler = new EventHandler(this);
    
    //Game State
    public GameState gameState;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
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
        assetSetter.setNPCS();
        assetSetter.spawnMonsters();
        gameState = GameState.TITLE_STATE;
        playMusic("prelude");
        
        //Make renderOrder arraylist
        //Add player
        renderOrder.add(player);
        updateDrawArrayObjects();
        updateDrawArrayNpc();
        updateDrawArrayMonsters();
    }
    
    public void clearObjectArray(){
        obj = new SuperObject[MAX_OBJECTS];
    }
    public void updateDrawArrayObjects(){
        for(SuperObject o : obj){
            if(o != null){
                renderOrder.add(o);
            }
        }
    }
    
    public void clearMonstersArray(){
        monsters = new Entity[MAX_MONSTER];
    }
    public void updateDrawArrayMonsters(){
        for(Entity m : monsters){
            if(m != null){
                renderOrder.add(m);
            }
        }
    }
    
    public void clearNpcArray(){
       npcs = new Entity[MAX_NPC];
    }
    public void updateDrawArrayNpc(){
        for(Entity e : npcs){
            if(e != null){
                renderOrder.add(e);
            }
        }
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
                fpsCounter = drawCount;
                drawCount = 0;
                timer = 0;
            }
            
        }
    }
    
    public void update(){
        if(gameState == GameState.PLAY_STATE){
            //Player
            player.update();
            
            //NPC
            for(Entity e : npcs){
                if(e != null){
                    e.update();
                }
            }
            
            //MONSTERS
            for(Entity m : monsters){
                if(m != null){
                    m.update();
                }
            }
            
        }
        if(gameState == GameState.PAUSE_STATE){
            //Nothing
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Debug
        long drawStart = 0;
        if(getKeyH().debugMode()) drawStart = System.nanoTime();
        
        //Tile Screen
        if(gameState == GameState.TITLE_STATE){
            //UI
            ui.draw(g2);
        }else{
            //TILE
            tileM.draw(g2);
            
            //Draw Entities and Objects
            Collections.sort(renderOrder);
            renderOrder.forEach(d -> {
                d.draw(g2);
            });
            
            //Debug
            if(getKeyH().debugMode()){
                long drawEnd = System.nanoTime();
                debug.draw(g2, drawStart, drawEnd);
                eHandler.draw(g2);
            }
            
            //UI
            ui.draw(g2);
        }
        
        g2.dispose();
    }
    
    //Sound Handlers
    public void playMusic(String file){
        music.setFile(file);
        music.play();
        music.loop();
    }
    
    public void stopMusic(){
        music.stop();
    }
    
    public void playSoundEffect(String file, float vol){
        soundEffect.setFile(file);
        soundEffect.volume(vol);
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
    public int getFpsCounter() {
        return fpsCounter;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Entity[] getNpcs() {
        return npcs;
    }

    public EventHandler geteHandler() {
        return eHandler;
    }

    public void seteHandler(EventHandler eHandler) {
        this.eHandler = eHandler;
    }

    public Entity[] getMonsters() {
        return monsters;
    }

    public boolean removeMonster(int position){
        if(position != -1 && position <= getMonsters().length){
            renderOrder.remove(getMonsters()[position]);
            getMonsters()[position] = null;
            return true;
        }
        return false;
    }
    
    public boolean removeMonster(Entity monster){
        for(int i = 0; i < monsters.length; i++){
            if(monster == monsters[i]){
                renderOrder.remove(monsters[i]);
                monsters[i] = null;
                return true;
            }
        }
        return false;
    }
}
