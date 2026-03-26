package Gameplay;

import misc.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LobbyState {

    public static LobbyState current;

    public final String lobbyName;
    public final boolean isHost;


    public String selectedMapId;

    public final HashMap<String, Player> allPlayers = new LinkedHashMap<>();

    public enum TriggerEvent {
        PLAYER_JOINED, PLAYER_LEFT, PLAYER_SPRITE_CHANGE
    }

    public static final HashMap<String, String[]> spriteEntries = new HashMap<>(Map.ofEntries(
            Map.entry("YELLOW", new String[]{"walk_01.png", "walk_02.png"}),
            Map.entry("BLACK", new String[]{"idle.png", "cast.png"}),
            Map.entry("BLUE", new String[]{"shoot.png", ""}),
            Map.entry("GREEN", new String[]{"", ""})
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

    public void handleEvent(TriggerEvent event, String[] params) {
        switch (event) {
            case PLAYER_JOINED -> onPlayerJoined(params);
            case PLAYER_LEFT   -> onPlayerLeft(params);
            case PLAYER_SPRITE_CHANGE -> {
                Player p = allPlayers.get(params[0]);
                if (p != null) p.changeSpriteName(params[1]);
            }
        }
    }

    private void onPlayerJoined(String[] params) {
        if (params == null || params.length < 2) return;

        String id   = params[0];
        String name = params[1];

        allPlayers.putIfAbsent(id, new Player(id, name));
        System.out.println("PLAYER JOINED: " + name + " [" + id + "]");
        for (Player p : allPlayers.values()) {
            System.out.println(p.getName());
        }
    }

    private void onPlayerLeft(String[] params) {
        if (params == null || params.length < 1) return;

        String id = params[0];
        allPlayers.remove(id);
        System.out.println("PLAYER LEFT: " + id);
    }

    public String getSelectedMapId() {
        return selectedMapId;
    }

    public void setSelectedMapId(String selectedMapId) {
        this.selectedMapId = selectedMapId;
    }
}
