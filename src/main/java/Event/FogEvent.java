package Event;


import Gameplay.GameState;

public class FogEvent extends Event {
    public FogEvent() { super(2); }

    @Override
    public String getEventVisualName() { return "Fog"; }
    @Override
    public String getEventName() {
        return "Fog";
    }

    @Override
    public void doVisual(GameState game) {}

    @Override
    public void onEventEntered(GameState game) {
        broadcastContinueForAll(game);
    }

    @Override
    public void onEventLeave(GameState game) {}

    @Override
    public void onEventTriggered(GameState game) {broadcastContinueForAll(game);}

    @Override
    public int modifyRollValue(int rollValue) {
        return (int)(Math.random() * 6) + 1;
    }
}