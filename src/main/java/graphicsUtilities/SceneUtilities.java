package graphicsUtilities;

import Gameplay.SceneList;
import objectClass.GameObject;
import scene.CyroGard;
import scene.MysteriousJungleScene;
import service.MainGame;
import service.RunService;

public class SceneUtilities {
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
        System.out.println("changes");
        switch (sceneName) {
            case "mysteriousJungle" -> changeSceneTo(new MysteriousJungleScene());
            case "cryoGard" -> changeSceneTo(new CyroGard());
            case "goldenSeason" -> {}
            case "lobbyScene" -> changeSceneTo(SceneList.lobbyScene);
            case "main_menu" -> changeSceneTo(SceneList.mainMenu);
        }
    }

    public static void changeSceneTo(Scene newScene) {
        if (currentGameScene != null) {
            currentGameScene.requestExit(() -> {
                performSwitch(newScene);
            });
        } else {
            performSwitch(newScene);
        }
    }

    private static void performSwitch(Scene newScene) {
        if (currentGameScene  != null) {
            exitScene();
            mainGameFrame.getContainer().removeAll();
        }


        newScene.setFocusable(true);
        currentGameScene = newScene;
        mainGameFrame.getContainer().add(newScene);

        startScene();
        refreshScene();
        currentGameScene.onSceneEntered();
    }

    private static void refreshScene() {
        mainGameFrame.revalidate();
        mainGameFrame.getContainer().revalidate();
        mainGameFrame.getContainer().repaint();
        currentGameScene.requestFocusInWindow();

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
