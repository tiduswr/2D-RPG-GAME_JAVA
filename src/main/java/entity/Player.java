package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
        greatSwordSprites();
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
    
    private void greatSwordSprites(){
        atkU1 = makeSprite("player/atk_u1.png", gp.getTileSize(), gp.getTileSize()*2);
        atkU2 = makeSprite("player/atk_u2.png", gp.getTileSize(), gp.getTileSize()*2);
        atkL1 = makeSprite("player/atk_l1.png", gp.getTileSize()*2, gp.getTileSize());
        atkL2 = makeSprite("player/atk_l2.png", gp.getTileSize()*2, gp.getTileSize());
        atkR1 = makeSprite("player/atk_r1.png", gp.getTileSize()*2, gp.getTileSize());
        atkR2 = makeSprite("player/atk_r2.png", gp.getTileSize()*2, gp.getTileSize());
        atkD1 = makeSprite("player/atk_d1.png", gp.getTileSize(), gp.getTileSize()*2);
        atkD2 = makeSprite("player/atk_d2.png", gp.getTileSize(), gp.getTileSize()*2);
        attackArea.width = 36;
        attackArea.height = 36;
    }
    
    @Override
    public void update(){
        if(attacking){
            playerAttack();
        }else if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed() || 
                keyH.iszPressed()){            
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
            
            //Check NPC Collision
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
        if(gp.getKeyH().iszPressed()){
            if(i != -1){
                gp.setGameState(GameState.DIALOG_STATE);
                gp.getNpcs()[i].speak();
            }else{
                gp.getPlayer().setAttacking(true);
            }
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
    
    private void damageMonster(int i) {
        if(i != -1){
            //Monster damage example
            if(!gp.getMonsters()[i].isInvincible()){
                gp.getMonsters()[i].doDamage(1);
                gp.getMonsters()[i].setInvincible(true);
                if(gp.getMonsters()[i].getLife() <= 0){
                    gp.removeMonster(i);
                }
            }
        }
    }
    
    private void playerAttack() {
        spriteCounter++;
        if(spriteCounter <= 15){
            spriteNum = 1;
        }else if(spriteCounter > 15 && spriteCounter <= 30){
            spriteNum = 2;
            
            //Check Collision
            int curWorldX = worldX;
            int curWorldY = worldY;
            int curSoldAreaWidth = solidArea.width;
            int curSoldAreaHeight = solidArea.height;
            
            //Move solid area to check collision temporary
            switch(direction){
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
                    
            //Check collision
            int monsterIndex = gp.getcChecker().checkEntity(this, gp.getMonsters());
            damageMonster(monsterIndex);
            
            //Reset values
            worldX = curWorldX;
            worldY = curWorldY;
            solidArea.width = curSoldAreaWidth;
            solidArea.height = curSoldAreaHeight;
            
        }else if(spriteCounter >= 45){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
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
    protected void checkDirection(){
        //Only move if collisionOn is true
        if(!collisionOn && !gp.getKeyH().iszPressed()){
            switch(direction){
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                   worldY += speed;
                   break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
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
        
        //Adjust attack Animation left and up
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        if(attacking){
            switch(direction){
                case "up":
                    tempScreenY -= gp.getTileSize();
                    break;
                case "left":
                    tempScreenX -= gp.getTileSize();
                    break;
            } 
        }
        
        //Draw player
        //Transparence if Invincible
        if(invincible && invincibleCounter%20 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        //Draw collision
        if (gp.getKeyH().debugMode()){
            drawCollision(g2, x, y);
            //Draw hitbox
            if(attacking) {
                int  sx = screenX, sy = screenY;
                switch(direction){
                    case "up": sy -= attackArea.height; break;
                    case "down": sy += attackArea.height; break;
                    case "left": sx -= attackArea.width; break;
                    case "right": sx += attackArea.width; break;
                }
                drawAttackCollision(g2, sx, sy);
            }
        }
    }
    
    protected void drawAttackCollision(Graphics2D g2, int screenX, int screenY){
        //Desenha a colisão
        Stroke oldStroke = g2.getStroke();
        Font oldFont = g2.getFont();
        g2.setStroke(collisionRectStroke);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, attackArea.width, attackArea.height);
        g2.setStroke(oldStroke);
        g2.setFont(oldFont);
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
    
    public void resetLife(){
        this.life = maxLife;
    }
    
}
