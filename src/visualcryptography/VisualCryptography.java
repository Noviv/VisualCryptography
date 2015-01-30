package visualcryptography;

import lib.util.CryptIO;
import visualcryptography.encryptions.AlphaEncryption;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        AlphaEncryption main = new AlphaEncryption();

        CryptIO.notify("Encrypting...");
        main.encrypt("src/res/trial3.png", "src/res/trial3output.png");

        CryptIO.notify("Decrypting...");
        main.decrypt("src/res/trial3output.png");

//        RGBEncryption main = new RGBEncryption();
//
//        CryptIO.notify("Encrypting...");
//        main.encrypt("src/res/trial2.png", "src/res/trial2output.png");
//
//        CryptIO.notify("Decrypting...");
//        main.decrypt("src/res/trial2output.png");
        
//        RGBNewEncryption main = new RGBNewEncryption();
//
//        CryptIO.notify("Encrypting...");
//        main.encrypt("src/res/trial2.png", "src/res/trial2output.png");
//
//        CryptIO.notify("Decrypting...");
//        main.decrypt("src/res/trial2output.png");
    }
}
