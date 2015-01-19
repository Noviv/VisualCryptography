package visualcryptography.encryptions;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lib.crypt.EncryptionImage;
import lib.crypt.MessageInput;
import lib.util.CryptIO;
import lib.util.CryptoFactory;
import lib.util.VisualStats;
import lib.util.datastructures.Pixel;
import lib.util.datastructures.PixelDistribution;
import lib.util.datastructures.TriMap;

public class RGBEncryption {

    private double CONSTANT_CORRECTION = 70.0;//dt

    private EncryptionImage image;
    private File imageFile;

    public void encrypt(String inputImagePath, String writeToPath) {
        /*preprocess*/
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

        /*encryption pre*/
        long eTime = System.currentTimeMillis();
        CryptIO.notify("ENCRYPTION STARTED");
        /*encryption*/
        ArrayList<Pixel> signalPixels = new ArrayList<>();
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                image.set(x, y, pixels[x][y]);
            }
        }

        CryptIO.notify("Modifying signal pixels...");
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                if (dist.getPixel(x, y).getVMulti() == imageFile.lastModified() * (int) ((String) input.next()).charAt(0) / CONSTANT_CORRECTION) {
                    //set signal pixel
                    image.set(x, y, new Color(dist.getPixel(x, y).getRed(), dist.getPixel(x, y).getBlue(), dist.getPixel(x, y).getGreen(), 254));
                    signalPixels.add(dist.getPixel(x, y));
                    CryptIO.notify("Set signal pixel at: (" + x + ", " + y + ")");
                } else {
                    input.back();
                }
            }
        }

        if (signalPixels.size() != input.getASCIIValues().size()) {
            CryptIO.notify("Forced to go into modify loop.");

            //fix correction constant
            int iterations = 0;
            double vM = 0.0;
            double result = 0.0;
            TriMap triMap = new TriMap();
            double lastAverage = Double.MAX_VALUE;
            double lastLastAverage = Double.MAX_VALUE;
            while (triMap.getAverage() < lastAverage && triMap.getAverage() < lastLastAverage) {
                CryptIO.notify("Forced constant correction iteration " + (iterations + 1));
                for (int x = 0; x < dist.getWidth(); x++) {
                    for (int y = 0; y < dist.getHeight(); y++) {
                        vM = dist.getPixel(x, y).getVMulti();
                        result = (imageFile.lastModified() * input.getAverageASCII()) / CONSTANT_CORRECTION;
                        triMap.put(x, y, vM - result);
                    }
                }
                lastLastAverage = lastAverage;
                lastAverage = triMap.getAverage();
                triMap.clear();
                CONSTANT_CORRECTION *= 1.05;//optional binary selection
                iterations++;
            }
            CryptIO.notifyResult("Fixed CORRECTION_CONSTANT: " + CONSTANT_CORRECTION + " (" + (iterations + 1) + " iterations)");

            //modify signal pixels
            iterations = 0;
            vM = 0.0;
            result = 0.0;
            triMap.clear();

            iterations = 0;
            double goal = imageFile.lastModified() * input.getNextCharInt() / CONSTANT_CORRECTION;
            double currentPercentDiff = 0.03;
            while (signalPixels.size() != input.getASCIIValues().size()) {
                CryptIO.notify("Forced into modify loop on iteration " + (iterations + 1));
                for (int x = 0; x < dist.getWidth(); x++) {
                    for (int y = 0; y < dist.getHeight(); y++) {
                        vM = dist.getPixel(x, y).getVMulti();
                        triMap.put(x, y, vM - dist.getPixel(x, y).getVMulti());
                    }
                }
                int xMin = triMap.getMinValX();
                int yMin = triMap.getMinValY();

                int pR = dist.getPixel(xMin, yMin).getRed();
                int pG = dist.getPixel(xMin, yMin).getGreen();
                int pB = dist.getPixel(xMin, yMin).getBlue();
                int nR = 0, nG = 0, nB = 0, nA;
                nA = 254;

                pixelModificationLoops:
                {
                    for (int r = (int) Math.ceil((1.0 - currentPercentDiff) * pR); r < Math.floor((1.0 + currentPercentDiff) * pR); r++) {
                        for (int g = (int) Math.ceil((1.0 - currentPercentDiff) * pG); r < Math.floor((1.0 + currentPercentDiff) * pB); g++) {
                            for (int b = (int) Math.ceil((1.0 - currentPercentDiff) * pB); r < Math.floor((1.0 + currentPercentDiff) * pB); b++) {
                                if (r * g * b == goal) {
                                    nR = r;
                                    nG = g;
                                    nB = b;
                                    break pixelModificationLoops;
                                }
                            }
                        }
                    }
                }

                //modify pixel
                image.set(xMin, yMin, new Color(nR, nG, nB, nA));
                signalPixels.add(dist.getPixel(xMin, yMin));
                iterations++;
            }
        }

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
        CryptIO.write(image, writeToPath, "png");
        try {
            CryptIO.close();
        } catch (Exception e) {
        }
    }

    public void decrypt(String outputImagePath) {
        //pre
        try {
            CryptIO.setup();
        } catch (Exception e) {
        }
        File f = new File(outputImagePath);
        if (!f.exists()) {
            CryptIO.notify("");
            System.exit(256);
        }
        EncryptionImage image = null;
        try {
            image = CryptoFactory.convertToEncryptionImage(ImageIO.read(new File(outputImagePath)));
        } catch (Exception e) {
            CryptIO.notifyErr("Image file " + outputImagePath + " could not be read.");
            System.exit(256);
        }
        //decryption
        String message = "";
        Color c;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                CryptIO.notify("matching (" + x + ", " + y + ")");
                c = new Color(image.getRGB(x, y), true);
                for (int i = 0; i < 510; i++) {
                    if ((double) c.getRed() * c.getGreen() * c.getBlue() == (f.lastModified() * i) / 70.0) {
                        message += (char) i;
                        System.out.println("(" + i + ") -> " + ((char) i) + "       FOUND");
                    }
                }
            }
        }

        /*post results*/
        CryptIO.notifyResult("Output Message: " + message);
        CryptIO.notifyResult("Worked: " + message.equals(CryptIO.readText()));

        /*postprocess*/
        try {
            CryptIO.close();
        } catch (Exception e) {
        }
    }
}
