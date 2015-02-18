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

    private double CONSTANT_CORRECTION = 50000000.0;//dt

    private EncryptionImage image;
    private File imageFile;

    public void encrypt(String inputImagePath, String writeToPath) {
        /*preprocess*/
        CryptIO.clearOutput();
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
        double[] initialAvg = VisualStats.runAverage(CryptIO.readImage(inputImagePath), false);
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
                if (dist.getPixel(x, y).getVMulti() == imageFile.lastModified() * ((String) input.next()).charAt(0) / CONSTANT_CORRECTION) {
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
            boolean worked = false;
            double goal = imageFile.lastModified() * input.getAverageASCII() / CONSTANT_CORRECTION;
            while (!worked) {
                CryptIO.notify("Forced constant correction iteration " + (iterations + 1));
                for (int x = 0; x < dist.getWidth(); x++) {
                    for (int y = 0; y < dist.getHeight(); y++) {
                        worked = (goal == dist.getPixel(x, y).getVMulti());
                    }
                }

                CONSTANT_CORRECTION += 1000000;
                iterations++;
                goal = (imageFile.lastModified() * input.getAverageASCII()) / CONSTANT_CORRECTION;
            }
            CryptIO.notifyResult("Fixed CORRECTION_CONSTANT: " + CONSTANT_CORRECTION + " (" + (iterations + 1) + " iterations)");

            //modify signal pixels
            iterations = 0;
            goal = imageFile.lastModified() * input.getNextCharInt() / CONSTANT_CORRECTION;
            double currentPercentDiff = 0.1;
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
                    for (int r = (int) (1.0 - currentPercentDiff * pR); r < (1.0 + currentPercentDiff * pR); r++) {
                        for (int g = (int) (1.0 - currentPercentDiff * pG); g < (1.0 + currentPercentDiff * pG); g++) {
                            for (int b = (int) (1.0 - currentPercentDiff * pB); b < (1.0 + currentPercentDiff * pB); b++) {
                                if (r * g * b == goal) {
                                    CryptIO.notify("Found matching pair.");
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

                if (input.hasNext()) {
                    goal = imageFile.lastModified() * input.getNextCharInt() / CONSTANT_CORRECTION;
                } else {
                    break;
                }
            }
        }

        /*encryption post*/
        CryptIO.notify("ENCRYPTION FINISHED");
        CryptIO.notifyResult("Encryption Duration: " + (System.currentTimeMillis() - eTime) / 1000.0 + "secs");

        /*postprocess*/
        CryptIO.notify("Post-processing...");
        CryptIO.write(image, writeToPath, "png");

        /*stats*/
        VisualStats.runAlphaLayerPlot(dist);
        double[] finalAvg = VisualStats.runAverage(image, false);
        CryptIO.notifyResult("meanDiffR: " + (Math.abs(initialAvg[0] - finalAvg[0])));
        CryptIO.notifyResult("meanDiffG: " + (Math.abs(initialAvg[1] - finalAvg[1])));
        CryptIO.notifyResult("meanDiffB: " + (Math.abs(initialAvg[2] - finalAvg[2])));
        CryptIO.notifyResult("meanDiffA: " + (Math.abs(initialAvg[3] - finalAvg[3])));
        VisualStats.runPSNR(CryptIO.readImage(inputImagePath), image);
        VisualStats.runSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);
        VisualStats.runDSSIM(CryptoFactory.convertToEncryptionImage(CryptIO.readImage(inputImagePath)), image);

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
        ArrayList<Pixel> signals = new ArrayList<>();
        String message = "";
        Color c;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() == 254) {
                    signals.add(new Pixel(x, y, c));
                }
            }
        }

        CryptIO.notifyResult("Number of signals: " + signals.size());
        for (Pixel p : signals) {
            message += (char) (p.getVMulti() * 1000000.0) / f.lastModified();
            CryptIO.notify("FOUND: " + (p.getVMulti() * 70.0) / f.lastModified() + " = " + (char) (p.getVMulti() * 70.0) / f.lastModified());
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
