package algorithms;

import exceptions.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;

public class SubstitutionCipher extends Cipher {

    private final Map<Character, Character> encryptMap;
    private final Map<Character, Character> decryptMap;
    private boolean isValid = true;
    private String errorMessage = "";

    public SubstitutionCipher(String mapping) {
        encryptMap = new HashMap<>();
        decryptMap = new HashMap<>();
        
        // Simple validation
        if (mapping == null || mapping.length() != 26) {
            isValid = false;
            errorMessage = "Mapping must have exactly 26 characters for A-Z";
            return;
        }
        
        mapping = mapping.toUpperCase();
        boolean[] used = new boolean[26];
        
        for (int i = 0; i < 26; i++) {
            char c = mapping.charAt(i);
            if (!Character.isLetter(c)) {
                isValid = false;
                errorMessage = "Mapping must contain only letters A-Z";
                return;
            }
            int index = c - 'A';
            if (used[index]) {
                isValid = false;
                errorMessage = "Duplicate character in mapping: '" + c + "'";
                return;
            }
            used[index] = true;
        }
        
        // If valid, create maps
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            char mapped = mapping.charAt(ch - 'A');
            encryptMap.put(ch, mapped);
            decryptMap.put(mapped, ch);
        }
    }

    public boolean isValid() {
        return isValid;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    @Override
    public String encrypt(String text, int key) throws InvalidKeyException {
        if (!isValid) {
            throw new InvalidKeyException(errorMessage);
        }
        final StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                result.append(encryptMap.get(ch));
            } else if (Character.isLowerCase(ch)) {
                result.append(Character.toLowerCase(encryptMap.get(Character.toUpperCase(ch))));
            } else {
                result.append(ch); // leave non-alphabets unchanged
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String text, int key) throws InvalidKeyException {
        if (!isValid) {
            throw new InvalidKeyException(errorMessage);
        }
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
