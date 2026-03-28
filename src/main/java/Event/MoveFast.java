package Event;

import Gameplay.GameState;

public class MoveFast extends Event{
    public MoveFast(){
        super(3);
    }
    public String getEventVisualName(){
        return "Faster";
    };
    @Override
    public String getEventName() {
        return "Faster Move";
    }


    public void doVisual(GameState game){};
    //ทำงาน Event
    public void onEventEntered(GameState game){
        broadcastContinueForAll(game);
    };
    //เมื่อ Event หมด
    public void onEventLeave(GameState game){};

    public void onEventTriggered(GameState game){broadcastContinueForAll(game);};

    public int modifyRollValue(int rollValue) {
        return rollValue + 2;
    }
}