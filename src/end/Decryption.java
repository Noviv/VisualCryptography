package end;

import java.io.File;
import javax.imageio.ImageIO;
import visualcryptography.onebyone.OneByOne;

public class Decryption {

    public static void main(String[] args) throws Exception {
        OneByOne main = new OneByOne(true);
        main.decryptImage(ImageIO.read(new File("src/res/share1.png")));
    }
}
