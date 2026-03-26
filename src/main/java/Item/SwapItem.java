package Item;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class SwapItem extends Item {
    private int userNewIndex;
    private int targetNewIndex;

    public SwapItem() { super(true); }

    @Override
    public void doImmediateAction(Player user, Player targetPlayer, GameState state) {
        PawnCharacter furthest = null;

        for (PawnCharacter character : state.spawnedCharacter.values()) {
            if (character.getNetworkId().equals(user.getNetworkID())) continue;

            if (furthest == null || character.getCurrentTileIndex() > furthest.getCurrentTileIndex()) {
                furthest = character;
            }
        }

        PawnCharacter userPawn = state.spawnedCharacter.get(user.getNetworkID());

        if (furthest == null || furthest == userPawn) return; // no valid target

        userNewIndex   = furthest.getCurrentTileIndex();
        targetNewIndex = userPawn.getCurrentTileIndex();
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        PawnCharacter userPawn = state.spawnedCharacter.get(user.getNetworkID());
        PawnCharacter targetPawn = state.spawnedCharacter.get(target.getNetworkID());

        CommandHandler.broadcastResult("MOVETO", userPawn.getNetworkId(), String.valueOf(userNewIndex));
        CommandHandler.broadcastResult("MOVETO", targetPawn.getNetworkId(), String.valueOf(targetNewIndex));
    }

    @Override
    public String getCardUIName() {
        return "Swap Position";
    }
}
