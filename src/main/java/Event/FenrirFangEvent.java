package Event;

import Gameplay.GameState;
import service.CommandHandler;

public class FenrirFangEvent extends Event{

    public FenrirFangEvent() {
        super(3);
    }
    @Override
    public String getEventVisualName() {
        return "FenrirFang";
    }

    @Override
    public String getEventName() {
        return "Fenrir's Fang";
    }


    @Override
    public int modifyRollValue(int rollValue) {
        return 1;
    }

    @Override
    public void doVisual(GameState game) {

    }

    @Override
    public void onEventEntered(GameState game) {
        broadcastContinueForAll(game);
    }

    @Override
    public void onEventLeave(GameState game) {
        CommandHandler.broadcastResult("UIEVENT", "EVENTICON", "blank");
    }

    @Override
    public void onEventTriggered(GameState game) {
        broadcastContinueForAll(game);
    }


}
