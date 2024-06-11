package assets;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    public Sound(){}

    public static void enemyHit(){
//        try{
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/assets/audios/hit.wav").getAbsoluteFile());
//            Clip hit = AudioSystem.getClip();
//            hit.open(audioInputStream);
//            hit.start();
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
        loadSound("src/assets/audios/hit.wav");
    }

    public static void explosion(){
        loadSound("src/assets/audios/explosion.wav");
    }

    public static void spiritDeath(){
        loadSound("src/assets/audios/spirit_death.wav");
    }

    private static void loadSound(String s){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
