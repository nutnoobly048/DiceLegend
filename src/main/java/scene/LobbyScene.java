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

public class LobbyScene extends Scene {

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);

    private GameButton backButton = new GameButton("BACK", "button.png", "buttonOnHover.png");
    private GameButton startButton = new GameButton("START", "button.png", "buttonOnHover.png");

    private double duration = 0.5;

    public LobbyScene() {
        setBackground(ImagePreload.get("battle.png"));
    }
    @Override
    public void onCreate() {
        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        backButton.setBounds(200, 500, 500, 80);
        startButton.setBounds(800, 500, 500, 80);

        setupButtonLogic();

        this.add(backButton);
        this.add(startButton);
    }
    @Override
    public void onEnter() {
        new Tween(transition_left,  TweenProperty.X, -960, -1920, duration).start();
        new Tween(transition_right, TweenProperty.X,  960,  1920, duration).start();
        new Tween(transition_up,    TweenProperty.Y, -540, -1080, duration).start();
        new Tween(transition_down,  TweenProperty.Y,  540,  1080, duration).start();
    }

    @Override
    public void onExit() {
        new Tween(transition_left,  TweenProperty.X, -1920, -960, duration).start();
        new Tween(transition_right, TweenProperty.X,  1920,  960, duration).start();
        new Tween(transition_up,    TweenProperty.Y, -1080, -540, duration).start();
        new Tween(transition_down,  TweenProperty.Y,  1080,  540, duration).start();
    }

    private void setupButtonLogic() {
        backButton.setOnButtonClicked(() -> {
            if (RunService.mqtt.isConnected() && LobbyState.current.isHost) {
                CommandHandler.broadcastResult("CHANGESCENETO", "main_menu");
                CommandHandler.broadcastResult("DISCONNECT");
                RunService.mqtt.clearRetained("DiceLegend/" + LobbyState.current.lobbyName + "/room_state");
            } else if (RunService.mqtt.isConnected()) {
                handleClientExit();
            }
        });

        startButton.setOnButtonClicked(() -> {
            String baseTopic = "DiceLegend/" + LobbyState.current.lobbyName;
            RunService.mqtt.publishRetained(baseTopic + "/room_state", "GAME_STARTED");
            CommandHandler.sentIntent("INTENT:SELF:START_GAME");
        });
    }

    private void handleClientExit() {
        AtomicBoolean isMinus = new AtomicBoolean(false);
        SceneUtilities.changeSceneTo(SceneList.mainMenu);

        CommandHandler.broadcastResult("PLAYER_LEFT", Player.localPlayer.getNetworkID());
        String topicRoomAmount = "DiceLegend/" + LobbyState.current.lobbyName + "/room_amount";

        RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
            if (!RunService.mqtt.isConnected()) return;
            try {
                if (!isMinus.getAndSet(true)) {
                    int addAmount = Math.max(0, Integer.parseInt(msg) - 1);
                    RunService.mqtt.publishRetained(topicRoomAmount, String.valueOf(addAmount));
                    RunService.mqtt.unsubscribe(topicRoomAmount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    if (RunService.mqtt.isConnected()) RunService.mqtt.disconnect();
                });
            }
        });
        GameState.currentGame = null;
        LobbyState.destroy();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        transition_left.getSprite().draw(g2d);
        transition_right.getSprite().draw(g2d);
        transition_up.getSprite().draw(g2d);
        transition_down.getSprite().draw(g2d);
    }
}