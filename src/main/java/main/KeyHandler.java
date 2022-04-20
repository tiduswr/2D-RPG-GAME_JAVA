package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    
    private boolean upPressed, downPressed, leftPressed, rightPressed;
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
            //Debug
            if(code == KeyEvent.VK_F1){
                debug = debug == false;
            }
            //Pause
            if(code == KeyEvent.VK_P){
                gp.setGameState(GameState.PAUSE_STATE);
            }
        }else if(gp.getGameState() == GameState.PAUSE_STATE){
            //Pause
            if(code == KeyEvent.VK_P){
                gp.setGameState(GameState.PLAY_STATE);
            }
        }else if(gp.getGameState() == GameState.DIALOG_STATE){
            if(code == KeyEvent.VK_Z){
                gp.setGameState(GameState.PLAY_STATE);
            }
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
    public boolean iszPressed() {
        return zPressed;
    }
    public void setzPressed(boolean zPressed) {
        this.zPressed = zPressed;
    }
}
