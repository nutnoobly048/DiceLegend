package Event;


import Gameplay.GameState;
import misc.PawnCharacter;
import service.CommandHandler;

public class StormEvent extends Event {
    public StormEvent() { super(1); }

    @Override
    public String getEventVisualName() { return "Storm"; }

    @Override
    public void doVisual(GameState game) {}

    @Override
    public void onEventEntered(GameState game) {
        for (PawnCharacter pawn : game.spawnedCharacter.values()) {
            int currentPos = pawn.getCurrentTileIndex();
            int min;
            int max;

            if (currentPos < 10) {
                min = 0;
                max = 20;
            } else if (currentPos < 30) {
                min = 15;
                max = 45;
            } else if (currentPos < 50) {
                min = 35;
                max = 65;
            } else if (currentPos <= 70) {
                min = 55;
                max = 85;
            } else {
                min = 70;
                max = 95;
            }

            int newTile = (int) (Math.random() * ((max - min) + 1)) + min;

            CommandHandler.broadcastResult("MOVETO", pawn.getNetworkId(), String.valueOf(newTile));
        }

        broadcastContinueForAll(game);
    }

    @Override
    public void onEventLeave(GameState game) {}

    @Override
    public void onEventTriggered(GameState game) {
        broadcastContinueForAll(game);
    }
}
