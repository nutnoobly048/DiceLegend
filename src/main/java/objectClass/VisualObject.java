package objectClass;

import Gameplay.SceneList;
import ServiceInterface.Drawable;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.SceneUtilities;
import service.UserInput;

import java.awt.*;
import java.awt.event.KeyEvent;
//วัตถุที่มีตัวตนและมองเห็นได้
//ตัวนี้ extends GameObject และเพิ่มความสามารถในการ Render เข้ามา
//จะดึงรูปจาก ImagePreload อัตโนมัติ แค่ใส่ชื่อไฟล์ ไม่ต้องมี Png
//ถ้าหาไฟล์ไม่เจอ จะแสดงรูป blank.png เพื่อป้องกัน Game Crash

public class VisualObject extends GameObject implements Drawable {
    private Image img;
    private boolean isVisible = true;

    public VisualObject(String networkID, String imgFileName, int x, int y) {
        super(networkID);
        this.img = ImagePreload.get(imgFileName + ".png");

        if (this.img == null) {
            this.img = ImagePreload.get("blank.png");
        }
        this.x = x;
        this.y = y;
    }
    public VisualObject(String imgFileName) {
        super("obj");
        this.img = ImagePreload.get(imgFileName + ".png");

        if (this.img == null) {
            this.img = ImagePreload.get("blank.png");
        }
        this.x = x;
        this.y = y;
    }


    public VisualObject() {
        this("obj","blank", 0, 0);
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;
    }

    @Override
    public void OnLateUpdate() {
        super.OnLateUpdate();
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (img != null && isVisible && isActive) {
            g2d.drawImage(img, this.x, this.y, null);
        }
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
