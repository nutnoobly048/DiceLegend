package graphicsUtilities;

import ServiceInterface.Drawable;
import objectClass.GameObject;
import service.RunService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

//พื้นที่แสดงผล (Stage)
//เปรียบเสมือนฉากในเกมที่เก็บรายการของ GameObject ทั้งหมดเอาไว้
//มีระบบ spawnObjectAt เพื่อวางสิ่งของลงในพิกัดที่ต้องการ
//จัดการเรื่องการวาด (Paint) วัตถุทั้งหมดที่อยู่ในฉากนั้นๆ
//ยังสามารถพัฒนาเพิ่มได้อีกเยอะ
public class Scene extends JPanel {

    // Default background
    private Image background = ImagePreload.get("blank.png");
    private HashMap<String, GameObject> currentSceneObject = new HashMap<>();

    public Scene() {
        this.setPreferredSize(new Dimension(1920, 1080));
    }

    public void onSceneEntered() {

    }

    public void onSceneExited() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        for (GameObject obj : currentSceneObject.values()) {
            if (obj instanceof Drawable) {
                ((Drawable) obj).draw(g2d);
            }
        }
    }

    public void setBackground(String backgroundName) {
        this.background = ImagePreload.get(backgroundName);
        this.repaint();
    }

    public void setBackground(Image img) {
        this.background = img;
        this.repaint();
    }

    public void spawnObjectAt(GameObject g, int x, int y) {
        g.x = x;
        g.y = y;
        g.SetCurrentGameScene(this);
        currentSceneObject.put(g.networkId, g);

        RunService.GetService().addProcess(g);

    }
    public void putObjectAt(GameObject g, int x, int y) {
        g.x = x;
        g.y = y;
        g.SetCurrentGameScene(this);
        currentSceneObject.put(g.networkId, g);
    }


    public HashMap<String, GameObject> getAllSceneObject() {
        return this.currentSceneObject;
    }
}
