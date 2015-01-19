package lib.util.datastructures;

public class PixelDistribution {

    private final int width;
    private final int height;
    private final Pixel[][] pixels;
    private final Signal[][] signals;
    private final double[][] r2Vals;
    private Signal pS;
    private double sigTotal;

    public PixelDistribution(int w, int h) {
        width = w;
        height = h;
        pixels = new Pixel[w][h];
        signals = new Signal[w][h];
        r2Vals = new double[w][h];
    }

    public PixelDistribution(Pixel[][] array) {
        pixels = array;
        width = array.length;
        height = array[0].length;
        signals = new Signal[width][height];
        r2Vals = new double[width][height];
    }

    public void setPositionFeeds(int x, int y, double nSignal, double eSignal, double wSignal, double sSignal) {
        signals[x][y] = new Signal(nSignal, eSignal, wSignal, sSignal);
        pS = signals[x][y];
        sigTotal += nSignal + eSignal + wSignal + sSignal;
    }

    public Pixel[][] get() {
        return pixels;
    }

    public int getNumPixels() {
        return width * height;
    }

    public double getPrevSignal2() {
        return Math.pow(pS.getSum(), 2);
    }

    public void printSignals() {
        for (Signal[] sA : signals) {
            for (Signal s : sA) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public Pixel getPixel(int x, int y) {
        return pixels[x][y];
    }

    public Signal getSignal(int x, int y) {
        return signals[x][y];
    }

    public double getR2(int x, int y) {
        return r2Vals[x][y];
    }

    public int getWidth() {
        return pixels.length;
    }

    public int getHeight() {
        return pixels[0].length;
    }

    public boolean verify() {
        for (Signal[] sA : signals) {
            for (Signal s : sA) {
//                if (s.anyGreater(Signal.MAX_PIXEL_DIFF)) {
//                    return false;
//                }
            }
        }
        return true;
    }

    public class Signal {

        private final double north, east, west, south;

        public Signal(double n, double e, double w, double s) {
            north = n;
            east = e;
            west = w;
            south = s;
        }

        public double getSouth() {
            return south;
        }

        public double getNorth() {
            return north;
        }

        public double getWest() {
            return west;
        }

        public double getEast() {
            return east;
        }

        public double getSum() {
            return north + east + west + south;
        }

        public double getMean() {
            return getSum() / 4.0;
        }

        public boolean anyGreater(double bound) {
            return north > bound || south > bound || east > bound || west > bound;
        }

        public boolean anyLess(double bound) {
            return north < bound || south < bound || east < bound || west < bound;
        }

        @Override
        public String toString() {
            return "N:" + north + ",E:" + east + ",S:" + south + ",W:" + west;
        }
    }
}
