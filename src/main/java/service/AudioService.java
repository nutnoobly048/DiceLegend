package service;


import misc.Player;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

public class AudioService {

    private static AudioService instance;
    private static float masterVolume = 0.2f;


    public static AudioService getInstance() {
        if (instance == null) instance = new AudioService();
        return instance;
    }

    private AudioService() {}


    private static final HashMap<String, byte[]> preloadedAudio = new HashMap<>();
    private AudioPlayer currentMusic;

    public static void preloadAllClips() { preloadAllClips(null); }

    public static void preloadAllClips(URI folderURI) {
        URL resourceURL = AudioService.class.getClassLoader().getResource(".");

        if (resourceURL == null) {
            System.out.println("[AudioService] Cannot find resource path");
            return;
        }

        if (folderURI == null) {
            try {
                File folder = new File(resourceURL.toURI());
                scanFolder(folder);
            } catch (URISyntaxException e) {
                System.err.println(e);
            }
        } else {
            File folder = new File(folderURI);
            scanFolder(folder);
        }
    }

    private static void scanFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File element : files) {
            if (element.isFile() && isAudio(element.getName())) {
                loadAudioBytes(element);
            } else if (element.isDirectory()) {
                System.out.println("[AudioService] Digging into folder: " + element.getName());
                scanFolder(element);
            }
        }
    }

    private static void loadAudioBytes(File file) {
        try {
            byte[] rawBytes = Files.readAllBytes(file.toPath());

            preloadedAudio.put(file.getName(), rawBytes);
            System.out.println("[AudioService] RAW Load Success: " + file.getName() + " (" + rawBytes.length + " bytes)");
        } catch (IOException e) {
            System.err.println("[AudioService] Failed to read file: " + file.getName());
            e.printStackTrace();
        }
    }



    private static boolean isAudio(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".wav");
    }

    public AudioPlayer playSFX(String fileName) {
        AudioPlayer player = createPlayer(fileName);
        if (player != null) player.play();
        return player;
    }


    public void playMusic(String fileName) {
        stopMusic();
        AudioPlayer player = createPlayer(fileName);
        player.setVolume(masterVolume);
        if (player != null) {
            player.loop();
            currentMusic = player;
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }



    private AudioPlayer createPlayer(String fileName) {
        byte[] audioData = preloadedAudio.get(fileName);
        if (audioData == null) {
            System.err.println("[AudioService] Audio not preloaded: " + fileName);
            return null;
        }

        return new AudioPlayer(audioData).setVolume(masterVolume);
    }

    public static float getMasterVolume() {
        return masterVolume;
    }

    public static void setMasterVolume(float masterVolume) {
        AudioService.masterVolume = masterVolume;
    }

    public AudioPlayer getCurrentMusic() {
        return currentMusic;
    }
}
