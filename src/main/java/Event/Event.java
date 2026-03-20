package Event;

import Gameplay.GameState;

public abstract class Event {

    public final String eventId;
    public final String eventName;

    public Event(String eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }


    //GAMESTATE มีหน้าที่เรียกใช้
    public final void execute(GameState game) {
        doVisual(game);
        doImmediateAction(game);
        broadcastResult(game);
    }

    public abstract String getEventVisualName();

    public abstract void doImmediateAction(GameState game);

    public abstract void broadcastResult(GameState game);

    public abstract void doVisual(GameState game);
}
