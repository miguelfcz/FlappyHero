package util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    private Clip clip; //Objeto do som que será tocado
    private URL soundURL;// Caminho do arquivo

    public AudioPlayer(String path) {
        try {
            soundURL = getClass().getResource(path); //permite usar o "new AudioPlayer("link da URL")
            if (soundURL == null) {
                System.err.println("Sound file not found: " + path);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);// LÊ o URL. prepara o clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Para informar erros
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }

}
