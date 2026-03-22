package objectClass;


import graphicsUtilities.ImagePreload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameButton extends JButton {

    public Runnable onMouseEntered = () -> {};
    public Runnable onMouseExited = () -> {};
    public Runnable onButtonClicked = () -> {};

    public GameButton(String text, String normalImg, String hoverImg) {
        super(text);

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon iconNormal = new ImageIcon(ImagePreload.get(normalImg));
        ImageIcon iconHover = new ImageIcon(ImagePreload.get(hoverImg));

        this.setIcon(iconNormal);
        this.setRolloverIcon(iconHover);
        this.setPressedIcon(iconHover);

        this.setForeground(Color.WHITE);
        this.setFont(new Font("Arial", Font.BOLD, 18));
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                onMouseEntered.run();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                onMouseExited.run();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onButtonClicked.run();
            }
        });
    }

    public GameButton(String text) {
        super(text);
        setText("");
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent e) { onButtonClicked.run(); }
            @Override public void mouseEntered(MouseEvent e)  { onMouseEntered.run(); }
            @Override public void mouseExited(MouseEvent e)   { onMouseExited.run();  }
        });
    }

    public void setOnButtonClicked(Runnable onButtonClicked) {
        this.onButtonClicked = onButtonClicked;
    }

    public void setOnMouseExited(Runnable onMouseExited) {
        this.onMouseExited = onMouseExited;
    }

    public void setOnMouseEntered(Runnable onMouseEntered) {
        this.onMouseEntered = onMouseEntered;
    }
}

