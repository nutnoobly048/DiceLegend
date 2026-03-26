package Event;

import Gameplay.GameState;

public class MoveSlow extends Event{
    public MoveSlow(){
        super(3);
    }
    public String getEventVisualName(){
        return "Movefast";
    };

    public void doVisual(GameState game){};
    //ทำงาน Event
    public void onEventEntered(GameState game){};
    //เมื่อ Event หมด
    public void onEventLeave(GameState game){};

    public void onEventTriggered(GameState game){};

    public int modifyRollValue(int rollValue) {
      if (rollValue <= 2) {
            rollValue = 2;
        }
        return rollValue - 2;
    }
}