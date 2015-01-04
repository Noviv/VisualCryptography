package visualcryptography;

import visualcryptography.encryptions.AlphaEncryption;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        AlphaEncryption main = new AlphaEncryption();

        System.out.println("Encrypting...");
        main.encrypt("src/res/input.png");

        System.out.println("Decrypting...");
        main.decrypt("src/res/output.png");
    }
}
