package graphicsUtilities;

import objectClass.GameObject;
import service.RunService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//พื้นที่แสดงผล (Stage)
//เปรียบเสมือนฉากในเกมที่เก็บรายการของ GameObject ทั้งหมดเอาไว้
//มีระบบ spawnObjectAt เพื่อวางสิ่งของลงในพิกัดที่ต้องการ
//จัดการเรื่องการวาด (Paint) วัตถุทั้งหมดที่อยู่ในฉากนั้นๆ
//ยังสามารถพัฒนาเพิ่มได้อีกเยอะ
public class Scene extends JPanel {

    // Default background
    private Image background = ImagePreload.get("blank.png");
    private CopyOnWriteArrayList<GameObject>            drawList     = new CopyOnWriteArrayList<>();
    private HashMap<String, GameObject> currentSceneObject = new HashMap<>();

    private Runnable onEnterMethod = () -> {};
    private Runnable onExitedMethod = () -> {};

    public Scene() {
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setLayout(null);
    }


    public void setOnSceneExited(Runnable onExitedMethod) {
        this.onExitedMethod = onExitedMethod;
    }

    public void setOnSceneEnter(Runnable onEnterMethod) {
        this.onEnterMethod = onEnterMethod;
    }

    public void onSceneEntered() {
        onEnterMethod.run();
    }

    public void onSceneExited() {
        onExitedMethod.run();
    }

    public void requestExit(Runnable onExitComplete) {
        if (onExitedMethod != null) {
            onExitedMethod.run();
        }

        onExitComplete.run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }

        for (GameObject obj : drawList) {
            if (obj.hasSprite() && obj.isVisible()) {
                obj.getSprite().draw(g2d);
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

    //สำหรับเพิ่ม
    public void spawnObjectAt(GameObject g, int x, int y) {
        g.x = x;
        g.y = y;
        registerObject(g);
    }

    public void spawnObjectAt(GameObject g) {
        registerObject(g);
    }

    private void registerObject(GameObject g) {
        g.setCurrentGameScene(this);

        int insertAt = drawList.size();
        for (int i = 0; i < drawList.size(); i++) {
            if (g.z < drawList.get(i).z) {
                insertAt = i;
                break;
            }
        }
        drawList.add(insertAt, g);

        currentSceneObject.put(g.networkId, g);

        if (SceneUtilities.getCurrentGameScene() == this) {
            RunService.GetService().addProcess(g);
        }
    }
    public GameObject getObjectByID(String networkId) {
        return this.currentSceneObject.get(networkId);
    }

    public HashMap<String, GameObject> getAllSceneObject() {
        return this.currentSceneObject;
    }
}
