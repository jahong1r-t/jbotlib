package exceptions;

/**
 * Exception thrown when a provided channel link is invalid.
 */
public class InvalidChannelLinkException extends RuntimeException {
    /**
     * Constructs a new InvalidChannelLinkException with the specified message.
     *
     * @param message the detail message
     */
    public InvalidChannelLinkException(String message) {
        super(message);
    }
}
