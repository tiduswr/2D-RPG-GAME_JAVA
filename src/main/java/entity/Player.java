package entity;

import entity.damage.Damage;
import animation.TimedAnimation;
import entity.damage.DamageAction;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import interfaces.Action;
import object.OBJ_Equipment;
import object.SuperObject;
import object.OBJ_GreatSword;
import object.OBJ_WoodShield;
import ui.Message;

public class Player extends Entity{
    
    private KeyHandler keyH;
    private int qtdKeys = 0;
    private final int screenX, screenY;
    private boolean atkCanceled = false;
    
    //Equips
    private OBJ_Equipment curWeapon, curShield;
    
    public Player(GamePanel gp, KeyHandler keyH){
        super(gp, EntityType.PLAYER);
        //Configurations
        this.keyH = keyH;
        this.screenX = gp.getScreenWidth()/2 - (gp.getTileSize()/2);
        this.screenY = gp.getScreenHeight()/2 - (gp.getTileSize()/2);
        
        //Basic stats
        stats.name = "Sam";
        stats.speed = 240/gp.getFPS();
        
        //SetDefault Values
        setDefaultValues();
        getDefaultImages();
        
        //Configure lifebar
        lifeBar = new LifeBar(gp, 56, this, 0, 0);
        lifeBar.setDisplay(true);
    }
    
    private void setDefaultValues(){
        worldX = gp.getTileSize() * 23;
        worldY = gp.getTileSize() * 21;
        direction = Direction.DOWN;
        
        //Stats
        stats.maxLife = 6;
        stats.life = stats.maxLife;
        stats.str = 1;
        stats.dex = 1;
        stats.exp = 0;
        stats.nextLvlExp = 5;
        stats.gil = 0;
        
        //Default Weapons
        OBJ_GreatSword defaultSword = new OBJ_GreatSword(gp);
        TimedAnimation[] arr = defaultSword.greatSwordSprites(attackArea);
        atkUp = arr[0];
        atkDown = arr[1];
        atkLeft = arr[2];
        atkRight = arr[3];
        
        curWeapon = defaultSword;
        curShield = new OBJ_WoodShield(gp);
        
        //Calculate attack and defense
        stats.calculateAtk(curWeapon);
        stats.calculateDef(curShield);
    }
    
    private void getDefaultImages(){
        //Animations
        up = new TimedAnimation(new BufferedImage[]{makeSprite("player/u1.png"),
                                                    makeSprite("player/u2.png")}, 12/(60/gp.getFPS()), 20);
        down = new TimedAnimation(new BufferedImage[]{makeSprite("player/d1.png"),
                                                      makeSprite("player/d2.png")}, 12/(60/gp.getFPS()), 20);
        left = new TimedAnimation(new BufferedImage[]{makeSprite("player/l1.png"),
                                                      makeSprite("player/l2.png")}, 12/(60/gp.getFPS()), 20);
        right = new TimedAnimation(new BufferedImage[]{makeSprite("player/r1.png"),
                                                       makeSprite("player/r2.png")}, 12/(60/gp.getFPS()), 20);
    }
    
    
    
