package end;

import java.awt.Color;
import lib.visualcryptiography.crypt.MessageInput;
import lib.visualcryptiography.crypt.Share;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.PixelDistributionFactory;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class Encryption {

    public static void main(String[] args) {
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

        /*postprocess*/
        CryptIO.notifyProcess("Post-processing...");
        CryptIO.write(s1, "src/res/share" + s1.getShareNum() + ".png");
    }
}
