package Event;


import Gameplay.GameState;

public class BlizzardEvent extends Event {
    public BlizzardEvent() { super(2); }

    @Override
    public String getEventVisualName() { return "Blizzard"; }

    @Override
    public void doVisual(GameState game) {}

    @Override
    public void onEventEntered(GameState game) {
        broadcastContinueForAll(game);
    }

    @Override
    public void onEventLeave(GameState game) {}

    @Override
    public void onEventTriggered(GameState game) {}

    @Override
    public int modifyRollValue(int rollValue) {
        return (int)(Math.random() * 6) + 1;
    }
}