package Gameplay;

import graphicsUtilities.SceneUtilities;
import misc.Player;
import misc.PawnCharacter;
import service.CommandHandler;
import service.RunService;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class GameState {

    //STATIC KEYWORD
    public static GameState currentGame;
    public static String selectedMapId = "Garden"; //example
    private static int maxPlayerAllowed;

    public boolean isHost;
    public String localPlayerNetworkId = "";
    public String currentPlayerTurnId = "";
    public String lobbyName;




    public final HashMap<String, Player> allPlayers = new LinkedHashMap<>();
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
    public GamePhase previousPhase = GamePhase.WAIT_FOR_PLAYERS;

    public boolean hasRolledDiceThisTurn = false;
    public boolean isAnimatingMovement = false;


    public GameState(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        String baseTopic = "DiceLegend/" + lobbyName;

        new Thread(() -> {
            try {
                if (isHost) {
                    RunService.mqtt.connectWithWill(baseTopic, "HOST_DISCONNECTED");
                    RunService.mqtt.subscribe(baseTopic + "/Intents", (topic, message) -> {
                        RunService.intentQueue.add(message);
                    });
                } else {
                    RunService.mqtt.connect();
                }
                RunService.mqtt.subscribe(baseTopic + "/Results", (topic, message) -> {
                    RunService.resultQueue.add(message);
                });
                CommandHandler.intent("INTENT:" + Player.getLocalPlayerId() + ":JOIN_GAME:" + "PlayerName");

                System.out.println("Network Ready for " + (isHost ? "Host" : "Client"));
            } catch (Exception e) {
                System.err.println("Connection Failed: " + e.getMessage());
            }
        }).start();

        currentGame = this;
    }

    public void handleEvent(TriggerEvent event, String[] payload) {

        switch (currentPhase) {
            case WAIT_FOR_PLAYERS -> handleWaitForPlayers(event, payload);
            case TURN_START -> handleTurnStart(event, payload);
            case WAIT_FOR_ROLL -> handleWaitForRoll(event, payload);
            case EXECUTE_MOVEMENT -> {}
            case WAIT_FOR_READY -> handleWaitForReady(event, payload);
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
            case PLAYER_LEFT   -> onPlayerLeft(params);
            case GAME_START -> {
                SceneUtilities.changeSceneTo(SceneList.buildMysteriousJungle());
                Player.setAllPlayerUnreadyToContinue();
                changeStateTo(GamePhase.WAIT_FOR_READY);
            }
        }
    }


    //wait for ready
    public void handleWaitForReady(TriggerEvent event, String[] params) {
        switch (event) {
            case PLAYER_READY -> {
                String id = params[0];
                GameState.currentGame.allPlayers.get(id).setReadyToContinue(true);

                System.out.println(id + " is ready to continue");

                if (Player.isAllPlayerReadyToContinue()) {
                    switch (previousPhase) {
                        case GamePhase.WAIT_FOR_PLAYERS -> changeStateTo(GamePhase.TURN_START);
                    }
                    Player.setAllPlayerUnreadyToContinue();
                }
            }
        }


    }

    private void handleTurnStart(TriggerEvent event, Object payload) {
    }

    private void handleWaitForRoll(TriggerEvent event, Object payload) {
    }



    public void changeStateTo(GamePhase newPhase) {
        onStateExited(currentPhase);
        currentPhase = newPhase;

        if (newPhase != GamePhase.WAIT_FOR_READY) {
            previousPhase = newPhase;
        }

        onStateEnter(currentPhase);
    }

    private void onStateEnter(GamePhase phase) {
        System.out.println("Entering State: " + phase);
    }

    private void onStateExited(GamePhase phase) {
        System.out.println("Exiting State: " + phase);
    }

    private void onPlayerJoined(String[] params) {
        if (params == null || params.length < 2) return;

        String id = params[0];
        String name = params[1];


        //Keep the character and Player model
        allPlayers.putIfAbsent(id, new Player(id, name));

        allPawnCharacters.putIfAbsent(id, new PawnCharacter(id, "blank.png", 0, 0));

        System.out.println("SESSION JOINED: " + id);
        System.out.println("PLAYER JOINED: " + name);
    }

    private void onPlayerLeft(String[] params) {
        if (params == null || params.length < 1) return;

        String id = params[0];
        allPlayers.remove(id);
        allPawnCharacters.remove(id);

        System.out.println("SESSION LEFT: " + id);
        System.out.println("PLAYER LEFT: " + id);
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}