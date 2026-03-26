package scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

import OtherUtilities.RandomUtilities;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.AnimatedSprite;
import objectClass.GameButton;
import objectClass.GameObject;

public class RealMainMenuScene extends Scene {

    private final GameButton createButton = new GameButton("", "mainMenu-create.png", "mainMenu-create-hover.png");
    private final GameButton joinButton = new GameButton("", "mainMenu-join.png", "mainMenu-join-hover.png");
    private final GameButton creditButton = new GameButton("", "mainMenu-credit.png", "mainMenu-credit-hover.png");
    private final GameButton exitButton = new GameButton("", "mainMenu-quit.png", "mainMenu-quit-hover.png");

    private final GameObject gameLogo = new GameObject("logo", new AnimatedSprite("mainMenu-logo-animation.png", 0, 0, 32, 14));

    public RealMainMenuScene() {
        this.setBackground(Color.BLACK);
        setupButtons();
        setupLogo();
    }

    public void setupLogo() {
        this.spawnObjectAt(gameLogo, 0, 0);
    }

    public void setupButtons() {
        createButton.setBounds(100, 280, createButton.getPreferredSize().width, createButton.getPreferredSize().height);
        createButton.setOnButtonClicked(() -> {
            final String lobbyCode = String.valueOf(RandomUtilities.randomIntDigits(6));
            System.out.println("Lobby code: " + lobbyCode);
            SceneUtilities.changeSceneTo(new LoadingScene(true, lobbyCode));
        });

        joinButton.setBounds(100, 450, joinButton.getPreferredSize().width, joinButton.getPreferredSize().height);
        creditButton.setBounds(100, 600, creditButton.getPreferredSize().width, creditButton.getPreferredSize().height);

        exitButton.setBounds(100, 750, exitButton.getPreferredSize().width, exitButton.getPreferredSize().height);
        exitButton.setOnButtonClicked(() -> {
            System.exit(0);
        });

        this.add(createButton);
        this.add(joinButton);
        this.add(creditButton);
        this.add(exitButton);
    }
}

class JoinPopUp {
    
}

// class Particle {
//     float x, y, size, speedY, driftX, opacity;
//     int colorType;
//     Random rng;

//     Particle(Random rng, int W, int H) {
//         this.rng = rng;
//         reset(W, H, true);
//     }

//     void reset(int W, int H, boolean anywhere) {
//         x = rng.nextFloat() * W;
//         y = anywhere ? rng.nextFloat() * H : H + 10;
//         size = rng.nextFloat() * 3f + 1f;
//         speedY = rng.nextFloat() * 0.6f + 0.2f;
//         driftX = (rng.nextFloat() - 0.5f) * 0.3f;
//         opacity = rng.nextFloat() * 0.6f + 0.2f;
//         colorType = rng.nextInt(4);
//     }

//     void update(int W, int H) {
//         y -= speedY;
//         x += driftX;
//         if (y < -10) reset(W, H, false);
//         if (x < 0) x = W;
//         if (x > W) x = 0;
//     }

//     void draw(Graphics2D g2) {
//         int r, gr, b;
//         switch (colorType) {
//             case 0: r = 0; gr = 230; b = 255; break; // cyan
//             case 1: r = 255; gr = 50; b = 180; break; // magenta
//             case 2: r = 80; gr = 255; b = 120; break; // lime
//             default: r = 255; gr = 220; b = 50; break; // yellow
//         }
//         int a = (int) (opacity * 220);

//         g2.setColor(new Color(r, gr, b, (int) (opacity * 80)));
//         float glow = size * 2.8f;
//         g2.fillOval((int) (x - glow / 2), (int) (y - glow / 2), (int) glow, (int) glow);

//         g2.setColor(new Color(r, gr, b, a));
//         g2.fillOval((int) (x - size / 2), (int) (y - size / 2), (int) size, (int) size);

//         g2.setColor(new Color(255, 255, 255, (int) (opacity * 200)));
//         g2.fillOval((int) (x - size / 4), (int) (y - size / 4), (int) (size / 2), (int) (size / 2));
//     }
// }