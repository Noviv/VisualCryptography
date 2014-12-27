package lib.visualcryptiography.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class CryptIO {

    private static long initialMs;
    private static long durationMs;
    private static File dataFile;
    private static BufferedImage image;
    private static boolean on = true;
    private static BufferedWriter bw = null;

    private CryptIO() {
    }

    public static void notify(String msg) {
        System.out.println("~~" + msg);
    }

    public static void notifyErr(Object msg) {
        System.out.println("~~" + msg + "                                   !!");
    }

    public static void notifyResult(Object msg, boolean outWrite) {
        if (bw == null) {
            try {
                bw = new BufferedWriter(new FileWriter(new File("src/res/stats/output.txt")));
            } catch (Exception e) {
                CryptIO.notifyErr("Failed to initialize writer.");
            }
        }
        try {
            bw.write(msg + "\n");
            bw.flush();
        } catch (Exception e) {
            CryptIO.notifyErr("Failed to write a result: " + msg);
        }
        if (outWrite) {
            System.out.println(msg);
        }
    }

    public static void notifyResult(Object msg) {
        CryptIO.notifyResult(msg, true);
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
            try {
                if (!ImageIO.write(toWrite, "png", f)) {
                    return false;
                }
            } catch (Exception e) {
                CryptIO.notifyErr("failed to write image: " + e.getMessage());
            }
            notify("CryptIO wrote: " + name);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void setup() throws IOException {
        notify("CryptIO setup...");
        initialMs = System.currentTimeMillis();
        dataFile = (dataFile == null ? dataFile = new File("src/res/data.txt") : dataFile);
        dataFile.createNewFile();
    }

    public static void close() throws IOException {
        durationMs = System.currentTimeMillis() - initialMs;
        CryptIO.notifyResult("Duration: " + durationMs / 1000.0 + " seconds");
        if (bw != null) {
            bw.close();
        }
    }
}
