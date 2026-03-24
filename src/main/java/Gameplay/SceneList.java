package Gameplay;

import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import scene.LoadingScene;
import scene.LobbyScene;
import scene.MainMenuScene;
import scene.MysteriousJungleScene;

public class SceneList {

    //Scene ที่มี keyword final คือ Scene ที่มีเพียงแค่ scene เดียวเท่านั้นตลอดการทำงาน
    //นอกนั้นจะ ด่าน จะถูก reset ทุกครั้งที่สร้าง
    public static final Scene mainMenu = new MainMenuScene();

    public static final Scene lobbyScene = new LobbyScene();

    public static Scene buildMysteriousJungle = new MysteriousJungleScene();

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