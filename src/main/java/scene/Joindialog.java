package scene;

import javax.swing.*;

import graphicsUtilities.FontLoader;

import java.awt.*;
import java.util.function.Consumer;

public class Joindialog extends JDialog {

    private Consumer<String> onJoin;
    private JTextField codeField;

    public Joindialog (Window owner){
        super (owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor((Color.WHITE));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor((new Color(25, 45, 120)));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 12, 12);

            }
        };
        root.setOpaque(false);
        root.setBounds(0, 0, 520, 420);
        setContentPane(root);

        //Title
        JLabel title = new JLabel("ENTER CODE", SwingConstants.CENTER);
        title.setFont(FontLoader.getFont(28));
        title.setForeground(new Color(255, 200 ,30));
        title.setBounds(30, 40, 460, 40);
        root.add(title);

        //Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(180, 180, 200, 120));
        sep.setBounds(40, 90, 440, 2);
        root.add(sep);

        //Code Input
        codeField = new JTextField() {
            @Override protected  void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.BLACK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        codeField.setFont(FontLoader.getFont(32));
        codeField.setForeground(new Color(160, 160, 180));
        codeField.setCaretColor(new Color(200, 150, 50));
        codeField.setHorizontalAlignment(JTextField.CENTER);
        codeField.setOpaque(false);
        codeField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(8, 16, 8,16)
        ));
        codeField.setBounds(40, 120, 440, 90);
        root.add(codeField);

        JButton joinBtn = new JButton("JOIN PARTY") {
            @Override protected  void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(70, 90, 160)
                        : new Color(50, 65, 130);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setFont(FontLoader.getFont(22));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        joinBtn.setContentAreaFilled(false);
        joinBtn.setBorderPainted(false);
        joinBtn.setFocusPainted(false);
        joinBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        joinBtn.setBounds(40, 240, 440, 70);
        joinBtn.addActionListener(e ->{
            String code = codeField.getText().trim();
            if (!code.isEmpty() && onJoin != null) {
                onJoin.accept(code);
                dispose();
            }
        });
        root.add(joinBtn);

        JButton closeBtn = new JButton("×") {
            @Override protected  void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(200, 30, 30) : new Color(180, 20, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(FontLoader.getFont(20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("×", (getWidth() - fm.stringWidth("×")) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

            }
        };
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setBounds(474, -10, 46, 46);
        closeBtn.addActionListener(e -> dispose());
        root.add(closeBtn);
    }
    public void setOnJoin(Consumer<String> onJoin) {
        this.onJoin = onJoin;
    }
}
