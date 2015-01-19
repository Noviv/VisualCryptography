package lib.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.crypt.EncryptionImage;
import lib.util.datastructures.Pixel;
import lib.util.datastructures.PixelDistribution;

public class CryptoFactory {

    private CryptoFactory() {
    }

    public static PixelDistribution createDistribution(Pixel[][] image) {
        double nSignal = 0.0, eSignal = 0.0, wSignal = 0.0, sSignal = 0.0;
        PixelDistribution distribution = new PixelDistribution(image);
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[x].length; y++) {
                try {
                    nSignal = (Math.abs(image[x][y - 1].getRed() - image[x][y].getRed()))
                            + (Math.abs(image[x][y - 1].getGreen() - image[x][y].getGreen()))
                            + (Math.abs(image[x][y - 1].getBlue() - image[x][y].getBlue()));
                } catch (Exception e) {
                }
                try {
                    eSignal = (Math.abs(image[x - 1][y].getRed() - image[x][y].getRed()))
                            + (Math.abs(image[x - 1][y].getGreen() - image[x][y].getGreen()))
                            + (Math.abs(image[x - 1][y].getBlue() - image[x][y].getBlue()));
                } catch (Exception e) {
                }
                try {
                    sSignal = (Math.abs(image[x][y + 1].getRed() - image[x][y].getRed()))
                            + (Math.abs(image[x][y + 1].getGreen() - image[x][y].getGreen()))
                            + (Math.abs(image[x][y + 1].getBlue() - image[x][y].getBlue()));
                } catch (Exception e) {
                }
                try {
                    wSignal = (Math.abs(image[x + 1][y].getRed() - image[x][y].getRed()))
                            + (Math.abs(image[x + 1][y].getGreen() - image[x][y].getGreen()))
                            + (Math.abs(image[x + 1][y].getBlue() - image[x][y].getBlue()));
                } catch (Exception e) {
                }
                distribution.setPositionFeeds(x, y, nSignal, eSignal, wSignal, sSignal);
            }
        }
        return distribution;
    }

    public static PixelDistribution createDistribution(Color[][] image) {
        Pixel[][] p = new Pixel[image.length][image[0].length];
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[x].length; y++) {
                p[x][y] = new Pixel(x, y, image[x][y]);
            }
        }
        return createDistribution(p);
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
