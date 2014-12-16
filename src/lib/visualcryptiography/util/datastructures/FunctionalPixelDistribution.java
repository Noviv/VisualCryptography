package lib.visualcryptiography.util.datastructures;

import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.VisualStats;

public class FunctionalPixelDistribution {

    private final PixelDistribution pd_;
    private boolean normal;//means it is a digital nonR picture
    private PixelDistribution[] pdLayers;

    public FunctionalPixelDistribution(PixelDistribution pd) {
        pd_ = pd;
        //check normality
        checkNormality();
        //split image
        split();
        //put message to layers
        //encode layers
        //return
    }

    private void checkNormality() {
        double norm = 0.5;
        double normTemp = 0.5;
        final double dx = Double.MIN_VALUE;//TODO calculate new dX based on size of picture so that x-min is minimized

        boolean topBound = false;
        boolean bottomBound = false;
        boolean rightBound = false;
        boolean leftBound = false;
        int[] xyCheck = new int[8];
        for (int x = 0; x < pd_.getWidth(); x++) {
            for (int y = 0; y < pd_.getHeight(); y++) {
                if (x == 0) {
                    leftBound = true;
                }
                if (x == pd_.getWidth() - 1) {
                    rightBound = true;
                }
                if (y == 0) {
                    topBound = true;
                }
                if (y == pd_.getHeight() - 1) {
                    bottomBound = true;
                }
                //set N, S, E, W bound checks
                /*topX, topY
                 botX, botY
                 lX, lY,
                 rX, rY*/
                xyCheck[0] = (topBound ? -1 : x);
                xyCheck[1] = (topBound ? -1 : y - 1);
                xyCheck[2] = (bottomBound ? -1 : x);
                xyCheck[3] = (bottomBound ? -1 : y + 1);
                xyCheck[4] = (leftBound ? -1 : x - 1);
                xyCheck[5] = (leftBound ? -1 : y);
                xyCheck[6] = (rightBound ? -1 : x + 1);
                xyCheck[7] = (rightBound ? -1 : y);

                //check
                Pixel tempPixel = null;
                if (xyCheck[0] != -1) {//top
                    tempPixel = pd_.getPixel(xyCheck[0], xyCheck[1]);
                    if (tempPixel == null) {
                        CryptIO.notifyErr("tempPixel null");
                        System.exit(1);
                    }
                    if (VisualStats.innerComparePixel(pd_.getPixel(x, y), tempPixel, pd_)) {
                        norm += dx;
                    } else {
                        norm -= dx;
                    }
                }
                if (xyCheck[2] != -1) {//bottom
                    tempPixel = pd_.getPixel(xyCheck[2], xyCheck[3]);
                    if (tempPixel == null) {
                        CryptIO.notifyErr("tempPixel null");
                        System.exit(1);
                    }
                    if (VisualStats.innerComparePixel(pd_.getPixel(x, y), tempPixel, pd_)) {
                        norm += dx;
                    } else {
                        norm -= dx;
                    }
                }
                if (xyCheck[4] != -1) {//left
                    tempPixel = pd_.getPixel(xyCheck[4], xyCheck[5]);
                    if (tempPixel == null) {
                        CryptIO.notifyErr("tempPixel null");
                        System.exit(1);
                    }
                    if (VisualStats.innerComparePixel(pd_.getPixel(x, y), tempPixel, pd_)) {
                        norm += dx;
                    } else {
                        norm -= dx;
                    }
                }
                if (xyCheck[6] != -1) {//right
                    tempPixel = pd_.getPixel(xyCheck[6], xyCheck[7]);
                    if (tempPixel == null) {
                        CryptIO.notifyErr("tempPixel null");
                        System.exit(1);
                    }
                    if (VisualStats.innerComparePixel(pd_.getPixel(x, y), tempPixel, pd_)) {
                        norm += dx;
                    } else {
                        norm -= dx;
                    }
                }

                //process norm
                //finalize
                topBound = bottomBound = rightBound = leftBound = false;
                normTemp = 0.5;
            }
        }

        //finalize method
        normal = norm <= 2 * dx;
    }

    private void split() {
        //init layers
        pdLayers = new PixelDistribution[14];
        //do
        boolean onBounce = false;
        int currentLayer = 0;
        for (int x = 0; x < pd_.getWidth(); x++) {
            for (int y = 0; y < pd_.getHeight(); y++) {
                if (pdLayers[currentLayer] != null) {
                    currentLayer++;
                }
                onBounce = !onBounce;//ensure inversion so that no two coincidential pixels are modified
            }
        }
    }
}
