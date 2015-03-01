package visualcryptography;

import lib.util.CryptIO;
import visualcryptography.encryptions.AlphaEncryption;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        AlphaEncryption main = new AlphaEncryption();

        CryptIO.notify("Encrypting...");
        main.encrypt("src/res/trial4.png", "src/res/trial4output.png");

        CryptIO.notify("Decrypting...");
        main.decrypt("src/res/trial4output.png");
//        
//        RGBEncryption main = new RGBEncryption();
//
//        CryptIO.notify("Encrypting...");
//        main.encrypt("src/res/trial2.png", "src/res/trial2output.png");
//
//        CryptIO.notify("Decrypting...");
//        main.decrypt("src/res/trial2output.png");
//        
//        RGBTimeDiffEncryption main = new RGBTimeDiffEncryption();
//
//        System.out.println("Encrypting...");
//        main.encrypt("src/res/trial2.png", "src/res/trial2output.png");
//
//        System.out.println("Decrypting...");
//        main.decrypt("src/res/trial2output.png");
//        
//        RGBFileChooseEncryption main = new RGBFileChooseEncryption();
//
//        System.out.println("Encrypting...");
//        main.encrypt("src/res/video.mp4", "src/res/trial1.png", "src/res/videooutput.png");
//
//        System.out.println("Decrypting...");
//        main.decrypt("src/res/videooutput.png");

    }
}
