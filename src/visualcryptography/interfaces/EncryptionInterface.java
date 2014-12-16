package visualcryptography.interfaces;

import visualcryptography.exceptions.CryptException;

public interface EncryptionInterface {

    public void encrypt() throws CryptException;

    public String decrypt() throws CryptException;
}
