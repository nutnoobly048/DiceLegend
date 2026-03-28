package Event;

import Gameplay.GameState;
import service.CommandHandler;

public class BrokenDiceEvent extends Event {
    public BrokenDiceEvent() { super(3); }

    @Override
    public String getEventVisualName() { return "BrokenDice"; }

    @Override
    public String getEventName() {
        return "Broken Dice";
    }
    @Override
    public void doVisual(GameState game) {}

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

    @Override
    public int modifyRollValue(int rollValue) {
        CommandHandler.broadcastResult("NOTIFY", "Due to event, the player moved by : ", String.valueOf(7 - rollValue));
        return 7 - rollValue;
    }
}