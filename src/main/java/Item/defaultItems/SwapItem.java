package Item.defaultItems;

import Gameplay.GameState;
import Item.base.Item;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class SwapItem extends Item {
    private int userNewIndex;
    private int targetNewIndex;

    public SwapItem() {
        super(true);
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target);
        PawnCharacter player = state.spawnedCharacter.get(user);

        int targetIndex = targetPlayer.getCurrentTileIndex();
        int currIndex = player.getCurrentTileIndex();

        userNewIndex = targetIndex;
        targetNewIndex = currIndex;

        // targetPlayer.setCurrentTileIndex(targetNewIndex);
        // player.setCurrentTileIndex(userNewIndex);
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
