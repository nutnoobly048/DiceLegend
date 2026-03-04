package Gameplay;

import graphicsUtilities.Scene;
import misc.Player;
import misc.PawnCharacter;
import java.util.HashMap;

public class GameState {

    //STATIC KEYWORD
    public static String selectedMapId;
    private static int maxPlayerAllowed;

    public boolean isHost;
    public String localPlayerNetworkId = "";
    public String currentPlayerTurnId = "";



    public final HashMap<String, Player> allPlayers = new HashMap<>();
    public final HashMap<String, PawnCharacter> allPawnCharacters = new HashMap<>();

    public enum GamePhase {
        WAIT_FOR_PLAYERS, TURN_START, WAIT_FOR_ROLL, EXECUTE_MOVEMENT,
        WAIT_FOR_READY, CHECK_TILE, WAIT_FOR_TARGET, EXECUTE_ACTION,
        TURN_END, GAME_END
    }

    public enum TriggerEvent {
        PLAYER_JOINED, PLAYER_LEFT, GAME_START, PLAYER_READY, DICE_ROLL_EVENT,
    }

    public GamePhase currentPhase = GamePhase.WAIT_FOR_PLAYERS;

    public boolean hasRolledDiceThisTurn = false;
    public boolean isAnimatingMovement = false;

    public Scene gameMap = null;

    public GameState(boolean isHost, String selectedMapID) {
        this.isHost = isHost;
    }

    public void handleEvent(TriggerEvent event, Object payload) {
        if (!isHost) return;

        switch (currentPhase) {
            case WAIT_FOR_PLAYERS -> handleWaitForPlayers(event, payload);
            case TURN_START -> handleTurnStart(event, payload);
            case WAIT_FOR_ROLL -> handleWaitForRoll(event, payload);
            default -> System.out.println("Unhandled phase: " + currentPhase);
        }
    }

    private void handleWaitForPlayers(TriggerEvent event, Object payload) {
        if (event == TriggerEvent.PLAYER_JOINED && payload instanceof String id) {
            if (!allPlayers.containsKey(id)) {
                Player newPlayer = new Player(id);
                allPlayers.put(id, newPlayer);
                allPawnCharacters.put(id, new PawnCharacter(newPlayer.getNetworkID(), "blank", 0, 0));
                System.out.println("Player joined: " + id);
            } else {
                System.err.println("PLAYER WITH THIS ID EXISTED");
            }
        }
        else if (event == TriggerEvent.PLAYER_LEFT && payload instanceof String id) {
            if (allPlayers.remove(id) != null) {
                allPawnCharacters.remove(id);
                System.out.println("Player left: " + id);
            } else {
                System.err.println("PLAYER WITH THIS ID NEVER EXISTED");
            }
        }
        else if (event == TriggerEvent.GAME_START) {
            changeStateTo(GamePhase.TURN_START);
        }
    }

    private void handleTurnStart(TriggerEvent event, Object payload) {
    }

    private void handleWaitForRoll(TriggerEvent event, Object payload) {
    }

    public void handleEvent(TriggerEvent event) {
        handleEvent(event, null);
    }

    public void changeStateTo(GamePhase newPhase) {
        onStateExited(currentPhase);
        currentPhase = newPhase;
        onStateEnter(currentPhase);
    }

    private void onStateEnter(GamePhase phase) {
        System.out.println("Entering State: " + phase);
    }

    private void onStateExited(GamePhase phase) {
        System.out.println("Exiting State: " + phase);
    }
}