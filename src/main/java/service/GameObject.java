package service;

import ServiceInterface.ProcessByRunService;


public class GameObject implements ProcessByRunService { //เป็น base class, เอาไป extends ทำคลาสอื่น
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
