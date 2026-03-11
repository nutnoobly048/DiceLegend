package scene;

import Gameplay.GameState;
import Gameplay.SceneList;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.GameObject;
import objectClass.GameButton;

import javax.swing.*;
import java.awt.*;

public class MainMenuScene extends Scene {

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);
    private GameButton createaButton    = new GameButton("CREATE", "button.png", "buttonOnHover.png");
    private GameButton joinButton       = new GameButton("JOIN", "button.png", "buttonOnHover.png");
    private GameButton exitButton       = new GameButton("EXIT", "button.png", "buttonOnHover.png");
    private JTextField textField        = new JTextField();

    public MainMenuScene() {
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


        createaButton.setBounds(200, 500, 500, 80);
        add(createaButton);

        joinButton.setBounds(200, 650, 500, 80);
        add(joinButton);

        textField.setBounds(750,650, 500,80);
        textField.setOpaque(true); // Make it clear
        textField.setFont(new Font("Arial", Font.BOLD, 18));
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(20,25,51));
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(13,12,25)));
        textField.setHorizontalAlignment(JTextField.CENTER);
        add(textField);

        exitButton.setBounds(200, 800, 500, 80);
        add(exitButton);

    }

    private void setupButtons() {
        createaButton.setOnButtonClicked(() -> {
            new GameState(true, textField.getText());
            playExitTransition(() -> SceneUtilities.changeSceneTo(new LobbyScene()));
        });

        joinButton.setOnButtonClicked(() -> {
            new GameState(false, textField.getText());
            playExitTransition(() -> SceneUtilities.changeSceneTo(new LobbyScene()));
        });

        exitButton.setOnButtonClicked(() -> {
            System.exit(0);
        });
    }

    private void setupTransitions() {
        setOnSceneEnter(this::playEnterTransition);
    }


    private void playEnterTransition() {
        new Tween(transition_left,  TweenProperty.X, -960, -1920, 1).start();
        new Tween(transition_right, TweenProperty.X,  960,  1920, 1).start();
        new Tween(transition_up,    TweenProperty.Y, -540, -1080, 1).start();
        new Tween(transition_down,  TweenProperty.Y,  540,  1080, 1).start();
    }

    private void playExitTransition(Runnable onDone) {
        new Tween(transition_left,  TweenProperty.X, -1920, -960, 1).start();
        new Tween(transition_right, TweenProperty.X,  1920,  960, 1).start();
        new Tween(transition_up,    TweenProperty.Y, -1080, -540, 1).start();
        new Tween(transition_down,  TweenProperty.Y,  1080,  540, 1).OnComplete(onDone).start();
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
