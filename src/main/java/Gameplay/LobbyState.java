package Gameplay;

import misc.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class LobbyState {

    public static LobbyState current;

    public final String lobbyName;
    public final boolean isHost;
    public final HashMap<String, Player> allPlayers = new LinkedHashMap<>();

    public LobbyState(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        current = this;
    }

    public GameState createMatch() {
        return new GameState(this);
    }

    public static void destroy() {
        current = null;
    }
}
