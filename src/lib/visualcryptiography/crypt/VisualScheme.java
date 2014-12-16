package lib.visualcryptiography.crypt;

import lib.visualcryptiography.io.CryptIO;

public abstract class VisualScheme {

    protected final int k;
    protected final int n;
    protected Share[] shares;

    public VisualScheme(int k, int n) {
        CryptIO.notifyProcess("Initialized (" + k + ", " + n + ") VS.");
        if (k > n) {
            System.err.println("Cannot make (k, n) where k > n.");
        }
        this.k = k;
        this.n = n;
        shares = new Share[n];
    }

    public Share[] getShares() {
        return shares;
    }

    public abstract void makeShares();

    public abstract void decryptShares(Share... shares);
}
