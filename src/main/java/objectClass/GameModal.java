package objectClass;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import graphicsUtilities.ImagePreload;

public class GameModal extends JPanel {
    private int width;
    private int height;
    private Image background;

    public GameModal(int width, int height, String bgImgFileName) {
        this.width = width;
        this.height = height;
        this.background = ImagePreload.get(bgImgFileName);

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (background != null) {
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setImageBackgound(String backgound) {
        this.background = ImagePreload.get(backgound + ".png");
        this.repaint();
    }

}
