package Item;

import Gameplay.GameState;
import misc.PawnCharacter;
import misc.Player;
import service.CommandHandler;

public class SpineItem extends Item{

    public SpineItem() {
        super(true);
    }

    @Override
    public void doImmediateAction(Player user, Player target, GameState state) {
        target.increaseSkipTurns(2);
    }

    @Override
    public void broadcastResult(Player user, Player target, GameState state) {
        CommandHandler.broadcastResult("CHAT", user.getName() + " Got Jailed For", " 2 Turn");
        broadcastContinueForAll(state);
    }

    @Override
    public String getCardUIName() {
        return "SpineItem";
    }
}
