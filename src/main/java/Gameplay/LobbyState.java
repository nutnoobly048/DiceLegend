package Gameplay;

import misc.Player;

import java.lang.reflect.Array;
import java.util.*;

public class LobbyState {

    public static LobbyState current;

    public final String lobbyName;
    public final boolean isHost;


    public String selectedMapId;

    public final HashMap<String, Player> allPlayers = new LinkedHashMap<>();

    private Runnable onPlayerListChanged;

    public enum TriggerEvent {
        PLAYER_JOINED, PLAYER_LEFT, PLAYER_SPRITE_CHANGE
    }

    private ArrayList<String> spriteOrder = new ArrayList<String>(Arrays.asList("YELLOW", "BLACK", "BLUE", "GREEN"));

    public static final HashMap<String, String[]> spriteEntries = new HashMap<>(Map.ofEntries(
            Map.entry("YELLOW", new String[]{"YellowCharacter.png", "YellowFace.png"}),
            Map.entry("BLACK", new String[]{"BlackCharacter.png", "BlackFace.png"}),
            Map.entry("BLUE", new String[]{"BlueCharacter.png", "BlueFace.png"}),
            Map.entry("GREEN", new String[]{"GreenCharacter.png", "GreenFace.png"})
    ));

    public LobbyState(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        current = this;
        this.selectedMapId = "cryoGard";
    }

    public GameState createMatch() {
        return new GameState(this);
    }

    public static void destroy() {
        current = null;
    }

    public void setOnPlayerListChanged(Runnable callback) {
        this.onPlayerListChanged = callback;
    }

    private void notifyPlayerListChanged() {
        if (onPlayerListChanged != null) {
            onPlayerListChanged.run();
        }
    }

    public void handleEvent(TriggerEvent event, String[] params) {
        switch (event) {
            case PLAYER_JOINED -> onPlayerJoined(params);
            case PLAYER_LEFT   -> onPlayerLeft(params);
            case PLAYER_SPRITE_CHANGE -> {
                Player player = allPlayers.get(params[0]);
                String[] imgLoad = spriteEntries.get(params[1]);
                if (player != null) {
                    System.out.println("Changed to: " + imgLoad[0]);
                    player.changeSpriteName(imgLoad[0]);
                    System.out.println("Changed to: " + imgLoad[1]);
                    player.changeLocalSpritePortraitName(imgLoad[1]);
                }
            }
        }
    }

    int count = 0;

    private void onPlayerJoined(String[] params) {
        if (params == null || params.length < 2) return;

        String id   = params[0];
        String name = params[1];

        if (allPlayers.containsKey(id)) {
            return;
        }
        String spriteKey = spriteOrder.get(allPlayers.size());
        String[] sprite = spriteEntries.get(spriteKey);

        Player player = new Player(id, name);
        player.changeSpriteName(sprite[0]);
        player.changeLocalSpritePortraitName(sprite[1]);
        allPlayers.putIfAbsent(id, player);
        for (Player p : allPlayers.values()) {
            System.out.println(p.getName());
        }

        notifyPlayerListChanged();


    }

    private void onPlayerLeft(String[] params) {
        if (params == null || params.length < 1) return;

        String id = params[0];
        allPlayers.remove(id);
        System.out.println("PLAYER LEFT: " + id);

        notifyPlayerListChanged();
    }

    public String getSelectedMapId() {
        return selectedMapId;
    }

    public void setSelectedMapId(String selectedMapId) {
        this.selectedMapId = selectedMapId;
    }
}
