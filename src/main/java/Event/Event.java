package Event;

import Gameplay.GameState;

public abstract class Event {

    public int remainingTurn;

    public Event(int remainingTurn) {
        this.remainingTurn = remainingTurn;
    }

    public Event() {
        this.remainingTurn = 0;
    }

    //GAMESTATE มีหน้าที่เรียกใช้
    public static void useEvent(Event event, GameState state){
        event.execute(state);
    }


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
