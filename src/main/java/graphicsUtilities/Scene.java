package graphicsUtilities;

import ServiceInterface.Drawable;
import objectClass.GameObject;
import service.RunService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//พื้นที่แสดงผล (Stage)
//เปรียบเสมือนฉากในเกมที่เก็บรายการของ GameObject ทั้งหมดเอาไว้
//มีระบบ spawnObjectAt เพื่อวางสิ่งของลงในพิกัดที่ต้องการ
//จัดการเรื่องการวาด (Paint) วัตถุทั้งหมดที่อยู่ในฉากนั้นๆ
//ยังสามารถพัฒนาเพิ่มได้อีกเยอะ
public class Scene extends JPanel {
    private Scene subScene; //to display subscene using itself maybe for minigame
    private ArrayList<GameObject> currentSceneObject = new ArrayList<>();

    public Scene() {
        this.setPreferredSize(new Dimension(1920, 1080));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (GameObject obj : currentSceneObject) {
            if (obj instanceof Drawable) {
                ((Drawable) obj).draw(g2d);
            }
        }
    }

    public void spawnObjectAt(GameObject g, int x, int y) {
        g.x = x;
        g.y = y;
        g.SetCurrentGameScene(this);

        currentSceneObject.add(g);

        RunService.GetService().addProcess(g);

    }
    public ArrayList<GameObject> getAllSceneObject() {
        return this.currentSceneObject;
    }

}
