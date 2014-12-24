package lib.visualcryptiography.util.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lib.visualcryptiography.io.CryptIO;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class PixelDistributionAlphaLayerPlot {

    private Color[][] r;
    private Color[][] g;
    private Color[][] b;
    private BufferedImage rI;
    private BufferedImage gI;
    private BufferedImage bI;

    public PixelDistributionAlphaLayerPlot(PixelDistribution pd) {
        r = new Color[pd.getWidth()][pd.getHeight()];
        g = new Color[pd.getWidth()][pd.getHeight()];
        b = new Color[pd.getWidth()][pd.getHeight()];
        rI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        gI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        bI = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

        for (int x = 0; x < pd.getWidth(); x++) {
            for (int y = 0; y < pd.getHeight(); y++) {
                r[x][y] = genColor(Color.RED, pd.getPixel(x, y).getRed());
                g[x][y] = genColor(Color.GREEN, pd.getPixel(x, y).getGreen());
                b[x][y] = genColor(Color.BLUE, pd.getPixel(x, y).getBlue());
                rI.setRGB(x, y, r[x][y].getRGB());
                gI.setRGB(x, y, g[x][y].getRGB());
                bI.setRGB(x, y, b[x][y].getRGB());
            }
        }

        CryptIO.write(rI, "src/res/stats/rI.png");
        CryptIO.write(gI, "src/res/stats/gI.png");
        CryptIO.write(bI, "src/res/stats/bI.png");
        CryptIO.notifyProcess("Finished writing stat plot images.");
    }

    private Color genColor(Color base, int alpha) {
        return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
    }
}
