package Event;

import Gameplay.GameState;
import misc.PawnCharacter;
import service.CommandHandler;

public class ReverseEvent extends Event {

    public ReverseEvent() {}

    @Override
    public String getEventVisualName() {
        return "Reverse";
    }

    @Override
    public String getEventName() {
        return "Moonwalk back by 3 steps!";
    }


    @Override
    public void doVisual(GameState game) {

    }

    @Override
    public void onEventEntered(GameState game) {
        for (PawnCharacter character: game.spawnedCharacter.values()){
            int move = 3;
            int newIndex = Math.max(0, character.getCurrentTileIndex() - move);
            CommandHandler.broadcastResult("MOVETO", character.getNetworkId(), Integer.toString(newIndex));
        }
    }
    @Override
    public void onEventTriggered(GameState game) {
        broadcastContinueForAll(game);
    }



    @Override
    public void onEventLeave(GameState game) { CommandHandler.broadcastResult("UIEVENT", "EVENTICON", "blank"); }
}
