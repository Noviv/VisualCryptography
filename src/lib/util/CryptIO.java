package lib.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import static javax.print.attribute.ResolutionSyntax.DPI;
import pretty.GUI;

public class CryptIO {

    private static long initialMs;
    private static long durationMs;
    private static File dataFile;
    private static GUI gui = null;

    private CryptIO() {
    }

    public static void clearOutput() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/res/stats/output.txt"), false));
            bw.close();
            CryptIO.notify("Cleared output file.");
        } catch (Exception e) {
            CryptIO.notifyErr("Could not clear output: " + e.getMessage());
        }
    }

    public static void closeOutput() {
        System.out.close();
    }

    public static void setGUI(GUI gui_) {
        gui = gui_;
    }

    public static void notify(String msg) {
        if (gui != null) {
            gui.append(msg);
        }
        System.out.println("--" + msg);
    }

    public static void notifyErr(Object msg) {
        if (gui != null) {
            gui.append("!! " + msg);
        }
        System.out.println("!! " + msg);
    }

    public static void notifyResult(Object msg, boolean outWrite) {
        BufferedWriter bw = null;
        //try create
        try {
            bw = new BufferedWriter(new FileWriter(new File("src/res/stats/output.txt"), true));
        } catch (Exception e) {
            CryptIO.notifyErr("Failed to initialize writer.");
        }
        //try write
        try {
            bw.write(msg + "\n");
            bw.close();
        } catch (Exception e) {
            CryptIO.notifyErr("Failed to write a result: " + msg);
        }
        if (outWrite) {
            notify("" + msg);
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

    public static byte[] readBytes() {
        try {

        } catch (Exception e) {
        }
        return null;
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
        BufferedImage image = null;
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

    public static boolean write(BufferedImage toWrite, String fileLoc, String type) {
        try {
            File f = new File(fileLoc);
            if (f.createNewFile()) {
                CryptIO.notify("Forced to create new file " + fileLoc);
            }
            //write
            try {
                if (!ImageIO.write(toWrite, type, f)) {
                    return false;
                }
            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                    write(toWrite, fileLoc, type);
                }
                CryptIO.notifyErr("Failed to write image: " + e.getMessage());
            }
            notify("CryptIO wrote " + type + ": " + fileLoc);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean writeNew(BufferedImage toWrite, String fileLoc, String type, ImageWriteParam param) {
        try {
            for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(type); iw.hasNext();) {
                ImageWriter writer = iw.next();
                ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier
                        .createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
                IIOMetadata metadata = writer.getDefaultImageMetadata(
                        typeSpecifier, param);
                if (metadata.isReadOnly()
                        || !metadata.isStandardMetadataFormatSupported()) {
                    continue;
                }

                setDPI(metadata, DPI);

                ImageOutputStream stream = ImageIO.createImageOutputStream(new File(fileLoc));
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(toWrite, null, metadata), param);
                stream.close();
                break;
            }
        } catch (Exception e) {
            CryptIO.notifyErr("Could not write image: " + e.getMessage());
        }
        return false;
    }

    public static void setDPI(IIOMetadata metadata, int DPI) {
        try {
            double INCH_2_CM = 2.54;

            // for PNG, it's dots per millimeter
            double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;

            IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
            horiz.setAttribute("value", Double.toString(dotsPerMilli));

            IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
            vert.setAttribute("value", Double.toString(dotsPerMilli));

            IIOMetadataNode dim = new IIOMetadataNode("Dimension");
            dim.appendChild(horiz);
            dim.appendChild(vert);

            IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
            root.appendChild(dim);

            metadata.mergeTree("javax_imageio_1.0", root);
        } catch (Exception e) {
            CryptIO.notifyErr("Cannot set DPI: " + e.getMessage());
        }
    }

    public static void setup() throws IOException {
        notify("CryptIO setup...");
        initialMs = System.currentTimeMillis();
        dataFile = (dataFile == null ? dataFile = new File("src/res/data.txt") : dataFile);
        if (dataFile.createNewFile()) {
            CryptIO.notify("Forced to create new data.txt file.");
        }
    }

    public static void close() throws IOException {
        durationMs = System.currentTimeMillis() - initialMs;
        CryptIO.notifyResult("Duration: " + durationMs / 1000.0 + " seconds");
    }
}
