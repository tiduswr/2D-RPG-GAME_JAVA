package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private Clip clip;
    private URL soundURL[] = new URL[30];
    
    public Sound(){
        soundURL[0] = getClass().getResource("/sound/worldTheme.wav");
        soundURL[1] = getClass().getResource("/sound/fanfarreSlim.wav");
        soundURL[2] = getClass().getResource("/sound/coin.wav");
        soundURL[3] = getClass().getResource("/sound/cure.wav");
        soundURL[4] = getClass().getResource("/sound/chestUnlock.wav");
        soundURL[5] = getClass().getResource("/sound/doorUnlock.wav");
        soundURL[6] = getClass().getResource("/sound/keyGet.wav");
        soundURL[7] = getClass().getResource("/sound/speedUp.wav");
        soundURL[8] = getClass().getResource("/sound/teleport.wav");
    }
    
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e){
            
        }
    }
    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
