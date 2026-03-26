package Item.defaultItems;

import Gameplay.GameState;
import Item.base.Item;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class SwapItem extends Item {
    private int userNewIndex;
    private int targetNewIndex;

    public SwapItem() { super(false); }

    @Override
    public void doImmediateAction(Player user,Player targetPlayer, GameState state) {

        PawnCharacter target = GameState.currentGame.spawnedCharacter.get(user.getNetworkID());

        for (PawnCharacter character: GameState.currentGame.spawnedCharacter.values()) {

            if (target == null) {

                target = character;

            } else if (character.getCurrentTileIndex() > target.getCurrentTileIndex()) {

                target = character;

            }


        }

        PawnCharacter player =  state.spawnedCharacter.get(user.getNetworkID());

        int currIndex = player.getCurrentTileIndex();

        userNewIndex = target.getCurrentTileIndex();
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
        return "Swap Position";
    }
}
