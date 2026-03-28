package Event;

import Gameplay.GameState;
import service.CommandHandler;

public class MoveSlow extends Event{
    public MoveSlow(){
        super(3);
    }

    @Override
    public String getEventVisualName(){
        return "Slower";
    };

    @Override
    public String getEventName() {
        return "Slower Move";
    }


    public void doVisual(GameState game){};
    //ทำงาน Event
    public void onEventEntered(GameState game){
        broadcastContinueForAll(game);
    };
    //เมื่อ Event หมด
    public void onEventLeave(GameState game){
        CommandHandler.broadcastResult("UIEVENT", "EVENTICON", "blank");
    };

    public void onEventTriggered(GameState game){broadcastContinueForAll(game);};

    public int modifyRollValue(int rollValue) {
      if (rollValue <= 2) {
            rollValue = 2;
        }
        return rollValue - 2;
    }
}