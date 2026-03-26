package service;


import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {

    private Clip clip;
    private boolean isLooping = false;

    public AudioPlayer(byte[] audioData) {
        try {
            InputStream bais = new ByteArrayInputStream(audioData);
            InputStream bufferedIn = new BufferedInputStream(bais);

            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(ais);

            System.out.println("[AudioPlayer] Clip successfully initialized from RAM.");

        } catch (Exception e) {
            System.err.println("[AudioPlayer] CRITICAL: Still cannot read these bytes.");
            e.printStackTrace();
        }
    }

    public AudioPlayer setVolume(float volume) {
        if (clip == null) return this;

        if (volume < 0f) volume = 0f;
        if (volume > 1f) volume = 1f;

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float dB = (float) (Math.log10(volume) * 20.0 - 10);

            if (volume <= 0.0001f) dB = -80.0f;

            gainControl.setValue(dB);
        }
        return this;
    }

    public void play() {
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        isLooping = true;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) return;
        isLooping = false;
        clip.stop();
        clip.close();
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}