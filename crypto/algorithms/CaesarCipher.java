package algorithms;
public class CaesarCipher extends Cipher{
	CaesarCipher(){
		super("Caesar Cipher");
	}
	public String encrypt(String text,String key){
		StringBuilder result=new StringBuilder();
		int shift=Integer.parseInt(key);     //convert key to int
		for (char character : text.toCharArray()) {
            		if (Character.isLetter(character)) {
                		char base = Character.isLowerCase(character) ? 'a' : 'A';
                		char encrypted = (char) ((character - base + shift) % 26 + base);
               			result.append(encrypted);
            		} 
			else {
                		result.append(character);  // Keep spaces,special characters and punctuation
            		}
        	}
		return result.toString();
	}
	public String decrypt(String text,String key){
		int shift=Integer.parseInt(key);     //convert key to int
		return encrypt(text,""+(26-shift));       // decryption=shift backward //""+int->String due to concatenation
	}
}