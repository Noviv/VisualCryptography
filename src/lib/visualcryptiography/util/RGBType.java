package lib.visualcryptiography.util;

public enum RGBType {

    A(true, "A"),
    R(false, "R"),
    G(false, "G"),
    B(false, "B");

    private final boolean argb;
    private final String type;

    RGBType(boolean argbNecessary, String type) {
        argb = argbNecessary;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean getARGB() {
        return argb;
    }
}
