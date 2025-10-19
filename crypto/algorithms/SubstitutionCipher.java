package algorithms;

import exceptions.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

public class SubstitutionCipher extends Cipher {

    private final Map<Character, Character> encryptMap;
    private final Map<Character, Character> decryptMap;

    public SubstitutionCipher(Map<Character, Character> mapping) throws InvalidKeyException {
        if (mapping == null || mapping.size() != 26) {
            throw new InvalidKeyException("Mapping must have 26 characters for A-Z");
        }

        encryptMap = new HashMap<>();
        decryptMap = new HashMap<>();

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            char mapped = Character.toUpperCase(mapping.get(ch));
            encryptMap.put(ch, mapped);
            decryptMap.put(mapped, ch);
        }
    }

    @Override
    public String encrypt(String text, int key) throws InvalidKeyException {
        final StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append(encryptMap.get(ch));
            } else if (Character.isLowerCase(ch)) {
                result.append(Character.toLowerCase(encryptMap.get(Character.toUpperCase(ch))));
            } else {
                result.append(ch); // leave non-letters unchanged
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, int key) throws InvalidKeyException {
        final StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append(decryptMap.get(ch));
            } else if (Character.isLowerCase(ch)) {
                result.append(Character.toLowerCase(decryptMap.get(Character.toUpperCase(ch))));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return "Substitution Cipher";
    }

    @Override
    public String toString() {
        return "Cipher: Substitution Cipher";
    }
}
