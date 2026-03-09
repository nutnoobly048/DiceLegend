package service;

import Gameplay.GameState;
import misc.Player;

import static service.RunService.mqtt;

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
                broadcastResult("PLAYER_JOINED", senderID, params[0]);

                GameState.currentGame.allPlayers.forEach((id, p) -> sendResultTo(senderID, "PLAYER_JOINED", id, p.getName()));
            }
            case "LEAVE_GAME" -> broadcastResult("PLAYER_LEFT", senderID);

            case "START_GAME" -> {
                if (isFromHost) {
                    broadcastResult("GAME_STARTED");
                } else {
                    System.err.println("Access Denied: " + senderID + " tried to start game.");
                }
            }
            case "CONTINUE" -> {
                if (GameState.currentGame.allPlayers.get(senderID) != null) {
                    broadcastResult("CONTINUE", senderID);
                }
            }
            case "TESTPRINT" -> System.out.println("PRINT");
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


    //INTENT
    public static void intent(String input) {
        String topic = "DiceLegend/" + GameState.currentGame.getLobbyName() + "/Intents";
        String[] packetList = input.split(":");

        if (packetList.length >= 2 && packetList[1].equals("SELF")) {
            packetList[1] = Player.getLocalPlayerId();
        }

        input = String.join(":", packetList);
        mqtt.publish(topic, input);
        System.out.println("[DEBUG] Sent intent with topics "+ ": " + input);
    }

    //RESULT
    private static void broadcastResult(String act, String... p) {
        String packet = "RESULT:ALLCLIENTS:" + act + (p.length > 0 ? ":" + String.join(":", p) : "");
        mqtt.publish("DiceLegend/" + GameState.currentGame.getLobbyName() + "/Results", packet);
    }

    private static void sendResultTo(String target, String act, String... p) {
        String packet = "RESULT:" + target + ":" + act + (p.length > 0 ? ":" + String.join(":", p) : "");
        mqtt.publish("DiceLegend/" + GameState.currentGame.getLobbyName() + "/Results", packet);
    }
}