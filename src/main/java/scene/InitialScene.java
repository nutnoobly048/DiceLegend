package scene;

import java.awt.Color;
import java.awt.Label;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import graphicsUtilities.FontLoader;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import objectClass.AnimatedSprite;
import objectClass.GameObject;
import service.AudioService;

public class InitialScene extends Scene {
    private JLabel label = new JLabel("<<CLick anywhere to begin>>");
    private GameObject bg = new GameObject("ini-game-bg", "initial-bg.png", 0, 0);
    private final GameObject gameLogo = new GameObject("logo",
            new AnimatedSprite("mainMenu-logo-animation.png", 0, 0, 32, 14, true, false));
    @Override
    public void onCreate() {
        this.setBackground(Color.BLACK);
        this.add(label);

        // background
        spawnObjectAt(bg);

        label.setForeground(Color.white);
        label.setFont(FontLoader.getFont(80));
        
        int labelWidth = label.getPreferredSize().width;
        int labelHeight = label.getPreferredSize().height;
        int screenWidth = 1920;
        int screenHeight = 1080;
        int x = (screenWidth - labelWidth) / 2;
        int y = screenHeight - labelHeight - 50;
        
        label.setBounds(x, y, labelWidth, labelHeight);
        spawnObjectAt(gameLogo);
        AudioService.getInstance().playMusic("8-Bit Hero.wav");
        // add listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // AudioService.getInstance().stopMusic();
                SceneUtilities.changeSceneTo(SceneUtilities.mainMenu);
                AudioService.getInstance().playSFX("MainMenu.wav");
            }
        });
    }

    @Override
    public void onEnter() {
        
    }

    @Override
    public void onExit() {
        
    }
    
}
