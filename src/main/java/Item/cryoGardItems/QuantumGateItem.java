package Item.cryoGardItems;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;
import Item.base.Item;

public class QuantumGateItem extends Item{

    public QuantumGateItem() {
        super(true);
    }

    int userNewIndex;
    int targetNewIndex;

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        PawnCharacter targetPlayer = state.spawnedCharacter.get(target);
        PawnCharacter player =  state.spawnedCharacter.get(user);

        int targetIndex = targetPlayer.getCurrentTileIndex();
        int currIndex = player.getCurrentTileIndex();

        userNewIndex = targetIndex;
        targetNewIndex = currIndex;

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
        return "Quantum Dis-Gate";
    }
}
