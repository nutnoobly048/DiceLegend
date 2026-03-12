package Gameplay;


import misc.Player;
import misc.PawnCharacter;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameState {

    public static GameState currentGame;
    public String selectedMapId = "mysteriousJungle";

    public boolean isHost;
    public String lobbyName;
    public String currentPlayerTurnId = "";

    public final HashMap<String, Player> allPlayers = new LinkedHashMap<>();
    public final HashMap<String, PawnCharacter> spawnedCharacter = new HashMap<>();


    public enum GamePhase {
        WAIT_FOR_PLAYERS,
        WAIT_FOR_READY,
        TURN_START,
        WAIT_FOR_ROLL,
        EXECUTE_MOVEMENT,
        CHECK_TILE,
        WAIT_FOR_TARGET,
        EXECUTE_ACTION,
        TURN_END,
        GAME_END
    }

    public enum TriggerEvent {
        PLAYER_JOINED, PLAYER_LEFT, PLAYER_SPRITE_CHANGE, GAME_START, PLAYER_READY, DICE_ROLL_EVENT
    }

    public GamePhase currentPhase = GamePhase.WAIT_FOR_PLAYERS;
    public GamePhase previousPhase = GamePhase.WAIT_FOR_PLAYERS;

    public boolean hasRolledDiceThisTurn = false;
    public boolean isAnimatingMovement = false;

    public GameState(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        currentGame = this;
    }


    public void handleEvent(TriggerEvent event, String[] params) {
        switch (currentPhase) {
            case WAIT_FOR_PLAYERS -> handleWaitForPlayers(event, params);
            case WAIT_FOR_READY -> handleWaitForReady(event, params);
            case TURN_START -> handleTurnStart(event, params);
            case WAIT_FOR_ROLL -> handleWaitForRoll(event, params);
            case EXECUTE_MOVEMENT -> {
            }
            case CHECK_TILE -> {}
            case WAIT_FOR_TARGET -> {}
            case EXECUTE_ACTION -> {}
            case TURN_END -> {}
            case GAME_END -> {}
            default -> System.out.println("Unhandled phase: " + currentPhase);
        }
    }

    public void handleEvent(TriggerEvent event) {
        handleEvent(event, null);
    }

    private void handleWaitForPlayers(TriggerEvent event, String[] params) {
        switch (event) {
            case PLAYER_JOINED -> onPlayerJoined(params);
            case PLAYER_LEFT -> onPlayerLeft(params);
            case GAME_START -> {
                setAllPlayersUnreadyToContinue();
                changeStateTo(GamePhase.WAIT_FOR_READY);
            }
            case PLAYER_SPRITE_CHANGE -> {
                allPlayers.get(params[0]).changeSpriteName(params[1]);
            }
        }
    }

    private void handleWaitForReady(TriggerEvent event, String[] params) {
        if (event != TriggerEvent.PLAYER_READY)
            return;
        String id = params[0];
        allPlayers.get(id).setReadyToContinue(true);
        System.out.println(id + " is ready to continue");

        if (isAllPlayersReadyToContinue()) {
            setAllPlayersUnreadyToContinue();
            switch (previousPhase) {
                case WAIT_FOR_PLAYERS -> changeStateTo(GamePhase.TURN_START);
            }
        }
    }

    private void handleTurnStart(TriggerEvent event, String[] params) {

    }

    private void handleWaitForRoll(TriggerEvent event, String[] params) {
    }

    private void onPlayerJoined(String[] params) {
        if (params == null || params.length < 2)
            return;

        String id = params[0];
        String name = params[1];

        allPlayers.putIfAbsent(id, new Player(id, name));

        System.out.println("PLAYER JOINED: " + name + " [" + id + "]");
        for (Player player : GameState.currentGame.allPlayers.values()) {
            System.out.println(player.getName());
        }
    }

    private void onPlayerLeft(String[] params) {
        if (params == null || params.length < 1)
            return;

        String id = params[0];
        allPlayers.remove(id);

        System.out.println("PLAYER LEFT: " + id);
    }

    public boolean isAllPlayersReadyToContinue() {
        for (Player p : allPlayers.values()) {
            if (!p.isReadyToContinue())
                return false;
        }
        return true;
    }

    public void setAllPlayersUnreadyToContinue() {
        for (Player p : allPlayers.values()) {
            p.setReadyToContinue(false);
        }
    }

    public boolean isAllPlayersReadyToPlay() {
        for (Player p : allPlayers.values()) {
            if (!p.isReadyToPlay())
                return false;
        }
        return true;
    }

    public void setAllPlayersUnreadyToPlay() {
        for (Player p : allPlayers.values()) {
            p.setReadyToPlay(false);
        }
    }

    public void changeStateTo(GamePhase newPhase) {
        if (newPhase != GamePhase.WAIT_FOR_READY) {
            previousPhase = currentPhase;
        }
        currentPhase = newPhase;
        System.out.println("Phase -> " + newPhase);
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
