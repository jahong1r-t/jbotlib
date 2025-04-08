package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a scheduled task that runs at specified intervals.
 * This annotation can be used to schedule periodic tasks in the bot.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScheduledTask {
    /**
     * The interval in seconds between task executions.
     *
     * @return the interval in seconds
     */
    int intervalSeconds();
}
