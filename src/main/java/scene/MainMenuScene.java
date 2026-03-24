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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;
import java.util.function.Consumer;

public class MainMenuScene extends Scene {

    private java.util.List<Particle> particles = new java.util.ArrayList<>();
    private Random rng = new Random();
    private Timer particleTimer;
    private float time = 0;

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);


    private GameButton createaButton = new GameButton("");
    private GameButton joinButton    = new GameButton("");
    private GameButton exitButton    = new GameButton("");
    private JTextField textField        = new JTextField();

    private int hoveredItem             = -1;
    private final String[] menuLabels   = { "CREATE", "JOIN", "EXIT"};
    private final int MENU_X            = 260;
    private final int MENU_START        = 500;
    private final int MENU_STEP         = 150;
    private final int BTN_W             = 500;
    private final int BTN_H             = 80;


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


        createaButton.setBounds(MENU_X - 40, MENU_START - BTN_H / 2, BTN_W, BTN_H);
        createaButton.setOpaque((false));
        createaButton.setContentAreaFilled(false);
        createaButton.setBorderPainted(false);
        add(createaButton);

        joinButton.setBounds(MENU_X - 40, MENU_START + MENU_STEP - BTN_H / 2, BTN_W, BTN_H);
        joinButton.setOpaque(false);
        joinButton.setContentAreaFilled(false);
        joinButton.setBorderPainted(false);
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

        exitButton.setBounds(MENU_X - 40, MENU_START + MENU_STEP * 2 - BTN_H / 2, BTN_W, BTN_H);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        add(exitButton);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int prev = hoveredItem;
                hoveredItem = getHoveredMenuItem(e.getX(), e.getY());
                if (prev != hoveredItem) repaint();
            }
        });
    }
    private int getHoveredMenuItem(int mx, int my) {
        for (int i = 0; i < menuLabels.length; i++) {
            int cy = MENU_START + MENU_STEP * i;
            if (mx >= MENU_X - 40 && mx <= MENU_X - 40 + BTN_W && my >= cy - BTN_H / 2 && my <= cy +BTN_H / 2) return i;
        }
        return  -1;
    }

    private void setupButtons() {
        createaButton.setOnButtonClicked(() -> {
            new GameState(true, textField.getText());
            playExitTransition(() -> SceneUtilities.changeSceneTo(new LoadingScene(true, textField.getText())));
        });

        joinButton.setOnButtonClicked(() -> {
            Joindialog dialog = new Joindialog(SwingUtilities.getWindowAncestor(MainMenuScene.this));
            dialog.setOnJoin(code -> {
                new GameState(false, textField.getText());
                playExitTransition(() -> SceneUtilities.changeSceneTo(new LoadingScene(false, textField.getText())));
            });
            dialog.setVisible(true);
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

        drawMenuLabels(g2);

        for (Particle p : particles) p.draw(g2);

        transition_left.getSprite().draw((Graphics2D) g);
        transition_right.getSprite().draw((Graphics2D) g);
        transition_up.getSprite().draw((Graphics2D) g);
        transition_down.getSprite().draw((Graphics2D) g);
    }

    private void drawMenuLabels(Graphics2D g2){
        int start_x = MENU_X;

        for (int i = 0; i < menuLabels.length; i++){
            int cy = MENU_START + MENU_STEP * i;
            boolean hovered = (hoveredItem == i);

            if(hovered) {
                g2.setColor((new Color(255,255,255,18)));
                g2.fillRoundRect(start_x - 50, cy - BTN_H / 2 - 4, BTN_W + 20, BTN_H + 8, 6, 6);
            }

            g2.setStroke(new BasicStroke(1.8f));
            Color iconColor = hovered ? new Color(200, 255, 200) : new Color(160, 180, 160);
            g2.setColor((iconColor));
            drawMenuIcon(g2, i, start_x - 32, cy);

            //label text
            g2.setFont(new Font("Courier New", Font.BOLD, 26));
            if (hovered) {
                g2.setColor(Color.WHITE);
                g2.drawString("▶ " + menuLabels[i], start_x + 4, cy + 9);
            } else {
                g2.setColor(new Color(155,170,155));
                g2.drawString(menuLabels[i], start_x + 4, cy + 9);
            }
        }
    }

    private void drawMenuIcon(Graphics2D g2, int idx, int x, int y){
        g2.setStroke(new BasicStroke(1.8f));
        switch (idx) {
            case 0: // crossed swords in create
                g2.drawLine(x - 8, y - 8, x + 8, y + 8);
                g2.drawLine(x + 8, y - 8, x - 8, y + 8);
                g2.drawLine(x - 6, y - 10, x - 10, y - 6);
                g2.drawLine(x + 6, y + 10, x + 10, y + 6);
                break;
            case 1: //people in join
                g2.drawOval(x - 4, y - 12, 8, 8);
                g2.drawArc(x - 9, y - 4, 18, 12, 0, 180);
                g2.drawOval(x + 4, y - 13, 7, 7);
                g2.drawArc(x,     y - 5,  16, 11, 0, 180);
                break;
            case 2: // X in Exit
                g2.drawLine(x - 7, y - 7, x + 7, y + 7);
                g2.drawLine(x + 7, y - 7, x - 7, y + 7);
                break;
        }
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
