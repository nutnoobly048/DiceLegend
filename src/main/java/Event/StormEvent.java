package Event;


import Gameplay.GameState;
import misc.PawnCharacter;
import service.CommandHandler;

public class StormEvent extends Event {
    public StormEvent() { super(1); }

    @Override
    public String getEventVisualName() { return "Earthquake"; }

    @Override
    public void doVisual(GameState game) {}

    @Override
    public void onEventEntered(GameState game) {
        int boardSize = game.gameBoard.getBoardSize() - 1;

        for (PawnCharacter pawn : game.spawnedCharacter.values()) {
            int randomTile = (int)(Math.random() * (boardSize - 1)) + 1;
            CommandHandler.broadcastResult("MOVETO", pawn.getNetworkId(), String.valueOf(randomTile));
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
