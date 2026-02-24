package misc;

import service.GameObject;
import service.UserInput;

public class TestInputInRUn extends GameObject {
    public TestInputInRUn() {
        super();
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (UserInput.isPressE()) {
            System.out.println("Key Activate");
        }

    }
}
