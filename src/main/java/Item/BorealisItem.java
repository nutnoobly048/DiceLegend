package Item;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class BorealisItem extends Item{


    public BorealisItem(String itemId, String itemName) {
        super(itemId, itemName);
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target);
        PawnCharacter player = state.spawnedCharacter.get(user);

        int currentPlayerIndex = player.getCurrentTileIndex();

        targetPlayer.setCurrentTileIndex(currentPlayerIndex);
        target.increaseSkipTurns(1);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target);

        CommandHandler.broadcastResult("MOVETO", targetPlayer.getNetworkId(), Integer.toString(targetPlayer.getCurrentTileIndex()));
    }

    @Override
    public String getCardUIName() {
        return "Borealis";
    }
}
