package service;

import Gameplay.GameState;
import Gameplay.LobbyState;
import OtherUtilities.RandomPosition;
import graphicsUtilities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainGame extends JFrame {

    private static RunService runService;
    private JPanel container;
    private static Scene currentScene = null;

    // Entry Main
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        SwingUtilities.invokeLater(() -> {
            MainGame mainGame = new MainGame();
            mainGame.setVisible(true);
        });
        System.out.println(RandomPosition.resultEventPositionString);
        System.out.println(RandomPosition.resultItemPositionString);
        System.out.println(RandomPosition.resultPortalPositionString);
    }

    public MainGame() {
        setTitle("Dice Legend");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (RunService.mqtt.isConnected()) {
                    if (LobbyState.current.isHost) {
                        RunService.mqtt.clearRetained("DiceLegend/" + LobbyState.current.lobbyName + "/room_state");
                    }

                    if (GameState.currentGame != null) {
                        if (LobbyState.current.isHost) {
                            RunService.mqtt.clearRetained(
                                    "DiceLegend/" + GameState.currentGame.getLobbyName() + "/room_state");
                        }

                        RunService.mqtt.disconnect();
                    }
                }

            }
        });
        // thanks, AI, for Fullscreen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        startRunService();
        startContainerPanel();

        SceneUtilities.changeSceneTo(SceneUtilities.mainMenu);
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
        this.setSize(1920, 1080);
        this.container = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.add(container);
    }

    public Scene getCurrentScene() {
        return (Scene) this.container.getComponent(0);
    }

    public JPanel getContainer() {
        return this.container;
    }

}