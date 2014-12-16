package visualcryptography;

import java.util.Scanner;
import visualcryptography.onebyone.OneByOne;

public class VisualCryptography {

    public static void main(String[] args) throws Exception {
        go();
    }

    private static void go() {
        Scanner input = new Scanner(System.in);
        int menu;

        String toEncrypt, result;

//        System.out.println("1. Hex Encryption");
//        System.out.println("2. (2, 2) Encryption");
//        System.out.println("3. (1, 1) Encryption");
//        System.out.print("Enter: ");
//
//        menu = input.nextInt();
//        switch (menu) {
//            case 1:
//                try {
//                    Hex hex = new Hex();
//                    System.out.println("\n\nEncrypting...");
//                    hex.encrypt();
//                    System.out.println("Decrypting...");
//                    result = hex.decrypt();
//                    System.out.println("Result: " + result + "\n\n");
//                } catch (CryptException e) {
//                }
//
//                break;
//            case 2:
//                TwoByTwo vs = new TwoByTwo();
//
//                System.out.println("\n\nEncrypting...");
//                vs.makeShares();
//                System.out.println("Decrypting...");
//                vs.decryptShares(vs.getShares());
//
//                break;
//            case 3:
        //generate random letters
//        try (PrintWriter bw = new PrintWriter(new File("src/res/data.txt"), "UTF-8")) {
//            for (int i = 0; i < 2169783; i++) {
//                bw.write("" + (char) ((int) (Math.random() * 27) + 65) + (i % 11 == 0 ? "\n" : ""));
//            }
//            bw.close();
//        } catch (Exception e) {
//        } finally {
//            System.out.println("DONE");
//        }
        OneByOne main = new OneByOne();

        System.out.println("\n\nEncrypting...");
        main.makeShares();
        System.out.println("Decrypting...");
        main.decryptShares(main.getShares());
//                break;
//            default:
//        }
        System.exit(0);
    }
}
