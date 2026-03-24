package objectClass;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class ClickableObject extends JPanel {

    public Runnable onMouseEntered = () -> {};
    public Runnable onMouseExited = () -> {};
    public Runnable onButtonClicked = () -> {};

    public ClickableObject() {
        addMouseListener(new MouseAdapter() {
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