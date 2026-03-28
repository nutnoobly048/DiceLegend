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
import objectClass.GameObject;

public class InitialScene extends Scene {
    private JLabel label = new JLabel("CLick anywhere to continue.");
    private GameObject bg = new GameObject("ini-game-bg", "initial-bg.png", 0, 0);

    @Override
    public void onCreate() {
        this.setBackground(Color.BLACK);
        this.add(label);

        // background
        spawnObjectAt(bg);

        label.setForeground(Color.black);
        label.setFont(FontLoader.getFont(100));
        
        int labelWidth = label.getPreferredSize().width;
        int labelHeight = label.getPreferredSize().height;
        int screenWidth = 1920;
        int screenHeight = 1080;
        int x = (screenWidth - labelWidth) / 2;
        int y = screenHeight - labelHeight - 50;
        
        label.setBounds(x, y, labelWidth, labelHeight);

        // add listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SceneUtilities.changeSceneTo(new RealMainMenuScene());
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
