package Gameplay;

import graphicsUtilities.Scene;
import objectClass.VisualObject;

public class SceneList {



    public static final Scene mainMenu = new Scene() {
        VisualObject vis = new VisualObject("lico","licoCake144", 800, 300);
        {
            putObjectAt(vis, vis.x, vis.y);
        }
    };

    public static final Scene joinMenu = new Scene() {
        String savedGameRoom = "";
        {

        }

    };

    public static Scene buildMysteriousJungle() {
        return new Scene() {

            {
            }
        };
    }

    public static Scene buildCyberGard() {
        return new Scene() {
            {

            }
        };
    }
}