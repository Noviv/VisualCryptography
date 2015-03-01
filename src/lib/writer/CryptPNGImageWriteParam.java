package lib.writer;

import java.util.Locale;
import javax.imageio.ImageWriteParam;

public class CryptPNGImageWriteParam extends ImageWriteParam {

    public CryptPNGImageWriteParam() {
        super(Locale.getDefault());
    }

    public void setCompressionQuality(float quality) {
        if (quality < 0.0F || quality > 1.0F) {
            throw new IllegalArgumentException("Quality out-of-bounds!");
        }
        this.compressionQuality = 256 - (quality * 256);
    }
}
