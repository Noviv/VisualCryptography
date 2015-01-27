package lib.util.datastructures;

import java.awt.Color;

public class Pixel extends Color {

    private final int x, y;

    public Pixel(int x, int y, Color c) {
        super(c.getRGB(), true);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVMulti() {
        return getRed() * getGreen() * getBlue();
    }

    public enum PixelDirection {

        NORTH,
        SOUTH,
        EAST,
        WEST;
    }
}
