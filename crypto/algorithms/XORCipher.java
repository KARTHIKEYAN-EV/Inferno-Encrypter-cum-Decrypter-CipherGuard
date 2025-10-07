package algorithms;
public class XORCipher extends Cipher{
	XORCipher(){
		super("XOR Cipher");
	}
	public String encrypt(String text,String key){
		StringBuilder result=new StringBuilder();
		for (int i=0;i<text.length();i++){
			char character=text.charAt(i);
			char keyChar=key.charAt(i % key.length());
			char encrypted=(char) (character ^ keyChar);  //XOR
			result.append(encrypted);
		}
		return result.toString();
	}
	public String decrypt(String text,String key){
		return encrypt(text,key);   //XOR can be reversed by again encrypting it
	}
}