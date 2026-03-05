package Gameplay;

import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.Scene;
import objectClass.VisualObject;
import java.awt.*;


public class SceneList {

    //Scene ที่มี keyword final คือ Scene ที่มีเพียงแค่ scene เดียวเท่านั้นตลอดการทำงาน
    //นอกนั้นจะ ด่าน จะถูก reset ทุกครั้งที่สร้าง
    public static final Scene mainMenu = new Scene() {
        //สร้าง Object
        VisualObject vis = new VisualObject("board","prototypeBoard.png", 0, 540, -500,-500);

        //ทำงานทันที (วางของ setup background etc....)
        {
            //set background ไม่ก็ set background เป็นรูป
            setBackground(new Color(6,7,16));
            // วาง Object
            spawnObjectAt(vis, vis.x, vis.y);

            //ทำงานเมื่อ Scene ถูกโหลด
            setOnSceneEnter(() -> {
                Tween tween = new Tween(vis, TweenProperty.X, -960, 960, 2);
                tween.start();
                    }

            );
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