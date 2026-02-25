package misc;

import service.GameObject;
import service.RunService;
import service.UserInput;

import java.awt.event.KeyEvent;

public class TestInputInRUn extends GameObject {
    public TestInputInRUn() {
        super();
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (UserInput.isKeyHold(KeyEvent.VK_E) && (UserInput.isKeyHold(KeyEvent.VK_W))) {
            System.out.println("Key Activate");
        }

    }
}
