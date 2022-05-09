package main;

import object.SuperObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    
    private boolean upPressed, downPressed, leftPressed, rightPressed, shotKeyPressed;
    private GamePanel gp;
    //Debug
    private boolean debug = false;
    //Interaction
    private boolean zPressed;
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(gp.getGameState() == GameState.PLAY_STATE){
            playState(code);
        }else if(gp.getGameState() == GameState.PAUSE_STATE){
            //Pause
            if(code == KeyEvent.VK_P){
                if(gp.getGameUI().isStopMusic()){
                    gp.getGameUI().setStopMusic(false);
                    gp.resumeMusic();
                }
                gp.setGameState(GameState.PLAY_STATE);
            }
        }else if(gp.getGameState() == GameState.DIALOG_STATE){
            if(code == KeyEvent.VK_Z){
                if(gp.getGameUI().isStopMusic()){
                    gp.getGameUI().setStopMusic(false);
                    gp.resumeMusic();
                }
                gp.setGameState(GameState.PLAY_STATE);
            }
        }else if(gp.getGameState() == GameState.STATUS_WINDOW_STATE){
            if(code == KeyEvent.VK_C){
                gp.setGameState(GameState.PLAY_STATE);
            }
            controlInventory(code);
        }else if(gp.getGameState() == GameState.TITLE_STATE){
            titleState(code);
        }
    }
    
    public void titleState(int code){
        if(code == KeyEvent.VK_UP){
            gp.getGameUI().getTitleScreen().upTitleCommand();
        }
        if(code == KeyEvent.VK_DOWN){
            gp.getGameUI().getTitleScreen().downTitleCommand();
        }
        if(code == KeyEvent.VK_ENTER){
            gp.getGameUI().getTitleScreen().executeMenuAction();
        }
    }
    
    public void playState(int code){
        if(code == KeyEvent.VK_UP){
            upPressed = true;
        }
        if(code == KeyEvent.VK_DOWN){
            downPressed = true;
        }
        if(code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_Z){
            zPressed = true;
        }
        if(code == KeyEvent.VK_C){
            gp.setGameState(GameState.STATUS_WINDOW_STATE);
        }
        if(code == KeyEvent.VK_Q){
            shotKeyPressed = true;
        }
        //Debug
        if(code == KeyEvent.VK_F1){
            debug = !debug;
        }
        //Pause
        if(code == KeyEvent.VK_P){
            gp.setGameState(GameState.PAUSE_STATE);
            gp.stopMusic();
            gp.getGameUI().setStopMusic(true);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_UP){
            upPressed = false;
        }
        if(code == KeyEvent.VK_DOWN){
            downPressed = false;
        }
        if(code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_Z){
            zPressed = false;
        }
        if(code == KeyEvent.VK_Q){
            shotKeyPressed = false;
        }
    }
    
    public boolean debugMode(){
        return debug;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isShotKeyPressed() {
        return shotKeyPressed;
    }

    public boolean iszPressed() {
        return zPressed;
    }
    public void setzPressed(boolean zPressed) {
        this.zPressed = zPressed;
    }

    private void controlInventory(int code) {
        if(code == KeyEvent.VK_UP){
            gp.getGameUI().getInventory().cursorUp();
        }
        if(code == KeyEvent.VK_DOWN){
            gp.getGameUI().getInventory().cursorDown();
        }
        if(code == KeyEvent.VK_LEFT){
            gp.getGameUI().getInventory().cursorLeft();
        }
        if(code == KeyEvent.VK_RIGHT){
            gp.getGameUI().getInventory().cursorRight();
        }
        if(code == KeyEvent.VK_ENTER){
            SuperObject o = gp.getGameUI().getInventory().getSelectedItem();
            if(o != null && o.executeInventoryAction()) gp.getPlayer().removeItem(o);
        }
    }
}
