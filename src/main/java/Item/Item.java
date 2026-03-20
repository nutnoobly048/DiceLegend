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

    public static void useItem(Item item, Player user, Player target, GameState state) {
        item.execute(user,target, state);
    }

    public final void execute(Player user, Player target, GameState state) {
        doImmediateAction(user, target, state);
        broadcastResult(user, target, state);
    }


    //Method ที่ใช้ในการคำนวณ เช่น set ผู้เล่นถอยไป 3 ช่อง
    public abstract void doImmediateAction(Player user, Player target, GameState state);

    //เป็น output ที่ใช้ในการส่งไปให้ผู้เล่นทุกคน
    public abstract void broadcastResult(Player user, Player target, GameState state);

    public abstract String getCardUIName();
}