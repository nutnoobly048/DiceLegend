package service;

import Gameplay.GameState;
import Gameplay.SceneList;

import graphicsUtilities.Scene;
import graphicsUtilities.*;
import misc.Player;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class MainGame extends JFrame {

    private static RunService runService;
    private JPanel container;
    private static Scene currentScene;

    private static GameState currentGame;

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

        mockPlayer();
        startRunService();
        startContainerPanel();

        SceneUtilities.changeSceneTo(SceneList.mainMenu);

//        if (gd.isFullScreenSupported()) {
//            this.setUndecorated(true);
//            gd.setFullScreenWindow(this);
//        }


    }

    public void startRunService() {
        runService = RunService.GetService();
        runService.setMainGameFrame(this);
        runService.start();
    }

    public void startContainerPanel() {
        this.setSize(800,800);
        this.container = new JPanel();
        this.add(container);
    }


    public void mockPlayer() {
        String myLocalId = UUID.randomUUID().toString();
        Player.setLocalPlayerId(myLocalId);


        //MOCK
        currentGame = new GameState(true);
        RunService.intentQueue.add("INTENT:" + Player.getLocalPlayerId() + ":JOIN_GAME:" + "LICOWELLSPRING");
        RunService.intentQueue.add("INTENT:" + "TESTER" + ":JOIN_GAME:" + ".getLocalPlayerId()");
    }

    public Scene getCurrentScene() {
        return (Scene) this.container.getComponent(0);
    }

    public JPanel getContainer() {
        return this.container;
    }

    public static GameState getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(GameState currentGame) {
        MainGame.currentGame = currentGame;
    }
}