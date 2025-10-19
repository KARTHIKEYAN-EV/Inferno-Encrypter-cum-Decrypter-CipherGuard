package algorithms;

import exceptions.InvalidKeyException;

public class CaesarCipher extends Cipher {

    @Override
    public String encrypt(String text, int key) throws InvalidKeyException {
        if (key < 0) {
            throw new InvalidKeyException("Key must be positive for Caesar Cipher");
        }
        key = key % 26;
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch))
                result.append((char) (((ch - 'A' + key) % 26) + 'A'));
            else if (Character.isLowerCase(ch))
                result.append((char) (((ch - 'a' + key) % 26) + 'a'));
            else
                result.append(ch);
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, int key) throws InvalidKeyException {
        if (key < 0) {
            throw new InvalidKeyException("Key must be positive for Caesar Cipher");
        }
        key = key % 26;
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch))
                result.append((char) (((ch - 'A' - key + 26) % 26) + 'A'));
            else if (Character.isLowerCase(ch))
                result.append((char) (((ch - 'a' - key + 26) % 26) + 'a'));
            else
                result.append(ch);
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return "Caesar Cipher";
    }
}
