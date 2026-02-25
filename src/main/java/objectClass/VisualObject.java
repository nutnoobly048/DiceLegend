package objectClass;

import ServiceInterface.Drawable;
import graphicsUtilities.ImagePreload;

import java.awt.*;
//วัตถุที่มีตัวตนและมองเห็นได้
//ตัวนี้ extends GameObject และเพิ่มความสามารถในการ Render เข้ามา
//จะดึงรูปจาก ImagePreload อัตโนมัติ แค่ใส่ชื่อไฟล์ ไม่ต้องมี Png
//ถ้าหาไฟล์ไม่เจอ จะแสดงรูป blank.png เพื่อป้องกัน Game Crash

public class VisualObject extends GameObject implements Drawable {
    Image img;
    public VisualObject(String imgFileName, int x, int y) {
        super();
        this.img = ImagePreload.get(imgFileName + ".png");

        if (this.img == null) {
            this.img = ImagePreload.get("blank.png");
        }
        this.x = x;
        this.y = y;
    }
    public VisualObject(String imgFileName) {
        this(imgFileName, 0, 0);
    }

    public VisualObject() {
        this("blank", 0, 0);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (img == null) return;
        g2d.drawImage(img, this.x, this.y, null);
    }
}
