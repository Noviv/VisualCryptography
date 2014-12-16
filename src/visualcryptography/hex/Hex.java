package visualcryptography.hex;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import lib.visualcryptiography.io.CryptIO;
import visualcryptography.exceptions.CryptException;
import visualcryptography.interfaces.EncryptionInterface;

public class Hex implements EncryptionInterface {

    private final Color BLANK_SPACE_COLOR = Color.WHITE;

    @Override
    public void encrypt() throws CryptException {
        try {
            CryptIO.setup();
        } catch (IOException e) {
        }

        String msg = CryptIO.readText();

        if (msg.equals("")) {
            CryptIO.notifyErr("Quitting encryption because text is empty.");
            return;
        } else {
            CryptIO.notifyProcess("Writing message: " + msg.substring(0, msg.length() - 1));
        }

        /*convert to hex string[]*/
        String[] hexValues = new String[msg.length()];
        for (int i = 0; i < msg.length(); i++) {
            hexValues[i] = String.format("%04x", (int) msg.charAt(i));
        }

        /*fix string length*/
        for (int i = 0; i < hexValues.length; i++) {
            int spacesNeeded = 8 - hexValues[i].length();
            String subString = "";
            for (int i_ = 0; i_ < spacesNeeded; i_++) {
                subString += "0";
            }
            subString += hexValues[i];
            hexValues[i] = subString;
        }

        /*make new colors*/
        Color[] pixelColors = new Color[hexValues.length];
        for (int i = 0; i < pixelColors.length; i++) {
            pixelColors[i] = Color.decode("#" + hexValues[i]);
        }

        /*write pixels to image*/
        CryptIO.notifyProcess("Processing pixels...");
        int cCounter = 0;
        double psuedoSide = Math.sqrt(pixelColors.length);
        int side = (psuedoSide > Math.floor(psuedoSide) ? 1 + (int) psuedoSide : (int) psuedoSide);
        BufferedImage image = new BufferedImage(side, side, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (cCounter < pixelColors.length) {
                    image.setRGB(x, y, pixelColors[cCounter].getRGB());
                    cCounter++;
                } else {
                    image.setRGB(x, y, BLANK_SPACE_COLOR.getRGB());
                }
            }
        }

        /*write image*/
        CryptIO.notifyProcess("Writing image...");
        if (!CryptIO.write(image, "src/res/hexOutputImage.png")) {
            CryptIO.notifyErr("Could not write hex image.");
        }

        /*run image stats*/
//        VisualStats.run(CryptIO.readImage(), image);

        /*completed*/
        System.out.println("Hex encryption completed.");
    }

    @Override
    public String decrypt() throws CryptException {
        /*read pixels from image*/
        Color[][] pixelColors = CryptIO.readPixels("src/res/hexOutputImage.png");

        /*convert pixels to hex*/
        CryptIO.notifyProcess("Converting pixels...");
        ArrayList<String> hexValues = new ArrayList<>();
        for (Color[] pixelColor : pixelColors) {
            for (Color pixelColor_ : pixelColor) {
                if (pixelColor_.getRGB() != BLANK_SPACE_COLOR.getRGB()) {
                    hexValues.add(String.format("#%02x%02x%02x", pixelColor_.getRed(), pixelColor_.getGreen(), pixelColor_.getBlue()));
                }
            }
        }

        /*remove #*/
        ArrayList<String> newHexValues = new ArrayList<>();
        for (String hexValue : hexValues) {
            System.out.println(hexValue);
            String newHex = "";
            for (int o = 0; o < hexValue.length(); o++) {
                if (hexValue.charAt(o) != '#') {
                    newHex += hexValue.charAt(o);
                }
            }
            newHexValues.add(newHex);
        }

        /*convert hex to unicode value*/
        CryptIO.notifyProcess("Converting hex to unicode...");
        String decoded = "";
        for (int i = 0; i < newHexValues.size(); i++) {
            CryptIO.notifySubProcess("Iteration " + i + " of " + newHexValues.size());
            decoded += (char) Integer.parseInt(newHexValues.get(i), 16);
        }

        /*completed*/
        System.out.println("Hex decryption completed.");

        return decoded;
    }
}
