package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import object.Action;

public class Player extends Entity{
    
    private KeyHandler keyH;
    private int qtdKeys = 0;
    private int standCounter = 0;
    private final int screenX, screenY;
    
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp, EntityType.PLAYER);
        this.keyH = keyH;
        
        this.screenX = gp.getScreenWidth()/2 - (gp.getTileSize()/2);
        this.screenY = gp.getScreenHeight()/2 - (gp.getTileSize()/2);
        name = "Sam";
        speed = 240/gp.getFPS();
                
        setDefaultValues();
        getImages();
    }
    
    private void setDefaultValues(){
        worldX = gp.getTileSize() * 23;
        worldY = gp.getTileSize() * 21;
        direction = "down";
        
        //Life
        maxLife = 6;
        life = maxLife;
    }
    
    private void getImages(){
        u1 = makeSprite("player/u1.png");
        u2 = makeSprite("player/u2.png");
        l1 = makeSprite("player/l1.png");
        l2 = makeSprite("player/l2.png");
        r1 = makeSprite("player/r1.png");
        r2 = makeSprite("player/r2.png");
        d1 = makeSprite("player/d1.png");
        d2 = makeSprite("player/d2.png");
    }
    
    @Override
    public void update(){
        if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()){            
            //Get user input
            if(keyH.isUpPressed()){
                direction = "up";
            }else if(keyH.isDownPressed()){
                direction = "down";
            }else if(keyH.isLeftPressed()){
                direction = "left";
            }else if(keyH.isRightPressed()){
                direction = "right";
            }
            
            checkTileCollision();            
            
            //Check object collision
            int objIndex = checkObjectCollision();
            pickUpObject(objIndex);
            
            //Chec kNPC Collision
            int npcIndex = gp.getcChecker().checkEntity(this, gp.getNpcs());
            interactNpcIndex(npcIndex);
            
            //CheckMonster Collision
            int monsterIndex = gp.getcChecker().checkEntity(this, gp.getMonsters());
            interactMonsterIndex(monsterIndex);
            
            //Check Event Collision
            gp.geteHandler().checkEvent();
            
            checkDirection();
            controlSpriteAnimationSpeed();
        }else{
            standCounter++;
            if(standCounter == 20){
                standCounter = 0;
                spriteNum = 1;
            }
        }
        
        //Invincible damage frame
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > gp.getFPS()){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    
    private void interactNpcIndex(int i){
        if(i != -1 && gp.getKeyH().iszPressed()){
            gp.setGameState(GameState.DIALOG_STATE);
            gp.getNpcs()[i].speak();
        }
    }
    
    private void interactMonsterIndex(int monsterIndex) {
        if(monsterIndex != -1){
            if(!invincible){
                doDamage(1);
                invincible = true;
            }
        }
    }
    
    @Override
    protected int checkObjectCollision(){
        return gp.getcChecker().checkObject(this, true);
    }
    
    public void pickUpObject(int i){
        if(i != -1){
            Action a = gp.getObj()[i];
            if(a.executeAction()) gp.getObj()[i] = null;
        }
    }
    
    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = getSpriteDirection();
        
        //Check location of player to draw a tile
        //Implemented to dont show blank tiles of map
        int x = screenX;
        int y = screenY;
        if(screenX > worldX){
            x = worldX;
        }
        if(screenY > worldY){
            y = worldY;
        }
        int rightOffset = gp.getScreenWidth() - getScreenX();
        if(rightOffset > gp.getWorldWidth() - getWorldX()){
            x = gp.getScreenWidth() - (gp.getWorldWidth() - worldX);
        }
        int bottomOffset = gp.getScreenHeight() - getScreenY();
        if(bottomOffset > gp.getWorldHeight() - getWorldY()){
            y = gp.getScreenHeight() - (gp.getWorldHeight() - worldY);
        }
        
        //Draw player
        //Transparence if Invincible
        if(invincible && invincibleCounter%20 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        }
        g2.drawImage(image, x, y, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        //Draw collision
        if (gp.getKeyH().debugMode()) drawCollision(g2, x, y);
    }
    
    public int getScreenX() {
        return screenX;
    }
    public int getScreenY() {
        return screenY;
    }
    public int getQtdKeys() {
        return qtdKeys;
    }
    public void addKeys(int value){
        qtdKeys += value;
    }
    public void removeKeys(int value){
        qtdKeys -= value;
    }
    
    
    
    //Teste functions
    public void doDamage(int value){
        if(life > 0) life -= value;
    }
    
    public void resetLife(){
        this.life = maxLife;
    }
    
}
