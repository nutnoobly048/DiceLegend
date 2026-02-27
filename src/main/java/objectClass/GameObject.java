package objectClass;

import ServiceInterface.ProcessByRunService;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import service.RunService;

//หัวใจหลักของวัตถุในเกม
//เป็น Base Class (Abstract) สำหรับทุกอย่างที่อยู่ในเกม ไม่ว่าจะเป็นผู้เล่น ศัตรู รูป
//OnCreate(): ทำงานครั้งเดียวตอนสร้าง
//OnUpdate(deltaTime): ทำงานทุกเฟรม ใช้สำหรับคำนวณ Logic หรือการเคลื่อนที่
//OnRemoved(): ใช้สำหรับเคลียร์ Memory หรือหยุดการทำงานเมื่อวัตถุถูกลบ

//ปัญหา: GameObject ยังคงรันแม้ว่าจะไม่ได้อยู่ Scene ตัวเองก็ตาม
public abstract class GameObject implements ProcessByRunService { //เป็น base class, เอาไป extends ทำคลาสอื่น

    public int x;//Object Position
    public int y;
    public boolean isActive = true;

    protected Scene currentScene;
    public GameObject() {
    }

    @Override //เมื่อ object ถูกสร้างให้ทำอะไร
    public void OnCreate() {

    }

    @Override //object นี้จะทำอะไรในทุกๆ frame
    public void OnUpdate(double deltaTime) {
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;
    }

    @Override //object นี้จะทำอะไรหลังจากที่ OnUpdate() ทำงานเสร็จแล้ว
    public void OnLateUpdate() {
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;
    }

    @Override //ถ้าทุกลบ ให้ทำอะไร
    public void OnRemoved() {
        RunService.GetService().removeProcess(this);
    }



    public void SetCurrentGameScene(Scene gameScene) {
        this.currentScene = gameScene;
    }

    public Scene GetCurrentGameScene() {
        return this.currentScene;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
