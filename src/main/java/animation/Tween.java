package animation;

import ServiceInterface.ProcessByRunService;
import objectClass.GameObject;
import service.RunService;

import java.util.TreeMap;

public class Tween implements ProcessByRunService {

    private GameObject target;
    private TweenProperty property;

    private double startVal;
    private double endValue;
    private double duration;

    private double pastTime = 0;
    private boolean isPlaying = false;

    private Runnable onComplete;

    public Tween (GameObject target,
                  TweenProperty property,
                  double startVal,
                  double endValue,
                  double duration) {
        this.target = target;
        this.property = property;
        this.startVal = startVal;
        this.endValue = endValue;
        this.duration = duration;
    }

    public void start() {
        isPlaying = true;
        RunService.GetService().addProcess(this);
    }

    public void OnComplete(Runnable callback) {
        this.onComplete = callback;
    }

    private void applyValue(double value) {
        switch (property) {
            case X:
                target.x = (int) value;
                break;
            case Y:
                target.y = (int) value;
                break;
        }
    }

    @Override
    public void OnCreate() {}

    @Override
    public void OnUpdate(double deltatime) {
        if (!isPlaying) {return;}     //ถ้ายังไม่เล่นไม่ต้องทำอะไร

        pastTime += deltatime;    //บวกเวลา Frame นี้
        double t = Math.min(pastTime / duration, 1.0);    // คำนวนว่ากี่ % แล้ว
        double val = startVal + (endValue - startVal) * t;    //คำนวนค่าปัจจุบันของ Animation (ตำแหน่งตอนนี้)
        applyValue(val);    //เอาค่าไปใส่ใน obj

        //เช็คว่าเล่นเสร็จรึยัง
        if (t >= 1.0) {
            isPlaying = false;
            if (onComplete != null) {onComplete.run();}
            RunService.GetService().removeProcess(this);
        }
    }

    @Override
    public void OnLateUpdate() {}

    @Override
    public void OnRemoved() {}
}
