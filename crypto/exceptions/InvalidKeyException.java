package exceptions;

public class InvalidKeyException extends Exception {
    public InvalidKeyException() {
        super("Invalid key provided for cipher");
    }

    public InvalidKeyException(String message) {
        super(message);
    }
}
