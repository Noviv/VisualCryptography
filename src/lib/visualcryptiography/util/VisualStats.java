package lib.visualcryptiography.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.datastructures.Pixel;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class VisualStats {

    private VisualStats() {
    }

    public static double PSNR(BufferedImage img1_, BufferedImage img2_) {
        CryptIO.notifyProcess("Calculating PSNR...");
        if (img1_.getWidth() != img2_.getWidth() || img1_.getHeight() != img2_.getHeight()) {
            CryptIO.notifySubProcess("Forced to resize img2_.");
        }

        double mse = 0.0;
        //inner dual sum for mse
        for (int m = 0; m < img1_.getWidth(); m++) {
            for (int n = 0; n < img1_.getHeight(); n++) {
                mse += Math.pow(img1_.getRGB(m, n) - img2_.getRGB(m, n), 2);
            }
        }
        //mse calc
        mse *= 1 / (img1_.getWidth() * img1_.getHeight());

        return 10 * (Math.log10(255 * 255) / Math.log10(mse));
    }

    public static void run(BufferedImage img1, BufferedImage img2) {
        //inner first
        int width = img1.getWidth();
        int height = img1.getHeight();

        //outer
        double r = 0.0;
        double r_diff = 0.0;
        double meanR1 = 0.0;
        double meanG1 = 0.0;
        double meanB1 = 0.0;
        double meanR2 = 0.0;
        double meanG2 = 0.0;
        double meanB2 = 0.0;

        //inner
        int counter = 0;

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                counter++;

                r_diff += Math.abs(img1.getRGB(x, y) - img2.getRGB(x, y));

                meanR1 += new Color(img1.getRGB(x, y)).getRed();
                meanG1 += new Color(img1.getRGB(x, y)).getGreen();
                meanB1 += new Color(img1.getRGB(x, y)).getBlue();
                meanR2 += new Color(img2.getRGB(x, y)).getRed();
                meanG2 += new Color(img2.getRGB(x, y)).getGreen();
                meanB2 += new Color(img2.getRGB(x, y)).getBlue();
            }
        }
        meanR1 /= counter;
        meanG1 /= counter;
        meanB1 /= counter;
        meanR2 /= counter;
        meanG2 /= counter;
        meanB2 /= counter;

        System.out.println("r: " + r);
        System.out.println("r_diff: " + r_diff);
        System.out.println("meanR1: " + meanR1);
        System.out.println("meanG1: " + meanG1);
        System.out.println("meanB1: " + meanB1);
        System.out.println("meanR2: " + meanR2);
        System.out.println("meanG2: " + meanG2);
        System.out.println("meanB2: " + meanB2);
    }

    public static boolean innerComparePixel(Pixel p1, Pixel p2, PixelDistribution pd) {
        return false;
    }
}
