package objectClass;

import ServiceInterface.ProcessByRunService;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import service.RunService;

import javax.swing.*;
import java.awt.*;

//หัวใจหลักของวัตถุในเกม
//เป็น Base Class สำหรับทุกอย่างที่อยู่ในเกม ไม่ว่าจะเป็นผู้เล่น ศัตรู
//OnUpdate(deltaTime): ทำงานทุกเฟรม ใช้สำหรับคำนวณ Logic หรือการเคลื่อนที่

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
        System.out.println(this.networkId);
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;

        if (sprite != null) {
            sprite.posX = this.x;
            sprite.posY = this.y;
            sprite.OnUpdate(deltaTime);
        }
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void setSprite(AnimatedSprite newSprite) {
        this.sprite = newSprite;
        if (this.sprite != null) {
            this.sprite.posX = this.x;
            this.sprite.posY = this.y;
        }
    }
    public void setSprite(String imgFileName) {
        AnimatedSprite newSprite = new AnimatedSprite(imgFileName,0,0,1,1 );
        this.sprite = newSprite;

        if (this.sprite != null) {
            this.sprite.posX = this.x;
            this.sprite.posY = this.y;
        }
    }

}