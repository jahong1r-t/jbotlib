package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an automatic reply for a specific message or command.
 * This annotation allows the bot to respond with a predefined message.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoReply {
    /**
     * The reply message to be sent when the condition is met.
     *
     * @return the reply message
     */
    String value();
}
