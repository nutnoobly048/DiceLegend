package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import Gameplay.SceneList;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import misc.Player;
import objectClass.GameButton;
import objectClass.GameObject;
import service.CommandHandler;
import service.RunService;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingScene extends Scene {

    private boolean isHost;
    private String lobbyName;

    public LoadingScene(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        setBackground(ImagePreload.get("Transition.png"));
        setOnSceneEnter(this::connectToNetwork);
    }

    private void connectToNetwork() {
        String baseTopic = "DiceLegend/" + this.lobbyName;

        new Thread(() -> {
            try {
                new LobbyState(this.isHost, this.lobbyName);
                boolean connected = false;
                boolean isRoomFull = false;
                if (isHost) {

                    AtomicBoolean roomTaken = new AtomicBoolean(false);
                    String topics = baseTopic + "/room_state";

                    RunService.mqtt.connectWithWill(baseTopic, "HOST_DISCONNECTED");

                    // เช็คห้องว่าถูกใช้ไปหรือยัง
                    RunService.mqtt.subscribe(topics, (t, msg) -> {
                        if (msg.equals("ACTIVE")) {
                            roomTaken.set(true);
                        }
                    });

                    Thread.sleep(2000);

                    if (!roomTaken.get()) {

                        RunService.mqtt.subscribe(baseTopic + "/Intents",
                                (topic, msg) -> RunService.intentQueue.add(msg));
                        RunService.mqtt.publishRetained(baseTopic + "/room_state", "ACTIVE");
                        RunService.mqtt.publishRetained(baseTopic + "/room_amount", "1");
                        connected = true;

                    }

                } else {
                    RunService.mqtt.connect();
                    AtomicBoolean state = new AtomicBoolean(false);
                    AtomicBoolean checkAmountRoom = new AtomicBoolean(false);
                    
                    String topicRoomState = baseTopic + "/room_state";
                    RunService.mqtt.subscribe(topicRoomState, (t, msg) -> {
                        if (msg.equals("ACTIVE"))
                            state.set(true);
                    });

                    String topicRoomAmount = baseTopic + "/room_amount";
                    RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
                        if (msg.equals("4"))
                            checkAmountRoom.set(true);
                    });

                    Thread.sleep(3000);
                    connected = state.get();
                    isRoomFull = checkAmountRoom.get();

                }
                if (connected && !isRoomFull) {
                    System.out.println("Connected");
                    System.out.println("Network Ready for " + (isHost ? "Host" : "Client"));

                    RunService.mqtt.subscribe(baseTopic + "/Results", (topic, msg) -> RunService.resultQueue.add(msg));

                    if(!isHost){
                        
                        String topicRoomAmount = baseTopic + "/room_amount";
                        AtomicBoolean isAdd = new AtomicBoolean(false);
                        RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
                            if (!isAdd.getAndSet(true)){

                                int addAmount = Integer.parseInt(msg) + 1;
                                RunService.mqtt.publishRetained(topicRoomAmount, String.valueOf(addAmount));

                            }
                        });

                    }

                    CommandHandler.sentIntent(
                            "INTENT:" + Player.getLocalPlayerId() + ":JOIN_GAME:" + Player.getLocalPlayerName());

                    SceneUtilities.changeSceneTo(SceneList.lobbyScene);
                } else {
                    RunService.mqtt.disconnect();
                    System.out.println("Disconnect");
                    SceneUtilities.changeSceneTo(SceneList.mainMenu);
                    LobbyState.current = null; // สร้างไม่สำเร็จ ให้ reset current game
                }

            } catch (Exception e) {
                System.err.println("Connection Failed: " + e.getMessage());
                SceneUtilities.changeSceneTo(SceneList.mainMenu);
            }
        }, "NetworkThread").start();
    }
}
