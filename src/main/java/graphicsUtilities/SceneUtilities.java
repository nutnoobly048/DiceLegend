package graphicsUtilities;

import objectClass.GameObject;
import service.MainGame;

public class SceneUtilities {
    private static MainGame currentGameFrame;

    public static MainGame getCurrentGameFrame() {
        return currentGameFrame;
    }

    public static void setCurrentGameFrame(MainGame currentGameFrame) {
        SceneUtilities.currentGameFrame = currentGameFrame;
        System.out.println("Scene Manager Initialized");
    }
}
