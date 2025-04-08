package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a bot command that can be triggered by a user.
 * This annotation is used to map a command to a specific method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BotCommand {
    /**
     * The command string (e.g., "/start").
     *
     * @return the command string
     */
    String value();
}
