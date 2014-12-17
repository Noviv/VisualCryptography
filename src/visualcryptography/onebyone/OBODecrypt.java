package visualcryptography.onebyone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class OBODecrypt {

    private String message;

    public static void main(String[] args) {
        OBODecrypt decrypt = new OBODecrypt(new File(args[0]));
        System.out.println(decrypt.get());
    }

    public OBODecrypt(File f) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
        } catch (Exception e) {
        }

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

        for (int i = 0; i < data.size(); i += 2) {
            message += (char) ((255 - (int) data.get(i)) + (255 - (int) data.get(i + 1)));
        }
    }

    public String get() {
        return message;
    }
}
