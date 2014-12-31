package lib.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.crypt.EncryptionImage;

public class CryptUtil {

    public static String unicodeEscaped(char ch) {
        if (ch < 0x10) {
            return "\\u000" + Integer.toHexString(ch);
        } else if (ch < 0x100) {
            return "\\u00" + Integer.toHexString(ch);
        } else if (ch < 0x1000) {
            return "\\u0" + Integer.toHexString(ch);
        }
        return "\\u" + Integer.toHexString(ch);
    }

    public static double log10(double val) {
        return Math.log(val) / Math.log(10);
    }

    public static int toBase(int value, int currentBase, int goalBase) {
        try {
            return Integer.parseInt(Integer.toString(Integer.valueOf("" + value, currentBase), goalBase));
        } catch (Exception e) {
            return 0;
        }
    }

    public static byte[] convertToByteAry(Object[] bytes) {
        byte[] ary = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ary[i] = Byte.parseByte("" + bytes[i]);
        }
        return ary;
    }

    public static double toLuma(int r, int g, int b) {
        return Math.sqrt(0.299 * r * r + 0.587 * g * g + 0.114 * b * b);
    }

    public static double toLuma(int rgb) {
        return toLuma((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF);
    }

    public static double toLuma(Color c) {
        return toLuma(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static EncryptionImage convertToEncryptionImage(BufferedImage image) {
        EncryptionImage newImage = new EncryptionImage(image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                newImage.set(x, y, image.getRGB(x, y));
            }
        }
        return newImage;
    }
}
