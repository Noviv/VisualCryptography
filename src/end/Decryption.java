package end;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lib.visualcryptiography.io.CryptIO;

public class Decryption {

    public static void main(String[] args) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/res/share1.png"));
        } catch (Exception e) {
        }
        CryptIO.notifyProcess("Reading alpha values...");
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
        CryptIO.notifyResult("MESSAGE: " + message);
    }
}
