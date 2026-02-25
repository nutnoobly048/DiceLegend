package service;

import game.window.Main;
import misc.TestInputInRUn;

import javax.swing.*;

public class MainGame extends JFrame {

    private static RunService runService;
    private JPanel currentScene;

    // Standard entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGame mainGame = new MainGame();
            mainGame.setVisible(true);
        });
    }

    public MainGame() {
        // Essential window settings
        setTitle("Game Window");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startRunService();
    }

    public void startRunService() {
        runService = RunService.GetService();
        runService.setMainGameFrame(this);
        runService.start();
        TestInputInRUn test = new TestInputInRUn();
    }
}