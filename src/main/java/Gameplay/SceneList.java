package Gameplay;

import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.GameButton;
import objectClass.GameObject;
import objectClass.AnimatedSprite;
import java.awt.*;

public class SceneList {

    //Scene ที่มี keyword final คือ Scene ที่มีเพียงแค่ scene เดียวเท่านั้นตลอดการทำงาน
    //นอกนั้นจะ ด่าน จะถูก reset ทุกครั้งที่สร้าง
    public static final Scene mainMenu = new Scene() {
        GameObject transition_left = new GameObject("transit_left", "Transition.png", 0,0);
        GameObject transition_right = new GameObject("transit_right", "Transition.png", 0,0);
        GameObject transition_down = new GameObject("transit_down", "Transition.png", 0,0);
        GameObject transition_up = new GameObject("transit_up", "Transition.png", 0,0);

        GameButton startButton = new GameButton("licoCake144", "licoCake144.png","licoCake144.png");

        {
            spawnObjectAt(transition_left);
            spawnObjectAt(transition_right);
            spawnObjectAt(transition_down);
            spawnObjectAt(transition_up);

            startButton.setBounds(300,300, 500,500);
            add(startButton);

            startButton.setOnButtonClicked(() -> {
                new Tween(transition_left, TweenProperty.X, -1920, -960, 1).start();
                new Tween(transition_right, TweenProperty.X, 1920, 960, 1).start();
                new Tween(transition_up, TweenProperty.Y, -1080, -540, 1).start();
                new Tween(transition_down, TweenProperty.Y, 1080, 540, 1).OnComplete(() -> {SceneUtilities.changeSceneTo(SceneList.joinMenu);});

            });

            setOnSceneEnter(() -> {
                new Tween(transition_left, TweenProperty.X, -960, -1920, 1).start();
                new Tween(transition_right, TweenProperty.X, 960, 1920, 1).start();
                new Tween(transition_up, TweenProperty.Y, -540, -1080, 1).start();
                new Tween(transition_down, TweenProperty.Y, 540, 1080, 1).start();
            });
        }
    };

    public static final Scene joinMenu = new Scene() {
        String savedGameRoom = "";
        {
            setOnSceneEnter(() -> {SceneUtilities.changeSceneTo(SceneList.mainMenu);});
        }

    };

    public static Scene buildMysteriousJungle() {
        return new Scene() {
            {

            }
        };
    }

    public static Scene buildCryoGarden() {
        return new Scene() {
            {

            }
        };
    }

    public static Scene buildGoldenSeason() {
        return new Scene() {
            {

            }
        };
    }
}