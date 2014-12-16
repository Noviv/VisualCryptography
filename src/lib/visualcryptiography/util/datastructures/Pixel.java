package lib.visualcryptiography.util.datastructures;

import java.awt.Color;

public class Pixel extends Color {

    private final int x, y;

    public Pixel(int x, int y, Color c) {
        super(c.getRGB());
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public enum PixelDirection {

        NORTH,
        SOUTH,
        EAST,
        WEST;
    }
}
