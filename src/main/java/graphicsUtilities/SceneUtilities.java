package graphicsUtilities;

import misc.Player;
import objectClass.GameObject;
import objectClass.VisualObject;
import service.MainGame;
import service.RunService;

public class SceneUtilities {
    private static MainGame mainGameFrame;
    private static Scene currentGameScene;


    public static Scene scene2 = new Scene();

    public static Scene scene3 = new Scene();



    public static MainGame getCurrentGameFrame() {
        return mainGameFrame;
    }

    public static void setCurrentGameFrame(MainGame currentGameFrame) {
        SceneUtilities.mainGameFrame = currentGameFrame;
        System.out.println("Scene Manager Initialized");

        scene2.spawnObjectAt(new Player("licoCake144"), 500,500);
        scene3.spawnObjectAt(new Player("licoCake144"), 800,300);
    }
    public static void changeSceneTo(SceneList sceneData) {
        System.out.println("Switching to Scene: " + sceneData.name());

        Scene newSceneInstance = sceneData.create();
        changeSceneTo(newSceneInstance);
    }

    public static void changeSceneTo(Scene newScene) {
        if (currentGameScene != null) {
            exitScene();
            mainGameFrame.getContainer().removeAll();
        }
        newScene.setFocusable(false);

        currentGameScene = newScene;
        mainGameFrame.getContainer().add(newScene);

        startScene();

        refreshScene();
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

        for (GameObject obj : currentGameScene.getAllSceneObject()) {
            runService.removeProcess(obj);
        }
//        System.out.println("Successfully Removed");
    }

    private static void startScene() {
        currentGameScene = mainGameFrame.getCurrentScene();

        if (currentGameScene == null ) {
            System.err.println("Error: currentGameScene is not found!");
            return;
        }
        RunService runService = RunService.GetService();

        for (GameObject obj : currentGameScene.getAllSceneObject()) {
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
