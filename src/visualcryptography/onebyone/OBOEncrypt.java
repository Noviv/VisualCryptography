package visualcryptography.onebyone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import lib.visualcryptiography.crypt.MessageInput;
import lib.visualcryptiography.crypt.Share;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.PixelDistributionFactory;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class OBOEncrypt {

    private Share share;

    public static void main(String[] args) {
        OBOEncrypt encrypt = new OBOEncrypt(args[0], new File(args[1]));
    }

    public OBOEncrypt(String message, File goalImage) {
        try {
            BufferedImage image = ImageIO.read(goalImage);
        } catch (Exception e) {
        }

        try {
            CryptIO.setup();
        } catch (Exception e) {
        }
        MessageInput input = new MessageInput(message);
        Color[][] pixels = CryptIO.readPixels(goalImage.getAbsolutePath());
        share = new Share(1, pixels.length, pixels[0].length);

        PixelDistribution dist = PixelDistributionFactory.createDistribution(pixels);

        for (int x = 0; x < dist.getWidth(); x++) {
            for (int y = 0; y < dist.getHeight(); y++) {
                share.add(x, y, pixels[x][y]);
            }
        }
        int scatter = dist.getNumPixels() / input.getASCIIValues().size();
        final int INITIAL_SCATTER = scatter;
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
                share.add(xVal, yVal, new Color(r, g, b, a1));
                xVal = 0;
                share.add(xVal, yVal, new Color(r, g, b, a2));
            } else {
                share.add(xVal, yVal, new Color(r, g, b, a1));
                share.add(xVal + 1, yVal, new Color(r, g, b, a2));
            }

            scatter += INITIAL_SCATTER;
        }

        CryptIO.write(share, goalImage.getAbsolutePath());
    }

    public Share get() {
        return share;
    }
}
