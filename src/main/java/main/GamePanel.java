package main;

import projectile.Projectile;
import ui.Debug;
import ui.UI;
import event.EventHandler;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;
import interfaces.Drawable;

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
    private ArrayList<SuperObject> obj = new ArrayList<>(); //Pode ser mostrados até 10 objetos por vez no jogo
    private ArrayList<Entity> npcs = new ArrayList<>();
    private ArrayList<Entity> monsters = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Drawable> renderOrder = new ArrayList<>();
    private Stack<Drawable> markedForDeath = new Stack<>();
    
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
            for(Entity cur : npcs){
                cur.update();
            }
            
            //MONSTERS
            for(Entity cur : monsters){
                cur.update();
            }

            //PROJECTILES
            for(Projectile cur : projectiles){
                cur.update();
            }

            //REMOVE ENTITIES MARKED FOR DEATH
            while(!markedForDeath.empty()){
                Object e = markedForDeath.pop();
                if(e != null) {
                    if(e instanceof Entity) {
                        npcs.remove((Entity) e);
                        monsters.remove((Entity) e);
                    }else if(e instanceof Projectile){
                        projectiles.remove((Projectile) e);
                    }
                    renderOrder.remove((Drawable) e);
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
    
    public void resumeMusic(){
        music.play();
        music.loop();
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

    public EventHandler geteHandler() {
        return eHandler;
    }

    public void seteHandler(EventHandler eHandler) {
        this.eHandler = eHandler;
    }

    public void addObject(SuperObject o){
        obj.add(o);
        renderOrder.add(o);
    }

    public void addNpc(Entity npc){
        npcs.add(npc);
        renderOrder.add(npc);
    }

    public void addMonster(Entity monster){
        monsters.add(monster);
        renderOrder.add(monster);
    }

    public void addProjectile(Projectile projectile){
        projectiles.add(projectile);
        renderOrder.add(projectile);
    }

    public void removeObject(SuperObject o){
        obj.remove(o);
        renderOrder.remove(o);
    }

    public void removeNpc(Entity npc){
        npcs.remove(npc);
        renderOrder.remove(npc);
    }

    public void removeMonster(Entity monster){
        monsters.remove(monster);
        renderOrder.remove(monster);
    }

    public void removeProjectile(Projectile projectile){
        projectiles.remove(projectile);
        renderOrder.remove(projectile);
    }

    public void removeFromRenderOrder(Drawable drawable){
        renderOrder.remove(drawable);
    }

    public ArrayList<SuperObject> getObj() {
        return obj;
    }

    public ArrayList<Entity> getNpcs() {
        return npcs;
    }

    public ArrayList<Entity> getMonsters() {
        return monsters;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void markDrawableForDispose(Drawable e){
        markedForDeath.push(e);
    }

}
