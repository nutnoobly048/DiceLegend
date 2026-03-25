package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import Gameplay.SceneList;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.GameButton;
import objectClass.GameObject;
import service.CommandHandler;
import service.RunService;

import javax.swing.*;
import java.awt.*;

public class LobbyScene extends Scene {

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);

    private GameButton backButton = new GameButton("BACK", "button.png", "buttonOnHover.png");
    private GameButton startButton = new GameButton("START", "button.png", "buttonOnHover.png");

    private double duration = 0.5;

    public LobbyScene() {
        setupObjects();
        setupButtons();
        setupTransitions();
        setBackground(ImagePreload.get("battle.png"));
    }


    private void setupObjects() {
        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        backButton.setBounds(200, 500, 500, 80);
        add(backButton);

        startButton.setBounds(800, 500, 500,80);
        add(startButton);
    }

    private void setupButtons() {
        backButton.setOnButtonClicked(() -> {
            if (RunService.mqtt.isConnected()) {
                RunService.mqtt.clearRetained("DiceLegend/" + LobbyState.current.lobbyName + "/room_state");
                SceneUtilities.changeSceneTo(SceneList.mainMenu);
                RunService.mqtt.disconnect();
                GameState.currentGame = null;
                LobbyState.current = null;
            }
        });

        startButton.setOnButtonClicked(() -> {
            CommandHandler.sentIntent("INTENT:SELF:START_GAME");
        });
    }

    private void setupTransitions() {
        setOnSceneEnter(this::playEnterTransition);

    }


    private void playEnterTransition() {
        new Tween(transition_left,  TweenProperty.X, -960, -1920, duration).start();
        new Tween(transition_right, TweenProperty.X,  960,  1920, duration).start();
        new Tween(transition_up,    TweenProperty.Y, -540, -1080, duration).start();
        new Tween(transition_down,  TweenProperty.Y,  540,  1080, duration).start();
    }

    @Override
    public void requestExit(Runnable onExitComplete) {
        playExitTransition(() -> {
            onExitComplete.run();
        });
    }
    private void playExitTransition(Runnable onDone) {
        new Tween(transition_left,  TweenProperty.X, -1920, -960, duration).start();
        new Tween(transition_right, TweenProperty.X,  1920,  960, duration).start();
        new Tween(transition_up,    TweenProperty.Y, -1080, -540, duration).start();
        new Tween(transition_down,  TweenProperty.Y,  1080,  540, duration).OnComplete(onDone).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        transition_left.getSprite().draw((Graphics2D) g);
        transition_right.getSprite().draw((Graphics2D) g);
        transition_up.getSprite().draw((Graphics2D) g);
        transition_down.getSprite().draw((Graphics2D) g);
    }

}
