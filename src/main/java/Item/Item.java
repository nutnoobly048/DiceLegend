package Item;

import Gameplay.GameState;
import misc.Player;

public abstract class Item {

    public final String itemId;
    public final String itemName;

    public Item(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public final void execute(Player user, Player target, GameState state) {
        doImmediateAction(user, target, state);
        broadcastResult(user, target, state);
    }

    public abstract void doImmediateAction(Player user, Player target, GameState state);
    public abstract void broadcastResult(Player user, Player target, GameState state);

    public abstract String getCardUIName();
}