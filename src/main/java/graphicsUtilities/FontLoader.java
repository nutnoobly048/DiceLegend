package graphicsUtilities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class FontLoader {
    private static Font myFont;

    private static void loadFont() {
        try (InputStream is = FontLoader.class.getResourceAsStream("/Font/SOV_BokThang.ttf")) {
            System.out.println("InputStream: " + is);
            if (is == null) {
                System.out.println("Font file not found!");
                return;
            }
            byte[] fontData = is.readAllBytes();
            myFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(fontData));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(myFont);
            System.out.println("Font loaded successfully: " + myFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Font getFont(float size) {
        if (myFont == null) {
            loadFont();
        }
        return myFont.deriveFont(size);
    }
}
