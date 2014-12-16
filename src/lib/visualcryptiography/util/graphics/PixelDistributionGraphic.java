package lib.visualcryptiography.util.graphics;

import java.awt.image.BufferedImage;
import lib.visualcryptiography.util.datastructures.PixelDistribution;

public class PixelDistributionGraphic {

    private final BufferedImage image;
    private final PixelDistribution pd;

    public PixelDistributionGraphic(PixelDistribution pd) {
        this.pd = pd;
        image = new BufferedImage(pd.getWidth(), pd.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage get() {
        return image;
    }
}
