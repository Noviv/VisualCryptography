package lib.visualcryptiography.crypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.visualcryptiography.io.CryptIO;

public class ShareUtil {

    private ShareUtil() {
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
