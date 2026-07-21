package dataaccess.exceptions;

public class InvalidGameIDException extends RuntimeException {
    public InvalidGameIDException(String message) {
        super(message);
    }
}
