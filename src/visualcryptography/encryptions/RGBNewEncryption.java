package visualcryptography.encryptions;

import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import lib.crypt.EncryptionImage;
import lib.crypt.MessageInput;
import lib.util.CryptIO;
import lib.util.CryptoFactory;
import lib.util.VisualStats;
import lib.util.datastructures.Pixel;
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
        //time
        long timeDiff = -1;
        try {
            BasicFileAttributes attr = Files.readAttributes(imageFile.toPath(), BasicFileAttributes.class);
            timeDiff = attr.lastAccessTime().toMillis() - attr.lastModifiedTime().toMillis();
        } catch (Exception e) {
            CryptIO.notifyErr("Could not read file attributes.");
            System.exit(256);
        }
        CryptIO.notifyResult("Time Diff: " + timeDiff);

        //presetup
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                image.set(x, y, dist.getPixel(x, y));
            }
        }

        //super
        double goal = input.getNextCharInt() * timeDiff;
        ArrayList<Pixel> signalPixels = new ArrayList<>();

        //attempt to normal write
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                if (dist.getPixel(x, y).getVMulti() == goal) {
                    signalPixels.add(dist.getPixel(x, y));
                    CryptIO.notify("Added signal pixel " + input.getPosition() + " of " + input.getRaw().length() + ".");
                    goal = input.getNextCharInt() * timeDiff;
                }
            }
        }

        //fix
        System.exit(1000);
        while (input.hasNext()) {
        }

        //check working
        /*encryption post*/
        CryptIO.notify("ENCRYPTION FINISHED");
        CryptIO.notifyResult("Encryption Duration: " + (System.currentTimeMillis() - eTime) / 1000.0 + "secs");

        /*postprocess*/
        CryptIO.notify("Post-processing...");
        CryptIO.write(image, outputImagePath, "png");

        /*stats*/
        VisualStats.runAlphaLayerPlot(dist);
        VisualStats.runAverage(image);
        VisualStats.runPSNR(CryptIO.readImage(inputImagePath), image);
        VisualStats.runSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);
        VisualStats.runDSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);

        try {
            CryptIO.close();
        } catch (Exception e) {
        }
    }

    public void decrypt(String outputImageFile) {

    }
}
