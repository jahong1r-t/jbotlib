package service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for logging events, errors, and warnings in a Telegram bot.
 * Supports logging to both the console (via SLF4J) and a file (if enabled).
 * Provides methods to log user actions, bot actions, errors, and warnings, with
 * support for adding contextual data to logs.
 *
 * <p>This class is designed to provide a centralized logging mechanism for a Telegram bot,
 * allowing developers to track events, debug issues, and monitor bot activity. It supports
 * both in-memory context data and persistent file logging.</p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class EventLogger {
    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);
    private final boolean enableFileLogging;
    private final String logFilePath;
    private final Map<String, String> contextData;

    /**
     * Constructs a new EventLogger instance with file logging disabled.
     */
    public EventLogger() {
        this(false, null);
    }

    /**
     * Constructs a new EventLogger instance with optional file logging.
     *
     * @param enableFileLogging Whether to enable logging to a file.
     * @param logFilePath       The path to the log file (required if file logging is enabled).
     */
    public EventLogger(boolean enableFileLogging, String logFilePath) {
        this.enableFileLogging = enableFileLogging;
        this.logFilePath = logFilePath;
        this.contextData = new HashMap<>();
    }

    /**
     * Logs a user action with the specified user ID and action description.
     *
     * @param userId The ID of the user who performed the action.
     * @param action The description of the action.
     */
    @SneakyThrows
    public void logUserAction(Long userId, String action) {
        String message = String.format("User %d performed action: %s", userId, action);
        logger.info(message);
        writeToFileIfEnabled("INFO", message);
    }

    /**
     * Logs a bot action with the specified action and details.
     *
     * @param action  The description of the bot action.
     * @param details Additional details about the action.
     */
    @SneakyThrows
    public void logBotAction(String action, String details) {
        String message = String.format("Bot action: %s | Details: %s", action, details);
        logger.info(message);
        writeToFileIfEnabled("INFO", message);
    }

    /**
     * Logs an error with the specified exception and context.
     *
     * @param e       The exception to log.
     * @param context The context in which the error occurred.
     */
    @SneakyThrows
    public void logError(Exception e, String context) {
        String message = String.format("Error in context '%s': %s", context, e.getMessage());
        logger.error(message, e);
        writeToFileIfEnabled("ERROR", message + " | Stacktrace: " + getStackTrace(e));
    }

    /**
     * Logs a warning with the specified message and context.
     *
     * @param message The warning message.
     * @param context The context in which the warning occurred.
     */
    @SneakyThrows
    public void logWarning(String message, String context) {
        String fullMessage = String.format("Warning in context '%s': %s", context, message);
        logger.warn(fullMessage);
        writeToFileIfEnabled("WARN", fullMessage);
    }

    /**
     * Adds contextual data to the logger, which will be included in subsequent log messages.
     *
     * @param key   The key for the context data.
     * @param value The value for the context data.
     */
    @SneakyThrows
    public void addContext(String key, String value) {
        contextData.put(key, value);
    }

    /**
     * Clears all contextual data from the logger.
     */
    @SneakyThrows
    public void clearContext() {
        contextData.clear();
    }

    /**
     * Writes a log message to the file if file logging is enabled.
     *
     * @param level   The log level (e.g., INFO, ERROR, WARN).
     * @param message The message to log.
     */
    @SneakyThrows
    private void writeToFileIfEnabled(String level, String message) {
        if (enableFileLogging && logFilePath != null) {
            try (FileWriter writer = new FileWriter(logFilePath, true)) {
                String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
                String contextString = contextData.isEmpty() ? "" : " | Context: " + contextData;
                writer.write(String.format("[%s] %s: %s%s%n", timestamp, level, message, contextString));
            } catch (IOException e) {
                logger.error("Failed to write to log file: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Converts an exception's stack trace to a string.
     *
     * @param e The exception whose stack trace to convert.
     * @return A string representation of the stack trace.
     */
    @SneakyThrows
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
