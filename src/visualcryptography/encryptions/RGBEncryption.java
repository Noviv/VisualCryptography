package visualcryptography.encryptions;

import java.awt.Color;
import lib.crypt.EncryptionImage;
import lib.crypt.MessageInput;
import lib.util.CryptIO;
import lib.util.CryptUtil;
import lib.util.PixelDistributionFactory;
import lib.util.VisualStats;
import lib.util.datastructures.PixelDistribution;

public class RGBEncryption {

    private EncryptionImage image;

    public void encrypt(String inputImagePath) {
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
        PixelDistribution dist = PixelDistributionFactory.createDistribution(pixels);
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

            if (input.hasNext()) {
                System.out.println("val1: " + (dist.getPixel(xVal, yVal).getRed() * dist.getPixel(xVal, yVal).getGreen() * dist.getPixel(xVal, yVal).getBlue()));
                System.out.println("hash: " + CryptIO.readValue(inputImagePath));
                System.out.println("val2: " + (CryptIO.readValue(inputImagePath) * (int) ((String) input.next()).charAt(0)));
            }

            //increment randomness
            scatter += INITIAL_SCATTER;
        }
        CryptIO.notify("ENCRYPTION FINISHED");
        CryptIO.notifyResult("Encryption Duration: " + (System.currentTimeMillis() - eTime) / 1000.0 + "secs");

        System.exit(0);

        /*stats*/
        VisualStats.runAlphaLayerPlot(dist);
        VisualStats.runAverage(image);
        VisualStats.runPSNR(CryptIO.readImage(inputImagePath), image);
        VisualStats.runSSIM(CryptUtil.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);
        VisualStats.runDSSIM(CryptUtil.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);

        /*postprocess*/
        CryptIO.notify("Post-processing...");
        CryptIO.write(image, "src/res/output.png", "png");
    }
}
