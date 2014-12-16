package lib.visualcryptiography.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class CryptIO {

    private static File dataFile;
    private static BufferedImage image;
    private static boolean on = true;

    private CryptIO() {
    }

    public static void notifyProcess(String msg) {
        System.out.println("~~" + msg);
    }

    public static void notifySubProcess(String msg) {
        System.out.println("~~~~" + msg);
    }

    public static void notifyErr(Object msg) {
        System.err.println("~~" + msg);
    }

    public static void notifyResult(Object msg) {
        System.out.println(String.format("%-20s", msg));
    }

    public static String readText() {
        String data = "";
        Scanner reader = null;
        try {
            reader = new Scanner(dataFile);
        } catch (Exception e) {
        }
        do {
            data += reader.nextLine() + "\n";
        } while (reader.hasNext());
        reader.close();
        return data;
    }

    public static BufferedImage readImage(String filePath) {
        try {
            File f = new File(filePath);
            f.createNewFile();
            return ImageIO.read(f);
        } catch (IOException e) {
            return null;
        }
    }

    public static Color[][] readPixels(String filePath) {
        try {
            File f = new File(filePath);
            f.createNewFile();
            image = ImageIO.read(f);
        } catch (IOException e) {
        }
        Color[][] pixels = new Color[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pixels[x][y] = new Color(image.getRGB(x, y), true);
            }
        }
        return pixels;
    }

    public static boolean write(BufferedImage toWrite, String name) {
        try {
            File f = new File(name);
            f.createNewFile();
            if (!ImageIO.write(toWrite, "png", f)) {
                return false;
            }
            notifyProcess("CryptIO wrote: " + name + ".png");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void setup() throws IOException {
        notifyProcess("CryptIO setup...");
        dataFile = (dataFile == null ? dataFile = new File("src/res/data.txt") : dataFile);
        dataFile.createNewFile();
    }
}
