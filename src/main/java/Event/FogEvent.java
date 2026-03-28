package Event;


import Gameplay.GameState;
import scene.CyroGard;
import service.CommandHandler;

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
    public void onEventLeave(GameState game) {
        CommandHandler.broadcastResult("UIEVENT", "EVENTICON", "blank");
    }

    @Override
    public void onEventTriggered(GameState game) {broadcastContinueForAll(game);}

    @Override
    public int modifyRollValue(int rollValue) {
        int newValue = (int)(Math.random() * 6) + 1;
        CommandHandler.broadcastResult("CHAT", "Due to event, the player moved by ", String.valueOf(newValue));
        return newValue;
    }
}