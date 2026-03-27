package scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.Timer;

import Gameplay.SceneList;
import OtherUtilities.RandomUtilities;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.AnimatedSprite;
import objectClass.GameButton;
import objectClass.GameObject;
import service.AudioService;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;


public class RealMainMenuScene extends Scene {

    private boolean isInitialized = false;
    private final GameObject transition_left = new GameObject("transit_left", "TransitionArrow.png", -2400,0);
    private final GameButton createButton = new GameButton("", "mainMenu-create.png", "mainMenu-create-hover.png");
    private final GameButton joinButton = new GameButton("", "mainMenu-join.png", "mainMenu-join-hover.png");
    private final GameButton creditButton = new GameButton("", "mainMenu-credit.png", "mainMenu-credit-hover.png");
    private final GameButton exitButton = new GameButton("", "mainMenu-quit.png", "mainMenu-quit-hover.png");

    private final GameObject gameLogo = new GameObject("logo",
            new AnimatedSprite("mainMenu-logo-animation.png", 0, 0, 32, 14, true, false));

    private final JoinPopUp popUp = new JoinPopUp();

    public RealMainMenuScene() {
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
    }

    @Override
    public void onCreate() {
        if (!isInitialized) {
            spawnObjectAt(gameLogo, 0, 0);
            spawnObjectAt(transition_left);
            setupButtons();
            setupVolumeSlider();
            this.add(popUp);

            isInitialized = true;
        }
        transition_left.x = -2400;
        this.requestFocusInWindow();
    }

    @Override
    public void onEnter() {
        new Tween(transition_left, TweenProperty.X, 0, -2400, 1).start();
    }

    @Override
    public void onExit() {
        playExitTransition();
    }

    private void setupButtons() {
        createButton.setBounds(100, 280, createButton.getPreferredSize().width, createButton.getPreferredSize().height);
        createButton.setOnButtonClicked(() -> {
            final String lobbyCode = String.valueOf(RandomUtilities.randomIntDigits(6));
            SceneUtilities.changeSceneTo(new LoadingScene(true, lobbyCode));
        });

        joinButton.setBounds(100, 450, joinButton.getPreferredSize().width, joinButton.getPreferredSize().height);
        joinButton.setOnButtonClicked(() -> popUp.setVisible(true));

        creditButton.setBounds(100, 600, creditButton.getPreferredSize().width, creditButton.getPreferredSize().height);

        exitButton.setBounds(100, 750, exitButton.getPreferredSize().width, exitButton.getPreferredSize().height);
        exitButton.setOnButtonClicked(() -> System.exit(0));

        this.add(createButton);
        this.add(joinButton);
        this.add(creditButton);
        this.add(exitButton);
        setupVolumeSlider();
    }

    private void setupVolumeSlider() {
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 30);
        volumeSlider.setFocusable(false);
        volumeSlider.setRequestFocusEnabled(false);
        volumeSlider.setOpaque(false);
        volumeSlider.setBounds(64, 1016, 200, 50);

        volumeSlider.setUI(new BasicSliderUI(volumeSlider) {
            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Image diceMover = ImagePreload.get("DiceThumb.png");
                g2d.drawImage(diceMover, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, null);
            }

            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(101, 67, 33));
                int trackY = trackRect.y + (trackRect.height / 2) - 3;
                g2d.fillRoundRect(trackRect.x, trackY, trackRect.width, 6, 10, 10);
            }

            @Override
            protected Dimension getThumbSize() {
                return new Dimension(20, 20);
            }
        });

        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100f;
            AudioService.getInstance().setMasterVolume(volume);
            if (AudioService.getInstance().getCurrentMusic() != null) {
                AudioService.getInstance().getCurrentMusic().setVolume(volume);
            }
        });

        this.add(volumeSlider);
    }
    private void playExitTransition() {
        new Tween(transition_left, TweenProperty.X, -2400, 0, 1).start();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        transition_left.getSprite().draw(g2d);
    }
}

class JoinPopUp extends JPanel {
    private Image background = ImagePreload.get("mainMenu-join-popup.png");
    private final GameButton joinButton = new GameButton("", "mainMenu-popup-join.png", "mainMenu-popup-join-hover.png");
    private final GameButton closeButton = new GameButton("", "mainMenu-popup-close.png", "mainMenu-popup-close.png");
    private final JTextField textField = new JTextField();

    public JoinPopUp() {
        this.setBounds(960-background.getWidth(null)/2, 540-background.getHeight(null)/2, background.getWidth(null), background.getHeight(null));
        this.setBackground(null);
        this.setLayout(null);
        this.setVisible(false);

        this.add(joinButton);
        joinButton.setBounds(50, 300, 401, 71);
        joinButton.setOnButtonClicked(() -> {
            SceneUtilities.changeSceneTo(new LoadingScene(false, textField.getText()));
        });

        this.add(textField);
        textField.setBackground(null);
        textField.setForeground(Color.white);
        textField.setFont(new Font("Arial", Font.PLAIN, 50));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBounds(50, 160, 401, 100);
        textField.setBorder(null);

        this.add(closeButton);
        closeButton.setBounds(background.getWidth(null) - 67, 0, 67, 67);
        closeButton.setOnButtonClicked(() -> {
            this.setVisible(false);
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }





}

// class Particle {
// float x, y, size, speedY, driftX, opacity;
// int colorType;
// Random rng;

// Particle(Random rng, int W, int H) {
// this.rng = rng;
// reset(W, H, true);
// }

// void reset(int W, int H, boolean anywhere) {
// x = rng.nextFloat() * W;
// y = anywhere ? rng.nextFloat() * H : H + 10;
// size = rng.nextFloat() * 3f + 1f;
// speedY = rng.nextFloat() * 0.6f + 0.2f;
// driftX = (rng.nextFloat() - 0.5f) * 0.3f;
// opacity = rng.nextFloat() * 0.6f + 0.2f;
// colorType = rng.nextInt(4);
// }

// void update(int W, int H) {
// y -= speedY;
// x += driftX;
// if (y < -10) reset(W, H, false);
// if (x < 0) x = W;
// if (x > W) x = 0;
// }

// void draw(Graphics2D g2) {
// int r, gr, b;
// switch (colorType) {
// case 0: r = 0; gr = 230; b = 255; break; // cyan
// case 1: r = 255; gr = 50; b = 180; break; // magenta
// case 2: r = 80; gr = 255; b = 120; break; // lime
// default: r = 255; gr = 220; b = 50; break; // yellow
// }
// int a = (int) (opacity * 220);

// g2.setColor(new Color(r, gr, b, (int) (opacity * 80)));
// float glow = size * 2.8f;
// g2.fillOval((int) (x - glow / 2), (int) (y - glow / 2), (int) glow, (int)
// glow);

// g2.setColor(new Color(r, gr, b, a));
// g2.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int)
// size);

// g2.setColor(new Color(255, 255, 255, (int) (opacity * 200)));
// g2.fillOval((int) (x - size / 4), (int) (y - size / 4), (int) (size / 2),
// (int) (size / 2));
// }
// }