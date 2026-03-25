package Event;

import Gameplay.GameState;
import misc.Player;
import service.CommandHandler;

public abstract class Event {

    public int remainingTurn;

    public Event(int remainingTurn, int rollValueModifier) {
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
        onEventEntered(game);
    }

    public abstract String getEventVisualName();


    //ทำงาน Visual Effect เริ่มต้น
    public abstract void doVisual(GameState game);
    //ทำงาน Event
    public abstract void onEventEntered(GameState game);
    //เมื่อ Event หมด
    public abstract void onEventLeave(GameState game);

    public abstract void onEventTriggered(GameState game);

    public int modifyRollValue(int rollValue) {
        return rollValue;
    }

    //ให้เรียกใช้ทุกครั้งใน broadcastResult ถ้า Event นั้นไม่มีการขยับตัวละคร
    protected void broadcastContinueForAll(GameState state) {
        for (Player p : state.allPlayers.values()) {
            CommandHandler.broadcastResult("CONTINUE", p.getNetworkID());
            System.out.println(p.getNetworkID() + " SHOUlD CONTINUE SINCE THIS IS NOT MOVING EVENT");
        }
    }
}
