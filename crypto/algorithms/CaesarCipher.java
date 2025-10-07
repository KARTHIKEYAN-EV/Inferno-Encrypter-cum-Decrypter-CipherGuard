package algorithms;
public class CaesarCipher extends Cipher{
	public String encrypt(String text,String key){
		StringBuilder result=new StringBuilder();
		int shift=Integer.parseInt(key);     //convert key to int
		
		return result.toString();
	}
	public String decrypt(String text,String key){
		return encrypt(text,26-key);
	}
}