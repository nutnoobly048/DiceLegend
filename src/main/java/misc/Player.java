package misc;

import graphicsUtilities.SceneUtilities;
import objectClass.VisualObject;
import service.UserInput;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

//A Central class to add Instance
public class Player {

    private String networkID;
    private String name;

    private boolean isReadyToPlay;
    private boolean isReadyToContinue;

    private int remainingSkipTurns = 0;

    private static HashMap<String, Player> playerList = new HashMap<>();

    public Player(String id, String name) {
        this.networkID = id;
        this.name = name;

        playerList.put(this.networkID, this);
    }

    public Player(String id) {
        this(id,id);
    }

    public static HashMap<String,Player> getPlayerList() {
        return playerList;
    }

    public static boolean isAllPlayerReadyToPlay() {
        for (Player player : playerList.values()) {
            if (!player.isReadyToPlay()) {
                return false;
            }
        }
        return true;
    }
    public static boolean isAllPlayerReadyToContinue() {
        for (Player player : playerList.values()) {
            if (!player.isReadyToContinue()) {
                return false;
            }
        }
        return true;
    }
    public static void setAllPlayerUnreadyToPlay() {
        for (Player player : playerList.values()) {
            player.setReadyToPlay(false);
        }
    }
    public static void setAllPlayerUnreadyToContinue() {
        for (Player player : playerList.values()) {
            player.setReadyToPlay(false);
        }
    }


    public boolean isSkipped() {
        return this.remainingSkipTurns > 0;
    }

    public void increaseSkipTurns(int i ) {
        this.remainingSkipTurns += i;
    }

    public void decreaseSkipTurns(int i) {
        this.remainingSkipTurns -= 1;
    }
    public int getRemainingSkipTurns() {
        return this.remainingSkipTurns;
    }

    public boolean isReadyToContinue() {
        return this.isReadyToContinue;
    }

    public void setReadyToContinue(boolean readyToContinue) {
        this.isReadyToContinue = readyToContinue;
    }

    public boolean isReadyToPlay() {
        return this.isReadyToPlay;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.isReadyToPlay = readyToPlay;
    }

    public String getNetworkID() {
        return networkID;
    }
}
