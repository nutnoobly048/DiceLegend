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

    public boolean requiresTarget() {
        return true;
    }
    public abstract void applyEffect(Player user, Player target, GameState state);

    public abstract void broadcastResult(Player user, Player target, GameState state);

    public abstract String getCardUIName();
}