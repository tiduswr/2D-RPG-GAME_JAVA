package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import object.OBJ_Key;

public class UI {
    private GamePanel gp;
    private Font arial_40, arial_80B;
    private BufferedImage keyImage;
    
    //Messages
    private boolean messageOn = true;
    private String message = "";
    private int messageCounter = 0;
    private boolean gameFinished = false;
    
    //Timer
    private double playTime;
    private DecimalFormat dFormat = new DecimalFormat("#0.0");
    
    public UI(GamePanel gp){
        this.gp = gp;
        this.arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        keyImage = new OBJ_Key(gp).getImage();
    }
    
    public void showMessage(String text){
        message = text;
        messageOn = true;
    }
    
    public void draw(Graphics2D g2){
        
        //Desenha a tela de Finalização do Game com status de Venceu!
        if(gameFinished){
            String text;
            int textLength;
            int x, y;
            
            //Desenha a mensagem secundária
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            text = "You found the Treasure!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.getScreenWidth()/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize * 3);
            g2.drawString(text, x, y);
            
            //Desenha o tempo decorrido
            text = "Your time is " + dFormat.format(playTime) + "!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.getScreenWidth()/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 4);
            g2.drawString(text, x, y);
            
            //Desenha a mensagem Principal
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.getScreenWidth()/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 2);
            g2.drawString(text, x, y);
            
            gp.stopMusic();
            gp.playSoundEffect(1);
            gp.setGameThread(null);
            
        }else{
            //Desenha o contador de chaves na tela
            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(keyImage, gp.getTileSize()/2, gp.getTileSize()/2, gp.getTileSize(), gp.getTileSize(), null);
            g2.drawString("x " + gp.getPlayer().getQtdKeys(), 74, 65);
            
            //Draw game time
            playTime += (double)1/gp.getFPS();
            g2.drawString("Time: " + dFormat.format(playTime), gp.getTileSize()*11, 65);
            
            //Faz mensagens de log aparecer
            if(messageOn == true){
                g2.setFont(g2.getFont().deriveFont(20F));
                g2.drawString(message, gp.getTileSize()/2, gp.getTileSize()*5);

                //As mensagens vão desaparecer após 120 frames
                messageCounter++;
                if(messageCounter > 120){
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }

    public boolean isGameFinished() {
        return gameFinished;
    }
    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
    public Font getFontArial40(){
        return this.arial_40;
    }
}
