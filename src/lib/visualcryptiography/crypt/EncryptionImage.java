package lib.visualcryptiography.crypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class EncryptionImage extends BufferedImage {

    private final HashMap<Integer, Integer> used;

    public EncryptionImage(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        used = new HashMap<>();
    }

    public void set(int origX, int origY, Color subpixel) {
        setRGB(origX, origY, subpixel.getRGB());
    }
}
