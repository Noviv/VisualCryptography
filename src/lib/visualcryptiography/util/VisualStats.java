package lib.visualcryptiography.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.datastructures.Pixel;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class VisualStats {

    private static final int DIFF_CUTOFF_VALUE = 7;

    private VisualStats() {
    }

    public static void runPSNR(BufferedImage img1, BufferedImage img2) {
        CryptIO.notifySubProcess("Calculating PSNR...");

        double mse = 0.0;
        //inner dual sum for mse
        Color c1;
        Color c2;
        for (int m = 0; m < img1.getWidth(); m++) {
            for (int n = 0; n < img1.getHeight(); n++) {
                c1 = new Color(img1.getRGB(m, n), true);
                c2 = new Color(img2.getRGB(m, n), true);
                mse += Math.pow(c1.getRGB() - c2.getRGB(), 2);
            }
        }
        //mse calc
        mse *= 1.0 / (img1.getWidth() * img1.getHeight());

        CryptIO.notifyResult("MSE: " + mse);
        CryptIO.notifyResult("PSNR: " + 10.0 * (Math.log10(255.0 * 255.0) / Math.log10(mse)));
    }

    public static void runAverage(BufferedImage img) {
        CryptIO.notifySubProcess("Calculating average...");

        int width = img.getWidth();
        int height = img.getHeight();

        //averages
        double meanR = 0.0;
        double meanG = 0.0;
        double meanB = 0.0;

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                meanR += new Color(img.getRGB(x, y)).getRed();
                meanG += new Color(img.getRGB(x, y)).getGreen();
                meanB += new Color(img.getRGB(x, y)).getBlue();
            }
        }
        meanR /= width * height;
        meanG /= width * height;
        meanB /= width * height;

        CryptIO.notifyResult("meanR: " + meanR);
        CryptIO.notifyResult("meanG: " + meanG);
        CryptIO.notifyResult("meanB: " + meanB);
    }

    public static void runAlphaLayerPlot(PixelDistribution pd) {
        CryptIO.notifyProcess("Creating alpha layer plots...");

        Color[][] r = new Color[pd.getWidth()][pd.getHeight()];
        Color[][] g = new Color[pd.getWidth()][pd.getHeight()];
        Color[][] b = new Color[pd.getWidth()][pd.getHeight()];
        BufferedImage rI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        BufferedImage gI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        BufferedImage bI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

        for (int x = 0; x < pd.getWidth(); x++) {
            for (int y = 0; y < pd.getHeight(); y++) {
                r[x][y] = genColor(Color.RED, pd.getPixel(x, y).getRed());
                g[x][y] = genColor(Color.GREEN, pd.getPixel(x, y).getGreen());
                b[x][y] = genColor(Color.BLUE, pd.getPixel(x, y).getBlue());
                rI.setRGB(x, y, r[x][y].getRGB());
                gI.setRGB(x, y, g[x][y].getRGB());
                bI.setRGB(x, y, b[x][y].getRGB());
            }
        }

        CryptIO.write(rI, "src/res/stats/rI.png");
        CryptIO.write(gI, "src/res/stats/gI.png");
        CryptIO.write(bI, "src/res/stats/bI.png");
        CryptIO.notifyProcess("Finished creating alpha layer plot images.");
    }

    public static boolean innerComparePixel(Pixel p1, Pixel p2) {
        return Math.abs(p1.getRed() - p2.getRed()) < DIFF_CUTOFF_VALUE && Math.abs(p1.getGreen() - p2.getGreen()) < DIFF_CUTOFF_VALUE && Math.abs(p1.getBlue() - p2.getBlue()) < DIFF_CUTOFF_VALUE;
    }

    /*private methods*/
    private static Color genColor(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }
}
