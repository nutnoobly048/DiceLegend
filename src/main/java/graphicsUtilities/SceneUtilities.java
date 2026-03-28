package graphicsUtilities;

import objectClass.GameObject;
import scene.CyroGard;
import scene.LobbyScene;
import scene.RealMainMenuScene;
import service.MainGame;
import service.RunService;

import javax.swing.*;

public class SceneUtilities {
    public static final Scene mainMenu = new RealMainMenuScene();
    public static final Scene lobbyScene = new LobbyScene();
    private static MainGame mainGameFrame;
    private static Scene currentGameScene;

    public static MainGame getCurrentGameFrame() {
        return mainGameFrame;
    }

    public static void setCurrentGameFrame(MainGame currentGameFrame) {
        SceneUtilities.mainGameFrame = currentGameFrame;
        System.out.println("Scene Manager Initialized");

    }

    public static void changeSceneTo(String sceneName) {
        switch (sceneName) {
            case "cryoGard" -> changeSceneTo(new CyroGard());
            case "lobbyScene" -> changeSceneTo(lobbyScene);
            case "main_menu" -> changeSceneTo(mainMenu);
        }
    }

    public static void changeSceneTo(Scene newScene) {
        if (currentGameScene != null) {
            currentGameScene.onExit();

            Timer timer = new Timer(currentGameScene.getSceneLoadoffTimesInMilisecond(), e -> performSwitch(newScene));
            timer.setRepeats(false);
            timer.start();
        } else {
            performSwitch(newScene);
        }
    }

    private static void performSwitch(Scene newScene) {
        RunService runService = RunService.GetService();

        if (currentGameScene != null) {
            for (GameObject obj : currentGameScene.getAllSceneObject().values()) {
                runService.removeProcess(obj);
            }
            mainGameFrame.getContainer().removeAll();
        }

        newScene.onCreate();
        currentGameScene = newScene;
        mainGameFrame.getContainer().add(newScene);

        for (GameObject obj : newScene.getAllSceneObject().values()) {
            runService.addProcess(obj);
        }


        newScene.requestFocusInWindow();
        newScene.onEnter();
        mainGameFrame.getContainer().revalidate();
        mainGameFrame.getContainer().repaint();
    }

    private static void exitScene() {
        currentGameScene = mainGameFrame.getCurrentScene();

        if (currentGameScene == null ) {
            System.err.println("Critical Error: currentGameScene is not found!");
            return;
        }

        RunService runService = RunService.GetService();

        for (GameObject obj : currentGameScene.getAllSceneObject().values()) {
            runService.removeProcess(obj);
        }
    }

    private static void startScene() {
        currentGameScene = mainGameFrame.getCurrentScene();

        if (currentGameScene == null ) {
            System.err.println("Error: currentGameScene is not found!");
            return;
        }
        RunService runService = RunService.GetService();

        for (GameObject obj : currentGameScene.getAllSceneObject().values()) {
            runService.addProcess(obj);
        }
    }

    public static void setCurrentGameScene(Scene currentGameScene) {
        SceneUtilities.currentGameScene = currentGameScene;
    }

    public static Scene getCurrentGameScene() {
        return currentGameScene;
    }
}
