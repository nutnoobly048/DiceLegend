package Event;

import Gameplay.GameState;
import misc.PawnCharacter;
import service.CommandHandler;

public class ReverseEvent extends Event {

    public ReverseEvent(String eventId, String eventName) {
        super(eventId, eventName);
    }

    @Override
    public String getEventVisualName() {
        return "Reverse";
    }

    @Override
    public void doImmediateAction(GameState game) {
        for (PawnCharacter character: GameState.currentGame.spawnedCharacter.values()){
            int move = 1;
            if (character.getCurrentTileIndex() > (move - 1)) {
                CommandHandler.broadcastResult("MOVETO", character.getNetworkId(), Integer.toString(character.getCurrentTileIndex() - move));
            }
        }
    }

    @Override
    public void broadcastResult(GameState game) {

    }

    @Override
    public void doVisual(GameState game) {

    }
}
