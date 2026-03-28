package Item;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class BorealisItem extends Item{

    int targetIndex;

    public BorealisItem() {
        super(true);
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target.getNetworkID());
        PawnCharacter player = state.spawnedCharacter.get(user.getNetworkID());

        int currentPlayerIndex = player.getCurrentTileIndex();

        targetIndex = currentPlayerIndex;

        target.increaseSkipTurns(1);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target.getNetworkID());

        CommandHandler.broadcastResult("MOVETO", targetPlayer.getNetworkId(), Integer.toString(targetIndex));
        CommandHandler.broadcastResult("NOTIFY", target.getName() + " Has Been Pulled And Locked by", " Borealis");
    }

    @Override
    public String getCardUIName() {
        return "BorealisItem";
    }
}
