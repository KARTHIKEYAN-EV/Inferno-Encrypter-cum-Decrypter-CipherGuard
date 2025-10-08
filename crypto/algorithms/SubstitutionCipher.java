package algorithms;
public class SubstitutionCipher extends Cipher {
	SubstitutionCipher(){
		super("Substitution Cipher");
	}
    private Map<Character, Character> encryptionMap;
    private Map<Character, Character> decryptionMap;
    public String encrypt(String text, String key) {
        generateMaps(key);  // Use key to create mapping
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(encryptionMap.getOrDefault(c, c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    public String decrypt(String text, String key) {
        generateMaps(key);
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(decryptionMap.getOrDefault(c, c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    private void generateMaps(String key) {
        encryptionMap = new HashMap<>();
        decryptionMap = new HashMap<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String shuffled = shuffleAlphabet(key);  // Use key to shuffle alphabet
        for (int i = 0; i < alphabet.length(); i++) {
            char normal = alphabet.charAt(i);
            char sub = shuffled.charAt(i);
            encryptionMap.put(normal, sub);
            decryptionMap.put(sub, normal);
            // Add lowercase versions too
            encryptionMap.put(Character.toLowerCase(normal), 
                            Character.toLowerCase(sub));
            decryptionMap.put(Character.toLowerCase(sub), 
                            Character.toLowerCase(normal));
        }
    }
}