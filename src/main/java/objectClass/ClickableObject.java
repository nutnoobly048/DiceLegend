package objectClass;

import graphicsUtilities.ImagePreload;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickableObject extends JPanel {

    Image image;

    public ClickableObject(String imageName) {
        image = ImagePreload.get(imageName);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleImageClick(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    private void handleImageClick(MouseEvent e) {
        System.out.println("Image clicked at: " + e.getX() + ", " + e.getY());
        System.exit(0);
    }

}
