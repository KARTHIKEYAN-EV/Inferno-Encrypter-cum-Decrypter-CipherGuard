package algorithms;
public class CaesarCipher extends Cipher{
	CaesarCipher(){
		super("Caesar Cipher");
	}
	public String encrypt(String text,String key){
		StringBuilder result=new StringBuilder();
		int shift=Integer.parseInt(key);     //convert key to int
		
		return result.toString();
	}
	public String decrypt(String text,String key){
		int shift=Integer.parseInt(key);     //convert key to int
		return encrypt(text,26-shift);       // decryption=shift backward
	}
}