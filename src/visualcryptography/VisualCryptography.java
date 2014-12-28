package visualcryptography;

import visualcryptography.encryptions.AlphaEncryption;
import java.io.File;
import javax.imageio.ImageIO;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        AlphaEncryption main = new AlphaEncryption();

        System.out.println("Encrypting...");
        main.encrypt();
        
        System.out.println("\nDecrypting...");
        main.decrypt(ImageIO.read(new File("src/res/share1.png")));
    }
}
