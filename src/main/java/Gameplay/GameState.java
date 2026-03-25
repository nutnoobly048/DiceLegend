package Gameplay;


import Item.Item;
import ServiceInterface.CellAttribute;
import graphicsUtilities.SceneUtilities;
import misc.Player;
import misc.PawnCharacter;
import objectClass.Board;
import objectClass.GameModal;
import scene.LobbyScene;
import service.CommandHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import Item.DoubleDiceItem;
import Event.Event;
import Event.ReverseEvent;
import service.RunService;

import javax.swing.*;


//AKA ห้องเกม (Match)
public class GameState {

    public static GameState currentGame;
    public String selectedMapId = "mysteriousJungle";

    public boolean isHost;
    public String lobbyName;
    public String currentPlayerTurnId = "";

    public int lastRollResult = 0;
    public int currentPlayerTurnIndex = 0; // tracks index of players

    public Board gameBoard;

    public Event currentEvent;
    public Item currentItem;

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
        ON_PHASE_ENTER, //ใช้แค่ตอนเปลี่ยน State
        PLAYER_JOINED, PLAYER_LEFT, PLAYER_SPRITE_CHANGE, GAME_START, PLAYER_READY, DICE_ROLL_EVENT, SET_TARGET
    }

    public GamePhase currentPhase = GamePhase.WAIT_FOR_PLAYERS;
    public GamePhase previousPhase = GamePhase.WAIT_FOR_PLAYERS;


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
            case EXECUTE_MOVEMENT -> handleExecuteMovement(event, params);
            case CHECK_TILE -> handleCheckTile(event, params);
            case WAIT_FOR_TARGET -> handleWaitForTarget(event, params); // รอเป้าหมายจาก currentPlayerTurnID
            case EXECUTE_ACTION -> handleExecuteAction(event, params); //เมื่อได้รับให้เรียกใช้ Item.UseItem()
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
                CommandHandler.sentIntent("INTENT:SELF:CHANGESCENETO:" + GameState.currentGame.selectedMapId);
                switch (selectedMapId) {
                    case "mysteriousJungle" -> gameBoard = new Board(
                            Board.coordinatesMysteriousJungle,
                            Board.destinationMysteriousJungle,
                            Board.itemTileMysteriousJungle,
                            Board.eventTileMysteriousJungle);

                    case "cryoGard" -> {}
                    case "goldenSeason" -> {}
                }
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
        if (!isHost) return;
        if (event != TriggerEvent.ON_PHASE_ENTER) return;
        for (PawnCharacter character : spawnedCharacter.values()) {
            character.setCurrentTileIndex(0);
        }

        currentPlayerTurnIndex = 0;
        currentPlayerTurnId = getPlayerIdByTurnIndex(currentPlayerTurnIndex);

        CommandHandler.broadcastResult("UIEVENT", "PLAYERTURN", currentPlayerTurnId);
        beginTurnForPlayer(currentPlayerTurnId);
    }

    private void handleWaitForRoll(TriggerEvent event, String[] params) {
        if (!isHost) return; //test code
        if (event != TriggerEvent.DICE_ROLL_EVENT) return;

        String playerId = params[0];
        int roll = Integer.parseInt(params[1]);

        allPlayers.get(playerId).setOpenForNetworkInput(false);

        lastRollResult = roll;
        int rollValue = lastRollResult + (currentEvent != null ? currentEvent.getRollValueModifier() : 0);


        PawnCharacter pawn = spawnedCharacter.get(playerId);
        int currentIndex = pawn.getCurrentTileIndex();
        int rawDestination = Math.min(currentIndex + rollValue, gameBoard.getBoardSize() - 1);
        CommandHandler.broadcastResult("MOVETO", playerId, String.valueOf(rawDestination));

        int finalDestination = gameBoard.getDestinationFromIndex(rawDestination);

        //if current cell has destination
        if (finalDestination != rawDestination) {
            CommandHandler.broadcastResult("MOVETO", playerId, String.valueOf(finalDestination));
        }



        setAllPlayersUnreadyToContinue();
        changeStateTo(GamePhase.EXECUTE_MOVEMENT);
    }

    private void handleExecuteMovement(TriggerEvent event, String[] params) {
        if (event != TriggerEvent.PLAYER_READY) return;

        String id = params[0];
        allPlayers.get(id).setReadyToContinue(true);

        if (isAllPlayersReadyToContinue()) {
            setAllPlayersUnreadyToContinue();
            changeStateTo(GamePhase.CHECK_TILE);
        }
    }

    private void handleCheckTile(TriggerEvent event, String[] params) {
        if (!isHost) return;

        PawnCharacter currentPawn = GameState.currentGame.spawnedCharacter.get(currentPlayerTurnId);
        int currentIndex = currentPawn.getCurrentTileIndex();


        //Tile Type Check
        switch (gameBoard.getAttributeFromIndex(currentIndex)) {
            case WIN_TILE -> {

                for (Player player : currentGame.allPlayers.values()) { // Host will ignore messeage form other player

                    player.setOpenForNetworkInput(false);

                }

                GameModal winAlert = new GameModal(100, 100, "licoCake.png");
                winAlert.setVisible(true);

                int delay = 5000;
                Timer timer = new Timer(delay, e -> {
                    winAlert.setVisible(false);
                    SceneUtilities.changeSceneTo(SceneList.lobbyScene);
                    currentGame.spawnedCharacter.clear();
                });

                timer.setRepeats(false);
                timer.start();

                System.out.println("Winner -> " + currentPawn.getNetworkId());

            }

            case EVENT_TILE -> {
                Event selectedEvent = new ReverseEvent(); //แทนที่ด้วย randomEvent() ในภายหลัง
                Event.useEvent(selectedEvent, GameState.currentGame);

                currentEvent = selectedEvent;

                setAllPlayersUnreadyToContinue();
                changeStateTo(GamePhase.EXECUTE_ACTION);
            }
            case ITEM_TILE -> {
                Item selectedItem = new DoubleDiceItem(); //แทนที่ด้วย randomItem() ในภายหลัง

                if (selectedItem.isRequireTarget()) {
                    allPlayers.get(currentPlayerTurnId).setOpenForNetworkInput(true);
                    currentItem = selectedItem;
                    changeStateTo(GamePhase.WAIT_FOR_TARGET);
                } else {
                    Player user = allPlayers.get(currentPlayerTurnId);

                    Item.useItem(selectedItem, user, user, this);
                    setAllPlayersUnreadyToContinue();
                    changeStateTo(GamePhase.EXECUTE_ACTION);
                }
            }
            case WATER_TILE -> {}
            default -> advanceToNextPlayer();
        }
        //advanceToNextPlayer();

    }

    private void handleWaitForTarget(TriggerEvent event, String[] params) {
        if (!isHost) return;
        if (event != TriggerEvent.SET_TARGET) return;

        String id = params[0];
        Player targetPlayer = allPlayers.get(id);
        Player userPlayer   = allPlayers.get(currentPlayerTurnId);

        Item.useItem(currentItem, userPlayer, targetPlayer, this);
        currentItem = null;

        setAllPlayersUnreadyToContinue();
        changeStateTo(GamePhase.EXECUTE_ACTION);
    }

    private void handleExecuteAction(TriggerEvent event, String[] params) {
        if (event != TriggerEvent.PLAYER_READY) return;

        String id = params[0];
        allPlayers.get(id).setReadyToContinue(true);

        if (isAllPlayersReadyToContinue()) {
            setAllPlayersUnreadyToContinue();
            advanceToNextPlayer();

        }
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
        handleEvent(TriggerEvent.ON_PHASE_ENTER);
    }

    public String getPlayerIdByTurnIndex(int index) {
        List<String> keys = new ArrayList<>(allPlayers.keySet());
        return keys.get(index % keys.size());
    }

    private void beginTurnForPlayer(String playerId) {
        Player player = allPlayers.get(playerId);

        if (player.isSkipped()) {
            player.decreaseSkipTurns(1);
            advanceToNextPlayer();
            return;
        }

        for (Player p : allPlayers.values()) {
            p.setOpenForNetworkInput(false);
        }
        player.setOpenForNetworkInput(true);

        setAllPlayersUnreadyToContinue();
        CommandHandler.broadcastResult("UIEVENT", "WAITFOR", playerId);

        changeStateTo(GamePhase.WAIT_FOR_ROLL);
    }

    private void advanceToNextPlayer() {
        Player currentPlayer = allPlayers.get(currentPlayerTurnId);

        if (currentEvent != null) {
            if (currentEvent.remainingTurn <= 0) {
                currentEvent.onEventLeave(this);
                currentEvent = null;
            } else {
                currentEvent.remainingTurn--;
            }
        }

        if (currentPlayer != null && currentPlayer.hasExtraTurns()) {
            System.out.println(currentPlayer.hasExtraTurns());
            currentPlayer.decreaseExtraTurns(1);
            System.out.println("[EXTRA TURN] " + currentPlayer.getName() + " gets an extra turn! Remaining: " + currentPlayer.getExtraTurns());

            beginTurnForPlayer(currentPlayerTurnId);
            return;
        }

        currentPlayerTurnIndex = (currentPlayerTurnIndex + 1) % allPlayers.size();
        currentPlayerTurnId = getPlayerIdByTurnIndex(currentPlayerTurnIndex);

        beginTurnForPlayer(currentPlayerTurnId);
    }
    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
}
