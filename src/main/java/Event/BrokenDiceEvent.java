package Event;

import Gameplay.GameState;

public class BrokenDiceEvent extends Event {
    public BrokenDiceEvent() { super(3); }

    @Override
    public String getEventVisualName() { return "BrokenDiceEvent"; }

    @Override
    public void doVisual(GameState game) {}

    @Override
    public void onEventEntered(GameState game) {
        broadcastContinueForAll(game);
    }

    @Override
    public void onEventLeave(GameState game) {}

    @Override
    public void onEventTriggered(GameState game) {
        broadcastContinueForAll(game);
    }

    @Override
    public int modifyRollValue(int rollValue) {
        return 7 - rollValue;
    }
}