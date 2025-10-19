package algorithms;

import exceptions.InvalidKeyException;

public abstract class Cipher {
    public abstract String encrypt(String text, int key) throws InvalidKeyException;

    public abstract String decrypt(String text, int key) throws InvalidKeyException;

    public abstract String getName();
}
