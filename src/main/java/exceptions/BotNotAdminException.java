package exceptions;

public class BotNotAdminException extends RuntimeException {
    public BotNotAdminException(String message) {
        super(message);
    }
}
