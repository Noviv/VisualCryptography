package visualcryptography.encryptions;

import java.awt.Color;
import java.io.File;
import lib.crypt.EncryptionImage;
import lib.crypt.MessageInput;
import lib.util.CryptIO;
import lib.util.CryptoFactory;
import lib.util.VisualStats;
import lib.util.datastructures.PixelDistribution;

public class RGBNewEncryption {

    public void encrypt(String inputImagePath, String outputImagePath) {
        /*preprocess*/
        File imageFile;
        EncryptionImage image;
        try {
            CryptIO.setup();
        } catch (Exception e) {
        }
        imageFile = new File(inputImagePath);
        if (!imageFile.exists()) {
            CryptIO.notifyErr("Could not initialize image file. Create a new image path.");
            System.exit(0);
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

        /*encryption start*/
        CryptIO.notify("Started encryption...");
        long eTime = System.currentTimeMillis();
        long timeModified = imageFile.lastModified();
        
        /*encryption post*/
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
        CryptIO.write(image, outputImagePath, "png");
        try {
            CryptIO.close();
        } catch (Exception e) {
        }
    }

    public void decrypt(String outputImageFile) {

    }
}
