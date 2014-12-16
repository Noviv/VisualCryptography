package visualcryptography.exceptions;

public class CryptException extends Exception {

    public CryptException(CryptType type, String msg) {
        super(type + msg);
    }

    public enum CryptType {

        HEX("");

        CryptType(String msg) {

        }
    }
}
