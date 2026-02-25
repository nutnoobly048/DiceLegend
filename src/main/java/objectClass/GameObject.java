package objectClass;

import ServiceInterface.ProcessByRunService;
import service.RunService;

//หัวใจหลักของวัตถุในเกม
//เป็น Base Class (Abstract) สำหรับทุกอย่างที่อยู่ในเกม ไม่ว่าจะเป็นผู้เล่น ศัตรู รูป
//OnCreate(): ทำงานครั้งเดียวตอนสร้าง
//OnUpdate(deltaTime): ทำงานทุกเฟรม ใช้สำหรับคำนวณ Logic หรือการเคลื่อนที่
//OnRemoved(): ใช้สำหรับเคลียร์ Memory หรือหยุดการทำงานเมื่อวัตถุถูกลบ
public abstract class GameObject implements ProcessByRunService { //เป็น base class, เอาไป extends ทำคลาสอื่น

    public int x;//Object Position
    public int y;

    public GameObject() {
        RunService.GetService().addProcess(this); //ถ้า extends ไป อย่าลืม super() ไม่งั้นคลาสจะไม่ทำงานทุก frame
    }

    @Override //เมื่อ object ถูกสร้างให้ทำอะไร
    public void OnCreate() {

    }

    @Override //object นี้จะทำอะไรในทุกๆ frame
    public void OnUpdate(double deltaTime) {

    }

    @Override //object นี้จะทำอะไรหลังจากที่ OnUpdate() ทำงานเสร็จแล้ว
    public void OnLateUpdate() {

    }

    @Override //ถ้าทุกลบ ให้ทำอะไร
    public void OnRemoved() {
        RunService.GetService().removeProcess(this);
    }
}
