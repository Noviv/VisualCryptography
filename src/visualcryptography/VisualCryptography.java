package visualcryptography;

import visualcryptography.onebyone.OneByOne;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        OneByOne main = new OneByOne(false);

        System.out.println("\n\nEncrypting...");
        main.makeShares();
        System.out.println("Decrypting...");
        main.decryptShares(main.getShare());
        System.exit(0);
    }

    /*
     try {
     Hex hex = new Hex();
     System.out.println("\n\nEncrypting...");
     hex.encrypt();
     System.out.println("Decrypting...");
     result = hex.decrypt();
     System.out.println("Result: " + result + "\n\n");
     } catch (CryptException e) {
     }*/

    /*
     TwoByTwo vs = new TwoByTwo();
     System.out.println("\n\nEncrypting...");
     vs.makeShares();
     System.out.println("Decrypting...");
     vs.decryptShares(vs.getShares());*/
}
