package lib.crypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import lib.util.CryptUtil;

public class EncryptionImage extends BufferedImage {

    private final HashMap<Integer, Integer> used;
    private double averageLuma = 0.0;
    private double stdDevLuma = 0.0;

    public EncryptionImage(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        used = new HashMap<>();
    }

    public void set(int x, int y, Color c) {
        setRGB(x, y, c.getRGB());
    }

    public void set(int x, int y, int rgb) {
        setRGB(x, y, rgb);
    }

    public double getAverageLuma() {
        if (averageLuma == 0.0) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    averageLuma += CryptUtil.toLuma(getRGB(x, y));
                }
            }
            averageLuma /= getWidth() * getHeight();
        }
        return averageLuma;
    }

    public double getStdDevLuma() {
        if (stdDevLuma == 0.0) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    stdDevLuma += Math.pow(CryptUtil.toLuma(getRGB(x, y)) - getAverageLuma(), 2);
                }
            }
            stdDevLuma *= getWidth() * getHeight();
        }
        stdDevLuma = Math.sqrt(stdDevLuma);
        return stdDevLuma;
    }
}
