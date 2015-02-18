package lib.util;

import java.awt.Color;

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

    public static double toLuma(int r, int g, int b, int a) {
        return (0.2126 * r + 0.7152 * g + 0.0722 * b) * (a / 255.0);
    }

    public static double toLuma(int rgb) {
        return toLuma((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF, (rgb >> 24) & 0xff);
    }

    public static double toLuma(Color c) {
        return toLuma(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}
