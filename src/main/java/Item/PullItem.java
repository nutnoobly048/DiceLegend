package Item;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class PullItem extends Item {

    private int targetNewIndex;

    public PullItem() {
        super(true); 
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        PawnCharacter player = state.spawnedCharacter.get(user.getNetworkID());
        targetNewIndex = player.getCurrentTileIndex();
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        PawnCharacter targetPawn = state.spawnedCharacter.get(target.getNetworkID());
        CommandHandler.broadcastResult("MOVETO", targetPawn.getNetworkId(), String.valueOf(targetNewIndex));
        CommandHandler.broadcastResult("NOTIFY", user.getName() + " Got Pulled by ", target.getName());
    }

    @Override
    public String getCardUIName() {
        return "PullItem";
    }
}