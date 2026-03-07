package service;

import Gameplay.GameState;
import misc.Player;

public class CommandHandler {

    //ONLY HOST CAN USE THIS METHOD
    public static void handleIntent(String senderID, String action, String[] params) {
        if (GameState.currentGame == null || !GameState.currentGame.isHost) return;

        boolean isSystemAction = action.equals("JOIN_GAME") || action.equals("LEAVE_GAME") || action.equals("CONTINUE");

        if (!isSystemAction) {
            Player sender = GameState.currentGame.allPlayers.get(senderID);
           if (sender == null) return;

          if (!sender.isOpenForNetworkInput()) {
                System.out.println(senderID + " is locked. Blocking: " + action);
                return;
            }
        }

        boolean isFromHost = senderID.equals(Player.getLocalPlayerId());



        switch (action) {
            case "JOIN_GAME" -> {
                broadcast("PLAYER_JOINED", senderID, params[0]);

                GameState.currentGame.allPlayers.forEach((id, p) -> sendTo(senderID, "PLAYER_JOINED", id, p.getName()));
            }
            case "LEAVE_GAME" -> broadcast("PLAYER_LEFT", senderID);

            case "START_GAME" -> {
                if (isFromHost) {
                    broadcast("GAME_STARTED");
                } else {
                    System.err.println("Access Denied: " + senderID + " tried to start game.");
                }
            }
            case "CONTINUE" -> {
                if (GameState.currentGame.allPlayers.get(senderID) != null) {
                    broadcast("CONTINUE", senderID);
                }
            }
        }

    }


    //BOTH CLIENTS AND HOST CAN USE THIS METHOD
    public static void handleResult(String targetID, String action, String[] params) {
        if (GameState.currentGame == null) return;

        if (!targetID.equals("ALLCLIENTS") && !targetID.equals(Player.getLocalPlayerId())) return;

        switch (action) {
            case "PLAYER_JOINED" -> GameState.currentGame.handleEvent(GameState.TriggerEvent.PLAYER_JOINED, params);
            case "PLAYER_LEFT"   -> GameState.currentGame.handleEvent(GameState.TriggerEvent.PLAYER_LEFT, params);
            case "GAME_STARTED"  -> GameState.currentGame.handleEvent(GameState.TriggerEvent.GAME_START, null);
            case "CONTINUE" -> GameState.currentGame.handleEvent(GameState.TriggerEvent.PLAYER_READY, params);
        }
    }

    private static void broadcast(String act, String... p) {
        RunService.resultQueue.add("RESULT:ALLCLIENTS:" + act + (p.length > 0 ? ":" + String.join(":", p) : ""));
    }

    private static void sendTo(String target, String act, String... p) {
        RunService.resultQueue.add("RESULT:" + target + ":" + act + (p.length > 0 ? ":" + String.join(":", p) : ""));
    }
}