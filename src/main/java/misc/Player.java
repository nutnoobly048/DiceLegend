package misc;

import objectClass.VisualObject;
import service.UserInput;

import java.awt.event.KeyEvent;

public class Player extends VisualObject {
    public Player(String imgFileName) {
        super(imgFileName);
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (UserInput.isKeyHold(KeyEvent.VK_D)) {
            x += (int) (300 * deltaTime); //ให้ขยับ 300 Pixel ต่อวินาที
        }
        if (UserInput.isKeyHold(KeyEvent.VK_A)) {
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
