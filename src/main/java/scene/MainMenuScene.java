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
import java.util.Random;

public class MainMenuScene extends Scene {

    private java.util.List<Particle> particles = new java.util.ArrayList<>();
    private Random rng = new Random();
    private Timer particleTimer;
    private float time = 0;
    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);
    private GameButton createaButton    = new GameButton("CREATE", "button.png", "buttonOnHover.png");
    private GameButton joinButton       = new GameButton("JOIN", "button.png", "buttonOnHover.png");
    private GameButton exitButton       = new GameButton("EXIT", "button.png", "buttonOnHover.png");
    private GameButton debugLobbyButton = new GameButton("DEBUG LOBBY", "button.png", "buttonOnHover.png");
    private JTextField textField        = new JTextField();

    private double duration = 0.4;

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
        spawnParticles(80);
        startParticleLoop();


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

        debugLobbyButton.setBounds(750, 500, 500, 80);
        add(debugLobbyButton);

    }

    private void setupButtons() {
        createaButton.setOnButtonClicked(() -> {
            playExitTransition(() -> SceneUtilities.changeSceneTo(new LoadingScene(true, textField.getText())));
        });

        joinButton.setOnButtonClicked(() -> {
            playExitTransition(() -> SceneUtilities.changeSceneTo(new LoadingScene(false, textField.getText())));
        });

        exitButton.setOnButtonClicked(() -> {
            System.exit(0);
        });

        debugLobbyButton.setOnButtonClicked(() -> {
            playExitTransition(() -> SceneUtilities.changeSceneTo(new RealLobbyScene()));
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

    private void playExitTransition(Runnable onDone) {
        new Tween(transition_left,  TweenProperty.X, -1920, -960, duration).start();
        new Tween(transition_right, TweenProperty.X,  1920,  960, duration).start();
        new Tween(transition_up,    TweenProperty.Y, -1080, -540, duration).start();
        new Tween(transition_down,  TweenProperty.Y,  1080,  540, duration).OnComplete(onDone).start();
    }

    private void spawnParticles(int count) {
        for (int i = 0; i < count; i++){
            particles.add(new Particle(rng, 1920, 1080));
        }
    }

    private void startParticleLoop() {
        particleTimer = new Timer(16, e -> {
            time += 0.016f;
            for (Particle p : particles) p.update(1920, 1080);
            repaint();
        });
        particleTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2  = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Particle p : particles) p.draw(g2);

        transition_left.getSprite().draw((Graphics2D) g);
        transition_right.getSprite().draw((Graphics2D) g);
        transition_up.getSprite().draw((Graphics2D) g);
        transition_down.getSprite().draw((Graphics2D) g);
    }
}
class Particle {
    float x, y, size, speedY, driftX, opacity;
    int colorType;
    Random rng;

    Particle(Random rng, int W, int H) {
        this.rng = rng;
        reset(W, H, true);
    }
    void reset(int W, int H, boolean anywhere) {
        x         = rng.nextFloat() * W;
        y         = anywhere ? rng.nextFloat() * H : H + 10;
        size      = rng.nextFloat() * 3f + 1f;
        speedY    = rng.nextFloat() * 0.6f + 0.2f;
        driftX    = (rng.nextFloat() - 0.5f) * 0.3f;
        opacity   = rng.nextFloat() * 0.6f + 0.2f;
        colorType = rng.nextInt(4);
    }
    void update(int W, int H) {
        y -= speedY;
        x += driftX;
        if (y < -10) reset(W, H, false);
        if (x < 0) x = W;
        if (x > W) x = 0;
    }
    void draw(Graphics2D g2) {
        int r, gr, b;
        switch (colorType) {
            case 0: r =   0; gr = 230; b = 255; break; // cyan
            case 1: r = 255; gr =  50; b = 180; break; // magenta
            case 2: r =  80; gr = 255; b = 120; break; // lime
            default:r = 255; gr = 220; b =  50; break; // yellow
        }
        int a = (int)(opacity * 220);

        g2.setColor(new Color(r, gr, b, (int)(opacity * 80)));
        float glow = size * 2.8f;
        g2.fillOval((int)(x - glow/2), (int)(y - glow/2), (int)glow, (int)glow);

        g2.setColor(new Color(r, gr, b, a));
        g2.fillOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);

        g2.setColor(new Color(255, 255, 255, (int)(opacity * 200)));
        g2.fillOval((int)(x - size/4), (int)(y - size/4), (int)(size/2), (int)(size/2));
    }
}
