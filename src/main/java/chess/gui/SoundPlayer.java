package chess.gui;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    public static void playSound(String filename) {
        try {
            File soundFile = new File("src/sounds/" + filename);
            if (!soundFile.exists()) {
                System.out.println("Sound not found: " + filename);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.out.println("Error playing sound: " + filename);
            e.printStackTrace();
        }
    }
}
