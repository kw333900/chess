package dataaccess.exceptions;

public class GameAlreadyTakenException extends RuntimeException {
    public GameAlreadyTakenException(String message) {
        super(message);
    }
}
