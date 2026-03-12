package scene;

import Gameplay.GameState;
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
                new GameState(this.isHost, this.lobbyName);
                boolean connected = false;
                if (isHost) {
                    RunService.mqtt.connectWithWill(baseTopic, "HOST_DISCONNECTED");
                    RunService.mqtt.subscribe(baseTopic + "/Intents", (topic, msg) -> RunService.intentQueue.add(msg));
                    RunService.mqtt.publishRetained(baseTopic + "/room_state", "ACTIVE");
                    connected = true;
                } else {
                    RunService.mqtt.connect();
                    AtomicBoolean state = new AtomicBoolean(false);

                    String topics = baseTopic + "/room_state";
                    RunService.mqtt.subscribe(topics, (t, msg) -> { if (msg.equals("ACTIVE")) state.set(true);});

                    Thread.sleep(3000);
                    connected = state.get();
                }
                if ( connected ) {
                    System.out.println("Connected");
                    System.out.println("Network Ready for " + (isHost ? "Host" : "Client"));
                    RunService.mqtt.subscribe(baseTopic + "/Results", (topic, msg) -> RunService.resultQueue.add(msg));
                    CommandHandler.sentIntent("INTENT:" + Player.getLocalPlayerId() + ":JOIN_GAME:JeenIsReal");
                    SceneUtilities.changeSceneTo(new LobbyScene());
                } else {
                    RunService.mqtt.disconnect();
                    System.out.println("Disconnect");
                    SceneUtilities.changeSceneTo(SceneList.mainMenu);
                    GameState.currentGame = null; //สร้างไม่สำเร็จ ให้ reset current game
                }

            } catch (Exception e) {
                System.err.println("Connection Failed: " + e.getMessage());
                SceneUtilities.changeSceneTo(SceneList.mainMenu);
            }
        }, "NetworkThread").start();
    }
}
