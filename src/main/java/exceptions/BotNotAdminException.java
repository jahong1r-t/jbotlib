package exceptions;

/**
 * Exception thrown when a bot command is executed by a non-admin user.
 */
public class BotNotAdminException extends RuntimeException {
    /**
     * Constructs a new BotNotAdminException with the specified message.
     *
     * @param message the detail message
     */
    public BotNotAdminException(String message) {
        super(message);
    }
}
