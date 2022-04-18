package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private GamePanel gp;
    //Debug
    private boolean debug = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        
        //Pause
        if(code == KeyEvent.VK_P){
            if(gp.getGameState() == GameState.PLAY_STATE){
                gp.setGameState(GameState.PAUSE_STATE);
            }else if(gp.getGameState() == GameState.PAUSE_STATE){
                gp.setGameState(GameState.PLAY_STATE);
            }
        }
        
        //Debug
        if(code == KeyEvent.VK_F1){
            debug = debug == false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
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
    
}
