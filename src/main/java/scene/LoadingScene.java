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
import objectClass.AnimatedSprite;
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

    private final GameObject landingLoading = new GameObject("loading",
            new AnimatedSprite("dice-loading.png", 0, 0, 32, 30, true, false));

    public LoadingScene(boolean isHost, String lobbyName) {
        this.isHost = isHost;
        this.lobbyName = lobbyName;
        setBackground(ImagePreload.get("Transition.png"));
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onEnter() {

        connectToNetwork();
        spawnObjectAt(landingLoading, 0,0);

    }

    @Override
    public void onExit() {
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

                    RunService.mqtt.subscribe(topics, (t, msg) -> {
                        if (msg.equals("ACTIVE") || msg.equals("GAME_STARTED")) {
                            roomTaken.set(true);
                        }
                    });

                    Thread.sleep(2000);

                    if (!roomTaken.get()) {
                        RunService.mqtt.subscribe(baseTopic + "/Intents",
                                (topic, msg) -> RunService.intentQueue.add(msg));
                        RunService.mqtt.subscribe(baseTopic + "/player-left", (t, msg) -> {
                            if (msg.startsWith("DISCONNECT:")) {
                                String disconnectedId = msg.replace("DISCONNECT:", "");
                                CommandHandler.sentIntent("INTENT:" + disconnectedId + ":LEAVE_GAME");
                            }
                        });
                        RunService.mqtt.publishRetained(baseTopic + "/room_state", "ACTIVE");
                        RunService.mqtt.publishRetained(baseTopic + "/room_amount", "1");
                        connected = true;
                    }

                } else {
                    RunService.mqtt.connectClientWithWill(this.lobbyName, RunService.mqtt.clientId);
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
                    RunService.mqtt.subscribe(baseTopic + "/Results", (topic, msg) -> RunService.resultQueue.add(msg));

                    if (!isHost) {
                        String topicRoomAmount = baseTopic + "/room_amount";
                        AtomicBoolean isAdd = new AtomicBoolean(false);
                        RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
                            if (!isAdd.getAndSet(true)) {
                                int addAmount = Integer.parseInt(msg) + 1;
                                RunService.mqtt.publishRetained(topicRoomAmount, String.valueOf(addAmount));
                            }
                        });
                    }

                    CommandHandler.sentIntent(
                            "INTENT:" + Player.getLocalPlayerId() + ":JOIN_GAME:" + Player.getLocalPlayerName());

                    SwingUtilities.invokeLater(() -> SceneUtilities.changeSceneTo(SceneList.lobbyScene));
                } else {
                    RunService.mqtt.disconnect();
                    LobbyState.current = null;
                    SwingUtilities.invokeLater(() -> SceneUtilities.changeSceneTo(SceneList.mainMenu));
                }

            } catch (Exception e) {
                System.err.println("Connection Failed: " + e.getMessage());
                SwingUtilities.invokeLater(() -> SceneUtilities.changeSceneTo(SceneList.mainMenu));
            }
        }, "NetworkThread").start();
    }
}