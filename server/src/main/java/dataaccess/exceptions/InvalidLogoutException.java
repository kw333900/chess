package dataaccess.exceptions;

public class InvalidLogoutException extends RuntimeException {
    public InvalidLogoutException(String message) {
        super(message);
    }
}
