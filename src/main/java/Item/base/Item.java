package Item.base;

import Gameplay.GameState;
import misc.Player;
import service.CommandHandler;

public abstract class Item {

    private boolean requireTarget;

    public Item(boolean requireTarget) {
        this.requireTarget = requireTarget;
    }

    public boolean isRequireTarget() {
        return requireTarget;
    }

    public static void useItem(Item item, Player user, Player target, GameState state) {
        item.execute(user, target, state);
    }

    public final void execute(Player user, Player target, GameState state) {
        doImmediateAction(user, target, state);
        broadcastResult(user, target, state);
    }

    // Method ที่ใช้ในการคำนวณ เช่น set ผู้เล่นถอยไป 3 ช่อง
    public abstract void doImmediateAction(Player user, Player target, GameState state);

    // เป็น output ที่ใช้ในการส่งไปให้ผู้เล่นทุกคน
    public abstract void broadcastResult(Player user, Player target, GameState state);

    public abstract String getCardUIName();

    // ให้เรียกใช้ทุกครั้งใน broadcastResult ถ้า Item นั้นไม่มีการขยับตัวละคร
    protected void broadcastContinueForAll(GameState state) {
        for (Player p : state.allPlayers.values()) {
            CommandHandler.broadcastResult("CONTINUE", p.getNetworkID());
            System.out.println(p.getNetworkID() + " SHOUlD CONTINUE SINCE THIS IS NOT MOVING ITEM");
        }
    }
}