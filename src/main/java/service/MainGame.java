package service;

import Gameplay.GameState;
import Gameplay.SceneList;

import graphicsUtilities.Scene;
import graphicsUtilities.*;
import misc.Player;

import javax.swing.*;
import java.awt.*;

public class MainGame extends JFrame {


    private static RunService runService;
    private JPanel container;
    private static Scene currentScene = null;


    //Entry Main
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        SwingUtilities.invokeLater(() -> {
            MainGame mainGame = new MainGame();
            mainGame.setVisible(true);
        });
    }

    public MainGame() {
        setTitle("Dice Legend");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(new UserInput());
        //thanks, AI, for Fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        startRunService();
        startContainerPanel();


        SceneUtilities.changeSceneTo(SceneList.mainMenu);

        if (gd.isFullScreenSupported()) {
            this.setUndecorated(true);
            gd.setFullScreenWindow(this);
        }
    }

    public void startRunService() {
        runService = RunService.GetService();
        runService.setMainGameFrame(this);
        runService.start();
    }

    public void startContainerPanel() {
        this.setSize(800,800);
        this.container = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        this.add(container);
    }


    public Scene getCurrentScene() {
        return (Scene) this.container.getComponent(0);
    }

    public JPanel getContainer() {
        return this.container;
    }

}