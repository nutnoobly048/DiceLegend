package misc;

public class Player {

    private String networkID;
    private String name;

    private boolean isReadyToPlay;
    private boolean isReadyToContinue;
    private boolean openForNetworkInput = true;

    private int remainingSkipTurns = 0;

    public static Player localPlayer;

    public Player(String id, String name) {
        this.networkID = id;
        this.name = name;
    }

    public Player(String id) {
        this(id, id);
    }

    public static String getLocalPlayerId() {
        return localPlayer != null ? localPlayer.getNetworkID() : null;
    }

    public static void setLocalPlayerId(String myLocalId) {
        if (localPlayer == null) {
            localPlayer = new Player(myLocalId, "TESTIFICATE");
        } else {
            localPlayer.networkID = myLocalId;
        }
    }
    public boolean isSkipped() {
        return this.remainingSkipTurns > 0;
    }

    public void increaseSkipTurns(int amount) {
        this.remainingSkipTurns += amount;
    }

    public void decreaseSkipTurns(int amount) {
        this.remainingSkipTurns = Math.max(0, this.remainingSkipTurns - amount);
    }

    public int getRemainingSkipTurns() {
        return this.remainingSkipTurns;
    }

    public boolean isReadyToContinue() { return this.isReadyToContinue; }
    public void setReadyToContinue(boolean ready) { this.isReadyToContinue = ready; }

    public boolean isReadyToPlay() { return this.isReadyToPlay; }
    public void setReadyToPlay(boolean ready) { this.isReadyToPlay = ready; }


    public boolean isOpenForNetworkInput() { return openForNetworkInput; }
    public void setOpenForNetworkInput(boolean open) { this.openForNetworkInput = open; }

    public String getNetworkID() { return networkID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
