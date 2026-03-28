package graphicsUtilities;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class ImagePreload {
    private static URL resourceURL;
    private static final HashMap<String, Image> preloadedAsset = new HashMap<>();

    public static void preloadAllImage() { preloadAllImage(null); }
    
    public static void preloadAllImage(URI folderURL) {
        //URL ชี้ไปที่ Image Folder
        if (resourceURL == null) {
            resourceURL = ImagePreload.class.getClassLoader().getResource(".");
        }

        if (resourceURL == null) { System.out.println("Cannot find Image Path"); }
        
        if (folderURL == null) {
            try {
                File folder = new File(resourceURL.toURI());
                File[] listFiles_Folders = folder.listFiles();
                for (File element: listFiles_Folders) {
                    // ถ้าเป็นไฟล์ -> นำไปใส่ Hashmap
                    if (element.isFile() || isImage(element.getName())) {
                        preloadedAsset.put(element.getName(), ImageIO.read(element));
                        // System.out.println("loaded image: " + element.getName());
                    }
                    // ถ้าเป็น folder -> recursive 
                    else if (element.isDirectory()) {
                        // System.out.println("digging into folder: " + element.getName());
                        preloadAllImage(element.toURI());
                    }
                }
            } catch (URISyntaxException | IOException e) {
                System.err.println(e);
            }
        } else if (folderURL != null) {
            File folder = new File(folderURL);
            File[] listFiles_Folders = folder.listFiles();
            for (File element: listFiles_Folders) {
                // ถ้าเป็นไฟล์ -> นำไปใส่ Hashmap
                if (element.isFile() && isImage(element.getName())) {
                    try {
                        preloadedAsset.put(element.getName(), ImageIO.read(element));
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                    // System.out.println("loaded image: " + element.getName());
                }
                // ถ้าเป็น folder -> recursive 
                else if (element.isDirectory()) {
                    // System.out.println("digging into folder: " + element.getName());
                    preloadAllImage(element.toURI());
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