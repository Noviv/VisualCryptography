package lib.visualcryptiography.util;

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
        return Integer.parseInt(Integer.toString(Integer.valueOf("" + value, currentBase), goalBase));
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

    public static int generatePixelPair() {
        return 0;
//        return Integer.parseInt("" + (int) (Math.random() * 23) + "0") + Integer.parseInt("");
    }
}
