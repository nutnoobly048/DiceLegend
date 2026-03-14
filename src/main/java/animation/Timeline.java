package animation;

import ServiceInterface.ProcessByRunService;
import service.RunService;
import java.util.ArrayList;
import java.util.List;

public class Timeline implements ProcessByRunService {

    private static class TimelineItem {
        Tween tween;
        double delay;
        boolean start = false;

        TimelineItem(Tween tween, double delay) {
            this.tween = tween;
            this.delay = delay;
        }
    }

    private List<TimelineItem> tweenList = new ArrayList<>();

    public boolean isLooping = false;
    public double timeScale = 1.0; // ตัวคูณความเร็ว
    private boolean isPlaying = false;
    private double time = 0; // เวลาปจบ --> timeline
    private double totalDuration = 0;

    public void add(Tween tween, double delay) {
        TimelineItem item = new TimelineItem(tween, delay);
        tweenList.add(item);
        double tweenEnd = delay + tween.getDuration();
        totalDuration = Math.max(totalDuration, tweenEnd);
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    // copy tweenList
    public List<Tween> getTweenList() {
        List<Tween> list = new ArrayList<>();
        for (TimelineItem item : tweenList) {
            list.add(item.tween);
        }
        return list;
    }

    public void play() {
        if (isPlaying){return;}
        isPlaying = true;
        RunService.GetService().addProcess(this);
    }

    public void pause() {
        isPlaying = false;
    }

    public void stop() {
        isPlaying = false;
        time = 0;
        for (TimelineItem item : tweenList) {
            item.start = false;
        }
        RunService.GetService().removeProcess(this);
    }

    private void restart() {
        time = 0;
        for (TimelineItem item : tweenList) {
            item.start = false;
        }
    }
    @Override
    public void OnUpdate(double deltatime) {
        if (!isPlaying) {return;}

        time += deltatime * timeScale;
        for (TimelineItem item : tweenList) {
            if (!item.start && time >= item.delay) {
                item.start = true;
                item.tween.start();
            }
        }

        if (time >= totalDuration) {
            if (isLooping) {restart();}
            else {stop();}
        }
    }


}

/*
วิธีใช้ เดี่ยวค่อยลบ
Timeline timeline = new Timeline();
\
สรา้ง Tween
Tween moveX = new Tween(player, TweenProperty.X, 100, 500, 2);
Tween moveY = new Tween(player, TweenProperty.Y, 100, 300, 2);

เลื่อน x ก่อนค่อยเลื่อน y
timeline.add(moveX, 0);
timeline.add(moveY, 2);

เลื่อนพร้อมกัน
timeline.add(moveX, 0);
timeline.add(moveY, 0);

timeline.play();
*/