package algorithms;

import exceptions.InvalidKeyException;

public class XORCipher extends Cipher {

    @Override
    public String encrypt(String text, int key) throws InvalidKeyException {
        if (key < 0) {
            throw new InvalidKeyException("Key must be positive for XOR Cipher");
        }

        final StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            char c = (char) (ch ^ key); // XOR each character with the key
            result.append(c);
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, int key) throws InvalidKeyException {
        if (key < 0) {
            throw new InvalidKeyException("Key must be positive for XOR Cipher");
        }

        final StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            char c = (char) (ch ^ key); // XOR again restores original
            result.append(c);
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return "XOR Cipher";
    }

    @Override
    public String toString() {
        return "Cipher: XOR Cipher";
    }
}
