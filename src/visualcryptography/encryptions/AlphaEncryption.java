package visualcryptography.encryptions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lib.crypt.MessageInput;
import lib.crypt.EncryptionImage;
import lib.util.CryptIO;
import lib.util.CryptUtil;
import lib.util.CryptoFactory;
import lib.util.VisualStats;
import lib.util.datastructures.PixelDistribution;

public class AlphaEncryption {

    private EncryptionImage image;

    public void encrypt(String inputImagePath, String writeToPath) {
        /*preprocess*/
        try {
            CryptIO.setup();
        } catch (Exception e) {
        }
        CryptIO.notify("Pre-processing...");
        MessageInput input = new MessageInput(CryptIO.readText());
        CryptIO.notifyResult("INITIAL MESSAGE: " + input.getRaw(), false);
        Color[][] pixels = CryptIO.readPixels(inputImagePath);
        image = new EncryptionImage(pixels.length, pixels[0].length);

        /*create distributions*/
        CryptIO.notify("Creating distributions...");
        PixelDistribution dist = CryptoFactory.createDistribution(pixels);
        CryptIO.notify("Finished creating distributions.");

        //scatter
        long eTime = System.currentTimeMillis();
        CryptIO.notify("ENCRYPTION STARTED");
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                image.set(x, y, pixels[x][y]);
            }
        }
        int scatter = dist.getNumPixels() / input.getASCIIValues().size();
        final int INITIAL_SCATTER = scatter;
        CryptIO.notify("Writing alpha pixels to image...");
        for (int x = 0; x < input.getASCIIValues().size(); x++) {
            int temp = scatter;
            int count = 0;
            while (temp - dist.getWidth() > 0) {
                temp -= dist.getWidth();
                count++;
            }
            int xVal = temp;
            int yVal = count;
            if (xVal < 0) {
                xVal = 0;
            }
            if (xVal >= pixels.length) {
                xVal = pixels.length - 1;
            }
            if (yVal < 0) {
                yVal = 0;
            }
            if (yVal >= pixels[0].length) {
                yVal = pixels[0].length - 1;
            }
            int r = pixels[xVal][yVal].getRed();
            int g = pixels[xVal][yVal].getGreen();
            int b = pixels[xVal][yVal].getBlue();
            int a = (input.hasNext() ? input.getEncodedNext() : 0);
            int a1 = 255 - a / 2;
            int a2 = 255 - (a % 2 == 0 ? a / 2 : (a / 2) + 1);
            if (xVal + 1 >= dist.getWidth()) {
                image.set(xVal, yVal, new Color(r, g, b, a1));
                xVal = 0;
                image.set(xVal, yVal, new Color(r, g, b, a2));
            } else {
                image.set(xVal, yVal, new Color(r, g, b, a1));
                image.set(xVal + 1, yVal, new Color(r, g, b, a2));
            }
//            if (image.getRGB(xVal, yVal) != dist.getPixel(xVal, yVal).getRGB()) {
//                System.err.println("WTF");
//            }
            scatter += INITIAL_SCATTER;
        }
        CryptIO.notify("ENCRYPTION FINISHED");
        CryptIO.notifyResult("Encryption Duration: " + (System.currentTimeMillis() - eTime) / 1000.0 + "secs");

        /*stats*/
        VisualStats.runAlphaLayerPlot(dist);
        VisualStats.runAverage(image);
        VisualStats.runPSNR(CryptIO.readImage(inputImagePath), image);
        VisualStats.runSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);
        VisualStats.runDSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);

        /*postprocess*/
        CryptIO.notify("Post-processing...");
        CryptIO.write(image, writeToPath, "png");
    }

    public void decrypt(String outputImagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(outputImagePath));
        } catch (Exception e) {
            CryptIO.notifyErr("Image file " + outputImagePath + " could not be read.");
            System.exit(256);
        }
        CryptIO.notify("Reading alpha values...");
        Color c;
        ArrayList<Integer> data = new ArrayList<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() != 255 && c.getAlpha() != 0) {
                    data.add(c.getAlpha());
                }
            }
        }

        String message = "";
        for (int i = 0; i < data.size(); i += 2) {
            message += (char) ((255 - (int) data.get(i)) + (255 - (int) data.get(i + 1)));
        }
        CryptIO.notifyResult("Output Message: " + message);
        CryptIO.notifyResult("Worked: " + message.equals(CryptIO.readText()));
        try {
            CryptIO.close();
        } catch (Exception e) {
        }
    }
}
