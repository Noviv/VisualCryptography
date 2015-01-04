package lib.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.crypt.EncryptionImage;
import lib.util.datastructures.PixelDistribution;

public class VisualStats {

    private static final int DIFF_CUTOFF_VALUE = 7;

    private VisualStats() {
    }

    public static void runPSNR(BufferedImage pre, BufferedImage post) {
        CryptIO.notify("Calculating PSNR...");

        double mseR = 0.0;
        double mseG = 0.0;
        double mseB = 0.0;
        double mseA = 0.0;
        //inner dual sum for mse
        Color c1;
        Color c2;
        for (int m = 0; m < pre.getWidth(); m++) {
            for (int n = 0; n < pre.getHeight(); n++) {
                c1 = new Color(pre.getRGB(m, n), true);
                c2 = new Color(post.getRGB(m, n), true);

                mseR += Math.pow(c1.getRed() - c2.getRed(), 2);
                mseG += Math.pow(c1.getGreen() - c2.getGreen(), 2);
                mseB += Math.pow(c1.getBlue() - c2.getBlue(), 2);
                mseA += Math.pow(c1.getAlpha() - c2.getAlpha(), 2);
            }
        }
        //mse calc
        mseR *= 1.0 / (pre.getWidth() * pre.getHeight());
        mseG *= 1.0 / (pre.getWidth() * pre.getHeight());
        mseB *= 1.0 / (pre.getWidth() * pre.getHeight());
        mseA *= 1.0 / (pre.getWidth() * pre.getHeight());

        //psnr calc
        double psnrR = 10.0 * (Math.log10(255.0 * 255.0 / mseR));
        double psnrG = 10.0 * (Math.log10(255.0 * 255.0 / mseR));
        double psnrB = 10.0 * (Math.log10(255.0 * 255.0 / mseR));
        double psnrA = 10.0 * (Math.log10(255.0 * 255.0 / mseA));

        CryptIO.notifyResult("MSE_R: " + mseR);
        CryptIO.notifyResult("MSE_G: " + mseG);
        CryptIO.notifyResult("MSE_B: " + mseB);
        CryptIO.notifyResult("MSE_A: " + mseA);
        CryptIO.notifyResult("PSNR_R: " + psnrR);
        CryptIO.notifyResult("PSNR_B: " + psnrG);
        CryptIO.notifyResult("PSNR_G: " + psnrB);
        CryptIO.notifyResult("PSNR_A: " + psnrA);
    }

    public static void runAverage(BufferedImage img) {
        CryptIO.notify("Calculating averages...");

        //averages
        double meanR = 0.0;
        double meanG = 0.0;
        double meanB = 0.0;
        double meanA = 0.0;

        Color c;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                c = new Color(img.getRGB(x, y), true);
                meanR += c.getRed();
                meanG += c.getGreen();
                meanB += c.getBlue();
                meanA += c.getAlpha();
            }
        }
        meanR /= img.getWidth() * img.getHeight();
        meanG /= img.getWidth() * img.getHeight();
        meanB /= img.getWidth() * img.getHeight();
        meanA /= img.getWidth() * img.getHeight();

//        meanR = Math.round(meanR * 100.0) / 100.0;
//        meanG = Math.round(meanG * 100.0) / 100.0;
//        meanB = Math.round(meanB * 100.0) / 100.0;
        CryptIO.notifyResult("meanR: " + meanR);
        CryptIO.notifyResult("meanG: " + meanG);
        CryptIO.notifyResult("meanB: " + meanB);
        CryptIO.notifyResult("meanA: " + meanA);
    }

    public static void runAlphaLayerPlot(PixelDistribution pd) {
        CryptIO.notify("Creating alpha layer plots...");

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

        CryptIO.write(rI, "src/res/stats/rI.png", "png");
        CryptIO.write(gI, "src/res/stats/gI.png", "png");
        CryptIO.write(bI, "src/res/stats/bI.png", "png");
    }

    public static void runSSIM(EncryptionImage pre, EncryptionImage post) {
        CryptIO.notify("Calculating SSIM...");
        double ssim = ssim(pre, post);
        CryptIO.notifyResult("SSIM: " + ssim);
    }

    public static void runDSSIM(EncryptionImage pre, EncryptionImage post) {
        CryptIO.notify("Calculating DSSIM...");
        double dssim = (1.0 - ssim(pre, post)) / 2.0;
        CryptIO.notifyResult("DSSIM: " + dssim);
    }

    /*private methods*/
    private static Color genColor(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }

    private static double covar(EncryptionImage xImg, EncryptionImage yImg) {
        double covar = 0.0;

        for (int x = 0; x < xImg.getWidth(); x++) {
            for (int y = 0; y < xImg.getHeight(); y++) {
                covar += CryptUtil.toLuma(xImg.getRGB(x, y)) - xImg.getAverageLuma() * CryptUtil.toLuma(yImg.getRGB(x, y)) - yImg.getAverageLuma();
            }
        }
        covar /= (xImg.getWidth() * xImg.getHeight()) - 1.0;
        return Math.round(covar * 100.0) / 100.0;
    }

    private static double ssim(EncryptionImage pre, EncryptionImage post) {
        final double k1 = .01;
        final double k2 = .03;
        final int L = 255;

        double muX = pre.getAverageLuma();
        double muY = post.getAverageLuma();
        double sigmaX = pre.getStdDevLuma();
        double sigmaY = post.getStdDevLuma();
        double sigmaXY = covar(pre, post);
        double c1 = Math.pow(k1 * L, 2);
        double c2 = Math.pow(k2 * L, 2);

        double val = ((2 * muX * muY + c1) * (2 * sigmaXY + c2)) / ((muX * muX + muY * muY + c1) * (sigmaX * sigmaX + sigmaY * sigmaY + c2));
//        val = Math.round(100.0 * val) / 100.0;
        return val;
    }
}
