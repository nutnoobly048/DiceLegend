package graphicsUtilities;
//PROBLEM/IMPROVEMENT NEEDED : ตอนนี้ ImagePreloader จะอ่านไฟล์รูปที่อยู่ใน Image เท่านั้น จะไม่อ่านสิ่งที่อยู่ใน Subfolder
//ทำ Method preload ให้เป็นแบบ Recursive เพื่อเช็คทุกไฟล์
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ImagePreload {
    private static final HashMap<String, Image> preloadedAsset = new HashMap<>();

    public static void preloadAllImage() {
        //URL ชี้ไปที่ Image Folder
        URL resource = ImagePreload.class.getClassLoader().getResource(".");

        if (resource == null) {
            System.err.println("Error: 'Image' Folder not found");
            return;
        }

        //Folder โดยอ้างอิงจากพิกัดของ URL
        File folder = new File(resource.getPath());

        //list ของไฟล์ใน Folder
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && isImage(file.getName())) {
                    try {
                        Image img = ImageIO.read(file);
                        preloadedAsset.put(file.getName(), img);
                        System.out.println(file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static boolean isImage(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
    }

    public static Image get(String fileName) {
        return preloadedAsset.get(fileName);
    }
}