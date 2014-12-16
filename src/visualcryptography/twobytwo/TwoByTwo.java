package visualcryptography.twobytwo;

import java.awt.Color;
import java.io.IOException;
import lib.visualcryptiography.crypt.Share;
import lib.visualcryptiography.crypt.ShareUtil;
import lib.visualcryptiography.crypt.VisualScheme;
import lib.visualcryptiography.io.CryptIO;

public class TwoByTwo extends VisualScheme {

    public TwoByTwo() {
        super(2, 2);
    }

    @Override
    public void makeShares() {
        try {
            CryptIO.setup();
        } catch (IOException e) {
        }

        ReadState[][] flags = getPixelFlags();
        Share s1 = new Share(1, flags.length * 4, flags[0].length * 4);
        Share s2 = new Share(2, flags.length * 4, flags[0].length * 4);

        CryptIO.notifyProcess("Reading pixel flags...");
        for (int i = 0; i < flags.length; i++) {
            for (int o = 0; o < flags.length; o++) {
                if (flags[i][o] == TwoByTwo.ReadState.ERROR) {
                    CryptIO.notifyErr("Non-BW pixel state.");
                    return;
                }
                switch (getWriteState(flags[i][o])[0]) {
                    case BWWB:
                        s1.add(i, o, WriteState.BWWB.get());
                        break;
                    case WBBW:
                        s1.add(i, o, WriteState.WBBW.get());
                        break;
                    case WBWB:
                        s1.add(i, o, WriteState.WBWB.get());
                        break;
                    case BWBW:
                        s1.add(i, o, WriteState.BWBW.get());
                        break;
                    case BBBB:
                        s1.add(i, o, WriteState.BBBB.get());
                        break;
                }
                switch (getWriteState(flags[i][o])[1]) {
                    case BWWB:
                        s2.add(i, o, WriteState.BWWB.get());
                        break;
                    case WBBW:
                        s2.add(i, o, WriteState.WBBW.get());
                        break;
                    case WBWB:
                        s2.add(i, o, WriteState.WBWB.get());
                        break;
                    case BWBW:
                        s2.add(i, o, WriteState.BWBW.get());
                        break;
                    case BBBB:
                        s2.add(i, o, WriteState.BBBB.get());
                        break;
                }
            }
        }

        shares[0] = s1;
        shares[1] = s2;

        CryptIO.notifyProcess("Writing shares...");
        CryptIO.write(s1, "src/res/share" + s1.getShareNum() + ".png");
        CryptIO.write(s2, "src/res/share" + s2.getShareNum() + ".png");
    }

    private enum ReadState {

        B,
        W,
        ERROR;
    }

    private enum WriteState {

        BWWB,
        WBBW,
        BWBW,
        WBWB,
        BBBB;

        public Color[] get() {
            Color b = Color.BLACK;
            Color w = Color.WHITE;
            if (this == BWWB) {
                return new Color[]{b, w, w, b};
            } else if (this == WBBW) {
                return new Color[]{w, b, b, w};
            } else if (this == BWBW) {
                return new Color[]{b, w, b, w};
            } else if (this == WBWB) {
                return new Color[]{w, b, w, b};
            } else if (this == BBBB) {
                return new Color[]{b, b, b, b};
            }
            return null;
        }
    }

    private ReadState[][] getPixelFlags() {
        CryptIO.notifyProcess("Reading pixel flags...");
        Color[][] pixels = CryptIO.readPixels("src/res/tbtInputImage.png");
        ReadState[][] flags = new ReadState[pixels.length][];
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                flags[x] = new ReadState[pixels[x].length];
                if (pixels[x][y].equals(Color.WHITE)) {
                    flags[x][y] = ReadState.W;
                } else if (pixels[x][y].equals(Color.BLACK)) {
                    flags[x][y] = ReadState.B;
                } else {
                    flags[x][y] = ReadState.ERROR;
                }
                CryptIO.notifySubProcess("Pixel state at " + x + ", " + y + ": " + flags[x][y]);
            }
        }
        return flags;
    }

    private WriteState[] getWriteState(ReadState initialState) {
        if (initialState == ReadState.B) {
            if (Math.random() > .5) {
                return new WriteState[]{WriteState.BWWB, WriteState.WBBW};
            } else {
                return new WriteState[]{WriteState.WBBW, WriteState.BWWB};
            }
        } else {
            if (Math.random() > .5) {
                return new WriteState[]{WriteState.BWWB, WriteState.BWWB};
            } else {
                return new WriteState[]{WriteState.WBBW, WriteState.WBBW};
            }
        }
    }

    @Override
    public void decryptShares(Share... shares) {
        CryptIO.write(ShareUtil.overlayTwoShares(shares[0], shares[1]), "src/res/overlay.png");
    }
}
