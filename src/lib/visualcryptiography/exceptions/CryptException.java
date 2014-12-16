package lib.visualcryptiography.exceptions;

public class CryptException extends Exception {

    protected CryptException(String msg) {
        super(msg);
    }

    protected CryptException() {
        super("Crypt exception.");
    }
}
