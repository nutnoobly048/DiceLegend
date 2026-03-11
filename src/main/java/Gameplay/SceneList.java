package Gameplay;

import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import scene.MainMenuScene;

public class SceneList {

    //Scene ที่มี keyword final คือ Scene ที่มีเพียงแค่ scene เดียวเท่านั้นตลอดการทำงาน
    //นอกนั้นจะ ด่าน จะถูก reset ทุกครั้งที่สร้าง
    public static final Scene mainMenu = new MainMenuScene();

    public static final Scene joinMenu = new Scene() {
        String savedGameRoom = "";
        {

        }

    };

    public static Scene buildMysteriousJungle() {
        return new Scene() {
            {
                setOnSceneEnter(() -> {
                    for (Player player : GameState.currentGame.allPlayers.values()) {
                        PawnCharacter character = new PawnCharacter(player.getNetworkID(), "blank.png");
                        spawnObjectAt(character, 300, 400);
                    }
                });
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