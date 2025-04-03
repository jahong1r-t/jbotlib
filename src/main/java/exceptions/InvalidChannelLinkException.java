package exceptions;

public class InvalidChannelLinkException extends RuntimeException {
    public InvalidChannelLinkException(String message) {
        super(message);
    }
}
