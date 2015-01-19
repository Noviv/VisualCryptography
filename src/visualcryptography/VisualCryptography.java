package visualcryptography;

import visualcryptography.encryptions.RGBEncryption;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        RGBEncryption main = new RGBEncryption();

        System.out.println("Encrypting...");
        main.encrypt("src/res/trial1.png", "src/res/trial1output.png");

        System.out.println("Decrypting...");
        main.decrypt("src/res/trial1output.png");
    }
}
