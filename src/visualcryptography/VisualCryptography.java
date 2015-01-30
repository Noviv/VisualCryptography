package visualcryptography;

import lib.util.CryptIO;
import visualcryptography.encryptions.AlphaEncryption;
import visualcryptography.encryptions.RGBEncryption;
import visualcryptography.encryptions.RGBNewEncryption;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        AlphaEncryption main = new AlphaEncryption();

        CryptIO.notify("Encrypting...");
        main.encrypt("src/res/trial4.png", "src/res/trial4output.png");

        CryptIO.notify("Decrypting...");
        main.decrypt("src/res/trial4output.png");

//        RGBEncryption main = new RGBEncryption();
//
//        CryptIO.notify("Encrypting...");
//        main.encrypt("src/res/trial4.png", "src/res/trial4output.png");
//
//        CryptIO.notify("Decrypting...");
//        main.decrypt("src/res/trial4output.png");
        
//        RGBNewEncryption main = new RGBNewEncryption();
//
//        CryptIO.notify("Encrypting...");
//        main.encrypt("src/res/trial4.png", "src/res/trial4output.png");
//
//        CryptIO.notify("Decrypting...");
//        main.decrypt("src/res/trial4output.png");
    }
}
