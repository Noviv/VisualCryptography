package visualcryptography.encryptions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lib.util.CryptIO;

public class RGBFileChooseEncryption {

    public void encrypt(String videoPath, String encryptionMediumPath, String writeToPath) {
        try {
            byte[] bytes = Files.readAllBytes(new File(videoPath).toPath());
            AlphaEncryption alpha = new AlphaEncryption();
            FileWriter fw = new FileWriter("src/res/data.txt");
            for (byte b : bytes) {
                fw.write(b + "\n");
            }
            fw.close();
            alpha.encrypt(encryptionMediumPath, writeToPath);
        } catch (Exception e) {
            CryptIO.notifyErr("Error: " + e.getMessage());
            System.exit(256);
        }

    }

    public void decrypt(String outputImagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(outputImagePath));
        } catch (Exception e) {
            CryptIO.notifyErr("Image file " + outputImagePath + " could not be read.");
            System.exit(256);
        }
        CryptIO.notify("Reading alpha values...");
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

        System.out.println(message);

        try {
            FileOutputStream out = new FileOutputStream("src/res/videoutput.mp4");
//            out.write(bytes);
            out.close();
        } catch (Exception e) {
        }
    }
}