    @Override
    public void update(){
        if(attacking){
            TimedAnimation atk = null;
            switch(direction){
                case UP: atk = atkUp; break;
                case DOWN: atk = atkDown; break;
                case LEFT: atk = atkLeft; break;
                case RIGHT: atk = atkRight; break;
            }
            if(atk != null) playerAttack(atk);
        }else if(keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed() || 
                keyH.iszPressed()){            
            
            //Get user input
            if(keyH.isUpPressed()){
                direction = Direction.UP;
            }else if(keyH.isDownPressed()){
                direction = Direction.DOWN;
            }else if(keyH.isLeftPressed()){
                direction = Direction.LEFT;
            }else if(keyH.isRightPressed()){
                direction = Direction.RIGHT;
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
            up.updateSprite();
            down.updateSprite();
            left.updateSprite();
            right.updateSprite();
            
            if(gp.getKeyH().iszPressed() && !atkCanceled){
                gp.playSoundEffect("noHitWeapon", 0.6f);
                attacking = true;
            }
            atkCanceled = false;
        }else{
            up.resetAnimation();
            down.resetAnimation();
            left.resetAnimation();
            right.resetAnimation();
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
    
    @Override
    //Teste functions
    public void doDamage(DamageAction value){
        //Basic validations of every Entity
        if(!isInvincible()){
            setInvincible(true);
            if(stats.life > 0 && value.calculateDamage() < stats.life){
                stats.life -= value.calculateDamage();
            }else{
                stats.life = 0;
            }
        }
    }
    
    private void interactNpcIndex(int i){
        if(gp.getKeyH().iszPressed()){
            if(i != -1){
                atkCanceled = true;
                gp.setGameState(GameState.DIALOG_STATE);
                gp.getNpcs()[i].speak();
            }
        }
    }
    
    private void interactMonsterIndex(int monsterIndex) {
        if(monsterIndex != -1){
            if(!invincible && !gp.getMonsters()[monsterIndex].isDying()){
                doDamage(new Damage(gp.getMonsters()[monsterIndex], this));
                gp.playSoundEffect("receiveDamage", 0.4f);
                invincible = true;
            }
        }
    }
    
    private void damageMonster(int i) {
        if(i != -1){
            if(!gp.getMonsters()[i].isInvincible()) {
                
                gp.playSoundEffect("doDamage", 0.4f);
                gp.getMonsters()[i].doDamage(new Damage(this, gp.getMonsters()[i]));
                
                if(gp.getMonsters()[i].isDying()){
                    gp.getGameUI().getScrollMsg().addMessage(
                        new Message("You earned " + gp.getMonsters()[i].getStats().getExp() + " exp."));
                    if(receiveExp(gp.getMonsters()[i].getStats().getExp())){
                        //if true then Level UP!
                        gp.playSoundEffect("fanfarreSlim", 0.6f);
                        gp.getGameUI().setCurrentDialog("You leveled up!\nThe light of the crystal bless you.");
                        gp.setGameState(GameState.DIALOG_STATE);
                    }
                }
            }
        }
    }
    
    private boolean receiveExp(int exp){
        stats.exp += exp;
        if(stats.exp >= stats.nextLvlExp){
            stats.nextLvlExp += (int)(stats.nextLvlExp*(1.20));
            stats.level++;
            stats.maxLife++;
            stats.str++;
            stats.dex++;
            stats.calculateAtk(curWeapon);
            stats.calculateDef(curShield);
            return true;
        }
        return false;
    }
    
    private void playerAttack(TimedAnimation atk) {
        atk.updateSprite();
        if(atk.getCurentSpriteIndex() == 1 && atk.getSpriteCounter() < atk.getFRAMES()){
            //Check Collision
            int curWorldX = worldX;
            int curWorldY = worldY;
            int curSoldAreaWidth = solidArea.width;
            int curSoldAreaHeight = solidArea.height;
            
            //Move solid area to check collision temporary
            switch(direction){
                case UP: worldY -= attackArea.height; break;
                case DOWN: worldY += attackArea.height; break;
                case LEFT: worldX -= attackArea.width; break;
                case RIGHT: worldX += attackArea.width; break;
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
            
        }else if(atk.getCurentSpriteIndex() == 1 && atk.getSpriteCounter() == atk.getFRAMES()){
            attacking = false;
            atk.forceReset();
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
                case UP:
                    worldY -= stats.speed;
                    break;
                case DOWN:
                   worldY += stats.speed;
                   break;
                case LEFT:
                    worldX -= stats.speed;
                    break;
                case RIGHT:
                    worldX += stats.speed;
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
                case UP:
                    tempScreenY -= gp.getTileSize();
                    break;
                case LEFT:
                    tempScreenX -= gp.getTileSize();
                    break;
            } 
        }
        
        //Draw player
        //Transparence if Invincible
        if(invincible && invincibleCounter%20 == 0){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
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
                    case UP: sy -= attackArea.height; break;
                    case DOWN: sy += attackArea.height; break;
                    case LEFT: sx -= attackArea.width; break;
                    case RIGHT: sx += attackArea.width; break;
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
        stats.life = stats.maxLife;
    }
    public boolean isAtkCanceled() {
        return atkCanceled;
    }
    public void setAtkCanceled(boolean atkCanceled) {
        this.atkCanceled = atkCanceled;
    }

    public OBJ_Equipment getCurWeapon() {
        return curWeapon;
    }

    public void setCurWeapon(OBJ_Equipment curWeapon) {
        this.curWeapon = curWeapon;
    }

    public OBJ_Equipment getCurShield() {
        return curShield;
    }

    public void setCurShield(OBJ_Equipment curShield) {
        this.curShield = curShield;
    }
}
