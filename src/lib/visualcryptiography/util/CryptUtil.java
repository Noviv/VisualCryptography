package lib.visualcryptiography.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.visualcryptiography.crypt.Share;

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

    public static double sum(double... nums) {
        double n = 0.0;
        for (double num : nums) {
            n += num;
        }
        return n;
    }

    public static BufferedImage overlayTwoShares(Share s1, Share s2) {
        if (s1.getWidth() != s2.getWidth() || s1.getHeight() != s2.getHeight()) {
            CryptIO.notifyErr("Two overlay shares not same size.");
            return null;
        }

        //black = true
        BufferedImage overlay = new BufferedImage(s1.getWidth(), s1.getHeight(), BufferedImage.TYPE_INT_RGB);
        boolean[][] b1 = new boolean[s1.getWidth()][s1.getHeight()];
        boolean[][] b2 = new boolean[s2.getWidth()][s2.getHeight()];

        /*set flags to arrays*/
        for (int x = 0; x < s1.getWidth(); x++) {
            for (int y = 0; y < s1.getHeight(); y++) {
                b1[x][y] = s1.getRGB(x, y) == Color.BLACK.getRGB();
                b2[x][y] = s2.getRGB(x, y) == Color.BLACK.getRGB();
            }
        }

        /*set flags to overlay using OR*/
        for (int x = 0; x < overlay.getWidth(); x++) {
            for (int y = 0; y < overlay.getHeight(); y++) {
                overlay.setRGB(x, y, (b1[x][y] | b2[x][y] ? Color.BLACK.getRGB() : Color.WHITE.getRGB()));
            }
        }

        return overlay;
    }
}
