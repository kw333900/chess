package dataaccess;

public class InvalidGameIDException extends RuntimeException {
    public InvalidGameIDException(String message) {
        super(message);
    }
}
