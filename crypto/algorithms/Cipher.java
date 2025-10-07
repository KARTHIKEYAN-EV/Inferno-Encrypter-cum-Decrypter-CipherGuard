package algorithms;
public abstract class Cipher{
	protected String algorithmName; //to store the name of each algorithm
	protected static int encryptionCount=0;  //to count the encryption count
	Cipher(String name){
		this.algorithmName=name;
	}
	public String encrypt(String text,String key);
	public String decrypt(String text,String key);
	public String getAlgorithmName(){
		return algorithmName;
	}
	protected void incrementEncryptionCount(){
		encryptionCount++;
	}
	public static int getEncryptionCount(){
		return encryptionCount;
	}
}