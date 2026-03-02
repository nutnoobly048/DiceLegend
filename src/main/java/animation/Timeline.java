package animation;

import java.util.ArrayList;
import java.util.List;

public class Timeline {

    private List<Tween> tweenList = new ArrayList<>();
    private boolean isLooping = false;
    private double timeScale = 1.0;

    public void add(Tween tween) {
        tweenList.add(tween);
    }

    public void setLooping(boolean bool) {
        this.isLooping = bool;
    }

    public void setTimeScale(double scale) {
        this.timeScale =  scale;
    }

    public void play() {
        for (Tween t : tweenList) {
            t.start();
        }
    }
}
