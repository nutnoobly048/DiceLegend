package Item;

import Gameplay.GameState;
import misc.Player;
import service.CommandHandler;

public class DoubleDiceItem extends Item {

    public DoubleDiceItem() { super(false); }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
         user.increaseExtraTurns(1);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        broadcastContinueForAll(state);
    }

    @Override
    public String getCardUIName() {
        return "Double Dice";
    }
}
