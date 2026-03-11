package misc;

import objectClass.GameObject;


public class PawnCharacter extends GameObject {

    private int currentTileIndex = 0;

    public PawnCharacter(String playerID, String imgFileName, int x, int y) {
        super(playerID, imgFileName, x, y);
    }

    public PawnCharacter(String playerID, String imgFileName) {
        this(playerID, imgFileName, 0, 0);
    }

    public void moveToTileIndex(int index) {
        this.currentTileIndex = index;
        System.out.println("Moving pawn [" + this.getNetworkId() + "] to tile " + index);
    }

    public int getCurrentTileIndex() {
        return currentTileIndex;
    }

    public void setCurrentTileIndex(int index) {
        this.currentTileIndex = index;
    }
}
