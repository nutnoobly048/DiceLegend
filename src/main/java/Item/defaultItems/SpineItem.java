package Item.defaultItems;

import Gameplay.GameState;
import Item.base.Item;
import misc.PawnCharacter;
import misc.Player;

public class SpineItem extends Item {

    public SpineItem() {
        super(true);
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        target.increaseSkipTurns(2);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {

    }

    @Override
    public String getCardUIName() {
        return "Spine Item";
    }
}
