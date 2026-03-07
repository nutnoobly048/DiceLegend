package misc;

<<<<<<< Updated upstream
import java.util.HashMap;

public class Player {

    private String networkID;
    private String name;

    private boolean isReadyToPlay;
    private boolean isReadyToContinue;
    private boolean openForNetworkInput = true;

    private int remainingSkipTurns = 0;

    public static Player localPlayer;

    private static HashMap<String, Player> playerList = new HashMap<>();

    public Player(String id, String name) {
        this.networkID = id;
        this.name = name;
        playerList.put(this.networkID, this);
    }

    public Player(String id) {
        this(id, id);
    }

    public static HashMap<String, Player> getPlayerList() {
        return playerList;
    }

    public static String getLocalPlayerId() {
        return localPlayer != null ? localPlayer.getNetworkID() : null;
    }

    public static void setLocalPlayerId(String myLocalId) {
        if (localPlayer == null) {
            localPlayer = new Player(myLocalId, "TESTIFICATE");
        } else {
            playerList.remove(localPlayer.getNetworkID());
            localPlayer.networkID = myLocalId;
            playerList.put(myLocalId, localPlayer);
        }
    }

    public static boolean isAllPlayerReadyToPlay() {
        for (Player player : playerList.values()) {
            if (!player.isReadyToPlay()) return false;
        }
        return true;
    }

    public static boolean isAllPlayerReadyToContinue() {
        for (Player player : playerList.values()) {
            if (!player.isReadyToContinue()) return false;
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
            // FIXED: This was accidentally setting ReadyToPlay(false) before
            player.setReadyToContinue(false);
        }
    }

    public boolean isSkipped() {
        return this.remainingSkipTurns > 0;
    }

    public void increaseSkipTurns(int i) {
        this.remainingSkipTurns += i;
    }

    public void decreaseSkipTurns(int i) {
        // FIXED: This was ignoring 'i' and always subtracting 1
        this.remainingSkipTurns -= i;

        // Safety check so we don't get negative skip turns
        if (this.remainingSkipTurns < 0) {
            this.remainingSkipTurns = 0;
        }
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

    public boolean isOpenForNetworkInput() {
        return openForNetworkInput;
    }

    public void setOpenForNetworkInput(boolean openForNetworkInput) {
        this.openForNetworkInput = openForNetworkInput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
=======
import graphicsUtilities.Scene;
import graphicsUtilities.SceneList;
import graphicsUtilities.SceneUtilities;
import objectClass.VisualObject;
import service.UserInput;

import java.awt.event.KeyEvent;

public class Player extends VisualObject {
    public Player(String imgFileName) {
        super(imgFileName);
    }
    public Player(String imgFileName, int x, int y) {
        super(imgFileName,x,y);
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene()) return;

        if (UserInput.isKeyHold(KeyEvent.VK_D)) {
            x += (int) (300 * deltaTime); //ให้ขยับ 300 Pixel ต่อวินาที
            SceneUtilities.changeSceneTo(SceneUtilities.scene2);
        }
        if (UserInput.isKeyJustPressed(KeyEvent.VK_A)) {
            x -= (int) (300 * deltaTime);

        }
        if (UserInput.isKeyHold(KeyEvent.VK_W)) {
            y -= (int) (300 * deltaTime);
        }
        if (UserInput.isKeyHold(KeyEvent.VK_S)) {
            y += (int) (300 * deltaTime);
        }
    }
}
>>>>>>> Stashed changes
