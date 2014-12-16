package lib.visualcryptiography.crypt;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import lib.visualcryptiography.io.CryptIO;

public class Share extends BufferedImage {

    private final int shareNum;
    private final HashMap<Integer, Integer> used;

    public Share(int shareNum, int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        used = new HashMap<>();
        this.shareNum = shareNum;

        CryptIO.notifyProcess("Created share " + shareNum);
    }

    public void add(int origX, int origY, Color[] subpixelBlocks) {
        int x = origX * 4;
        int y = origY * 4;
        setRGB(x, y, subpixelBlocks[0].getRGB());
        setRGB(x + 1, y, subpixelBlocks[1].getRGB());
        setRGB(x, y + 1, subpixelBlocks[2].getRGB());
        setRGB(x + 1, y + 1, subpixelBlocks[3].getRGB());
    }

    public void add(int origX, int origY, Color subpixel) {
        setRGB(origX, origY, subpixel.getRGB());
    }

    public int getShareNum() {
        return shareNum;
    }

    @Override
    public void setRGB(int x, int y, int rgb) {
//        if (used(x, y)) {
        used.put(x, y);
        super.setRGB(x, y, rgb);
//        } else {
//            CryptIO.notifyErr("Attempted to add pixel at used space (" + x + ", " + y + ").");
//        }
    }

    public boolean used(int x, int y) {
        return used.get(x) != null;
    }

    public void printUsedCoordinates() {
        for (Integer x : used.keySet()) {
            System.out.println(x + ", " + used.get(x));
        }
    }
}
