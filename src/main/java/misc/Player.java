package misc;

import graphicsUtilities.Scene;
import graphicsUtilities.SceneList;
import graphicsUtilities.SceneUtilities;
import objectClass.VisualObject;
import service.UserInput;

import java.awt.event.KeyEvent;

public class Player extends VisualObject {
    public Player(String imgFileName) {
        super(imgFileName);
    }
    public Player(String imgFileName, int x, int y) {
        super(imgFileName,x,y);
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;

        if (UserInput.isKeyHold(KeyEvent.VK_D)) {
            x += (int) (300 * deltaTime); //ให้ขยับ 300 Pixel ต่อวินาที
            SceneUtilities.changeSceneTo(SceneUtilities.scene2);
        }
        if (UserInput.isKeyJustPressed(KeyEvent.VK_A)) {
            x -= (int) (300 * deltaTime);

        }
        if (UserInput.isKeyHold(KeyEvent.VK_W)) {
            y -= (int) (300 * deltaTime);
        }
        if (UserInput.isKeyHold(KeyEvent.VK_S)) {
            y += (int) (300 * deltaTime);
        }
    }
}
