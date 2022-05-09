package main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    private Clip clip;
    private final HashMap<String, URL> soundURL;
    
    public Sound(){
        this.soundURL = new HashMap<>();
        
        this.soundURL.put("worldTheme", getClass().getResource("/sound/worldTheme.wav"));
        this.soundURL.put("fanfarreSlim", getClass().getResource("/sound/fanfarreSlim.wav"));
        this.soundURL.put("coin", getClass().getResource("/sound/coin.wav"));
        this.soundURL.put("cure", getClass().getResource("/sound/cure.wav"));
        this.soundURL.put("chestUnlock", getClass().getResource("/sound/chestUnlock.wav"));
        this.soundURL.put("doorUnlock", getClass().getResource("/sound/doorUnlock.wav"));
        this.soundURL.put("getItem", getClass().getResource("/sound/getItem.wav"));
        this.soundURL.put("speedUp", getClass().getResource("/sound/speedUp.wav"));
        this.soundURL.put("teleport", getClass().getResource("/sound/teleport.wav"));
        this.soundURL.put("prelude", getClass().getResource("/sound/prelude.wav"));
        this.soundURL.put("cursor", getClass().getResource("/sound/cursor.wav"));
        this.soundURL.put("selected", getClass().getResource("/sound/selected.wav"));
        this.soundURL.put("doDamage", getClass().getResource("/sound/doDamage.wav"));
        this.soundURL.put("receiveDamage", getClass().getResource("/sound/receiveDamage.wav"));
        this.soundURL.put("noHitWeapon", getClass().getResource("/sound/notHitWeapon.wav"));
        this.soundURL.put("fireball", getClass().getResource("/sound/fireball.wav"));
        this.soundURL.put("fireBallDamage", getClass().getResource("/sound/fireBallDamage.wav"));
        this.soundURL.put("spiting", getClass().getResource("/sound/spiting.wav"));
    }
    
    public void setFile(String file){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL.get(file));
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(IOException | LineUnavailableException | UnsupportedAudioFileException e){}
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
    
    public void volume(float volume){
        if (volume > 0f && volume < 1f){
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }
    
}
