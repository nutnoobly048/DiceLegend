package Item;

import Gameplay.GameState;
import misc.Player;
import service.CommandHandler;

public class DoubleDiceItem extends Item {

    public DoubleDiceItem(String itemId, String itemName) { super(itemId, itemName); }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
         user.increaseExtraTurns(1);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        //CommandHandler.broadcastResult("EXTRATURN", user.getNetworkID(), ""));
    }

    @Override
    public String getCardUIName() {
        return "Double Dice";
    }
}
