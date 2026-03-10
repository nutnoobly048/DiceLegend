package objectClass;

import ServiceInterface.ProcessByRunService;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import service.RunService;

import javax.swing.*;

//หัวใจหลักของวัตถุในเกม
//เป็น Base Class (Abstract) สำหรับทุกอย่างที่อยู่ในเกม ไม่ว่าจะเป็นผู้เล่น ศัตรู รูป
//OnCreate(): ทำงานครั้งเดียวตอนสร้าง
//OnUpdate(deltaTime): ทำงานทุกเฟรม ใช้สำหรับคำนวณ Logic หรือการเคลื่อนที่
//OnRemoved(): ใช้สำหรับเคลียร์ Memory หรือหยุดการทำงานเมื่อวัตถุถูกลบ

//ปัญหา: GameObject ยังคงรันแม้ว่าจะไม่ได้อยู่ Scene ตัวเองก็ตาม
public class GameObject implements ProcessByRunService {
    public int x;
    public int y;
    public int z; //depth -- ใช้ในการเรียงว่าอะไรวาดก่อน/หลัง
    public boolean isActive = true;
    public boolean isVisible = true;

    public String networkId = "";
    protected Scene currentScene;
    protected AnimatedSprite sprite;

    public GameObject(String networkId, AnimatedSprite sprite, int x, int y) {
        this.networkId = networkId;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public GameObject() {
        this("obj", (AnimatedSprite) null, 0, 0);
    }

    public GameObject(String networkId) {
        this(networkId, (AnimatedSprite) null, 0, 0);
    }

    public GameObject(String networkId, String imgFileName, int x, int y) {
        this(networkId,new AnimatedSprite(imgFileName, 0, 0, 1, 1), x, y );
    }

    public GameObject(String networkId, String imgFileName, int frameCount, double fps) {
        this(networkId, new AnimatedSprite(imgFileName, 0, 0, frameCount, fps), 0, 0);
    }

    public GameObject(String networkId, AnimatedSprite sprite) {
        this(networkId, sprite, 0, 0);
    }

    @Override
    public void OnUpdate(double deltaTime) {
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;

        if (sprite != null) {
            sprite.posX = this.x;
            sprite.posY = this.y;
            sprite.OnUpdate(deltaTime);
        }
    }


    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible && isActive;
    }

    public boolean hasSprite() {
        return this.sprite != null;
    }

    public AnimatedSprite getSprite() {
        return this.sprite;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setCurrentGameScene(Scene gameScene) {
        this.currentScene = gameScene;
    }

    @Override
    public void OnRemoved() {
        RunService.GetService().removeProcess(this);
    }

    @Override
    public void OnCreate() {}

    @Override
    public void OnLateUpdate() {}
}