package service;

import Gameplay.GameState;
import Gameplay.SceneList;
import graphicsUtilities.SceneUtilities;
import misc.PawnCharacter;
import misc.Player;

import static service.RunService.mqtt;
/*
 * วิธีการทำงานของ Network:
 *
 * Client ทำอะไรสักอย่าง → sendIntent("INTENT:playerID:ACTION:params")
 *      ↓ (host only รับ Intent)
 * handleIntent() ตัดสินใจว่าต้องทำอะไร
 *      ↓
 * broadcastResult() or sendResultTo() เพื่อส่งผลลัพธ์
 *      ↓ (ถึงทุกคนรวมถึง  Host)
 * handleResult() routes to GameState.handleEvent()
 *      ↓
 * GameState อัพเดต */

public class CommandHandler {

    //ONLY HOST CAN USE THIS METHOD   format: [id]:     action:      params[0]:params[1]:.....:params[N]
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
            case "CHANGE_SPRITE" -> {
                broadcastResult("PLAYER_SPRITE_CHANGED", senderID,params[0]);
            }
            case "CHANGESCENETO" -> {
                if (isFromHost) {
                    broadcastResult("CHANGESCENETO", params);
                }
            }
            case "ROLLEVENT" -> {
                int roll = (int)(Math.random() * 6) + 1;
                broadcastResult("DICE_ROLLED", senderID, String.valueOf(roll));
            }

            case "SETTARGET" -> {
                GameState.currentGame.handleEvent(GameState.TriggerEvent.SET_TARGET, params);
            }

            default -> System.out.println("PRINT MESSAGE");
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
            case "PLAYER_SPRITE_CHANGED" -> GameState.currentGame.handleEvent(GameState.TriggerEvent.PLAYER_SPRITE_CHANGE, params);

            case "CHANGESCENETO" -> SceneUtilities.changeSceneTo(params[0]);
            case "DICE_ROLLED" -> GameState.currentGame.handleEvent(GameState.TriggerEvent.DICE_ROLL_EVENT, params);
            case "MOVETO" -> {
                String playerId = params[0];
                int targetIndex = Integer.parseInt(params[1]);
                PawnCharacter pawn = GameState.currentGame.spawnedCharacter.get(playerId);
                //อย่าลืมส่ง INTENT:SELF:CONTINUE กลับมาหลังจากเสร็จ เนื่องจากต้องรอให้ทุกคน ขยับ เสร็จก่อน
                //ในบางครั้ง MOVETO จะถูกส่งมา 2 รอบ (เดินปกติ กับ ย้ายไปยังเป้าหมายของบันได / งู)
                 //เช็ค Logic ใน GameState บรรทัดที่ 140 - 150
                pawn.moveToTileIndex(targetIndex); //เป็น mockup เฉยๆ
            }


            //ตอน BroadcastResult ==> BroadcastResult("UIEVENT", "WAITFOR", "player2")
            //ตอนรับ UIEVENT:params[0]:params[1]:....:params[n] เช่น UIEVENT:WAITFOR:player2
            //ไปดูวิธีการใช้ broadcastResult
            case "UIEVENT" -> {

            }
        }
    }


    //INTENT                 INTENT:[ID]:action:params[0]:params[1]:.....:params[N] format
    public static void sentIntent(String input) {
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
    //Fun Fact : นี่คือ varargs method (Variable Arguments Method) เป็น method ที่รับ argument ได้เรื่อยๆ โดยจะแปลงข้อมูลที่รับเข้ามาเรื่อยๆเป็น array เช่น
    //broadcastResult("CONTINUE");                      act = "CONTINUE"    p = []
    //broadcastResult("CONTINUE", "player1");           act = "CONTINUE"    p = ["player1"]
    //broadcastResult("MOVETO", "player1", "5");        act = "MOVETO"      p = ["player1", "5"]
    //broadcastResult("N", "W", "O", "R", "D");         act = "N"           p = ["W", "O", "R", "D"]
    //broadcastResult ใช้ตอนให้ทุกคน update
    public static void broadcastResult(String act, String... p) {
        String packet = "RESULT:ALLCLIENTS:" + act + (p.length > 0 ? ":" + String.join(":", p) : "");
        mqtt.publish("DiceLegend/" + GameState.currentGame.getLobbyName() + "/Results", packet);
    }
    //sendResultTo ใช้ตอนให้คนใดคนหนึ่ง update เท่านั้น
    public static void sendResultTo(String target, String act, String... p) {
        String packet = "RESULT:" + target + ":" + act + (p.length > 0 ? ":" + String.join(":", p) : "");
        mqtt.publish("DiceLegend/" + GameState.currentGame.getLobbyName() + "/Results", packet);
    }
}