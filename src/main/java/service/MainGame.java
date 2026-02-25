package service;

import graphicsUtilities.Scene;
import misc.Player;
import misc.TestInputInRUn;

import javax.swing.*;
import java.awt.*;

public class MainGame extends JFrame {

    private static RunService runService;
    private Scene currentScene;

    //Entry Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGame mainGame = new MainGame();
            mainGame.setVisible(true);
        });
    }

    public MainGame() {
        // Essential window settings
        setTitle("Dice Legend");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //thanks, AI, for Fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            this.setUndecorated(true);
            gd.setFullScreenWindow(this);
        }
        startRunService();
        startDefaultScene();
    }


    public void startRunService() {
        runService = RunService.GetService();
        runService.setMainGameFrame(this);
        runService.start();
        TestInputInRUn test = new TestInputInRUn();
    }

    public void startDefaultScene() {
        Scene gameScene1 = new Scene();
        gameScene1.spawnObjectAt(new Player("licoCake144"), 300,300);
        this.add(gameScene1);
    }

    public Scene getDrawingContext() {
        return this.currentScene;
    }
}