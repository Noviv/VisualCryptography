package visualcryptography.onebyone;

import java.awt.Color;
import java.util.ArrayList;
import lib.visualcryptiography.crypt.MessageInput;
import lib.visualcryptiography.crypt.Share;
import lib.visualcryptiography.crypt.VisualScheme;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.PixelDistributionFactory;
import lib.visualcryptiography.util.datastructures.FunctionalPixelDistribution;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class OneByOne extends VisualScheme {

    public OneByOne() {
        super(1, 1);
    }

    @Override
    public void makeShares() {
        /*preprocess*/
        try {
            CryptIO.setup();
        } catch (Exception e) {
        }
        CryptIO.notifyProcess("Pre-processing...");
        MessageInput input = new MessageInput(CryptIO.readText());
        Color[][] pixels = CryptIO.readPixels("src/res/house.jpg");
        Share s1 = new Share(1, pixels.length, pixels[0].length);

        /*create distributions*/
        CryptIO.notifyProcess("Creating distributions...");
        PixelDistribution dist = PixelDistributionFactory.createDistribution(pixels);
        FunctionalPixelDistribution fDist = PixelDistributionFactory.createFunctionalDistribution(dist);
        CryptIO.notifyProcess("Finished creating distributions.");

        //scatter
        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                s1.add(x, y, pixels[x][y]);
            }
        }
        int scatter = dist.getNumPixels() / input.getASCIIValues().size();
        final int INITIAL_SCATTER = scatter;
        CryptIO.notifySubProcess("Writing basic phase pixels to image...");
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
                s1.add(xVal, yVal, new Color(r, g, b, a1));
                xVal = 0;
                s1.add(xVal, yVal, new Color(r, g, b, a2));
            } else {
                s1.add(xVal, yVal, new Color(r, g, b, a1));
                s1.add(xVal + 1, yVal, new Color(r, g, b, a2));
            }

            scatter += INITIAL_SCATTER;
        }
//        for (int x = 0; x < digetWidth(); x++) {
//            for (int y = 0; y < dist.getHeight(); y++) {
//                if (x == 0 && y == 0) {
//                    continue;
//                }
//                int r = pixels[x][y].getRed();
//                int g = pixels[x][y].getGreen();
//                int b = pixels[x][y].getBlue();
//                int a = input.hasNext() ? input.getEncodedNext() : 255;
//                s1.add(x, y, new Color(r, g, b, a));
//            }
//        }
//        CryptIO.notifySubProcest.ss("Finished writing pixels.");

        /*postprocess*/
        CryptIO.notifyProcess("Post-processing...");
        shares[0] = s1;
        CryptIO.write(s1, "src/res/share" + s1.getShareNum() + ".png");
    }

    @Override
    public void decryptShares(Share... shares) {
        CryptIO.notifyProcess("Reading alpha values...");
        Color c;
        ArrayList<Integer> data = new ArrayList<>();
        for (int y = 0; y < shares[0].getHeight(); y++) {
            for (int x = 0; x < shares[0].getWidth(); x++) {
                c = new Color(shares[0].getRGB(x, y), true);
                if (c.getAlpha() != 255 && c.getAlpha() != 0) {
                    data.add(c.getAlpha());
                }
            }
        }

        String message = "";
        for (int i = 0; i < data.size(); i += 2) {
            message += (char) ((255 - (int) data.get(i)) + (255 - (int) data.get(i + 1)));
        }
        CryptIO.notifyResult("MESSAGE: " + message);
    }
}
