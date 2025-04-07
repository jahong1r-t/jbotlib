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

public class EventLogger {
    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);
    private final boolean enableFileLogging;
    private final String logFilePath;
    private final Map<String, String> contextData;

    public EventLogger() {
        this(false, null);
    }

    public EventLogger(boolean enableFileLogging, String logFilePath) {
        this.enableFileLogging = enableFileLogging;
        this.logFilePath = logFilePath;
        this.contextData = new HashMap<>();
    }

    @SneakyThrows
    public void logUserAction(Long userId, String action) {
        String message = String.format("User %d performed action: %s", userId, action);
        logger.info(message);
        writeToFileIfEnabled("INFO", message);
    }

    @SneakyThrows
    public void logBotAction(String action, String details) {
        String message = String.format("Bot action: %s | Details: %s", action, details);
        logger.info(message);
        writeToFileIfEnabled("INFO", message);
    }

    @SneakyThrows
    public void logError(Exception e, String context) {
        String message = String.format("Error in context '%s': %s", context, e.getMessage());
        logger.error(message, e);
        writeToFileIfEnabled("ERROR", message + " | Stacktrace: " + getStackTrace(e));
    }

    @SneakyThrows
    public void logWarning(String message, String context) {
        String fullMessage = String.format("Warning in context '%s': %s", context, message);
        logger.warn(fullMessage);
        writeToFileIfEnabled("WARN", fullMessage);
    }

    @SneakyThrows
    public void addContext(String key, String value) {
        contextData.put(key, value);
    }

    @SneakyThrows
    public void clearContext() {
        contextData.clear();
    }

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

    @SneakyThrows
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
