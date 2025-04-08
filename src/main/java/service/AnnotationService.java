package service;

import bot.JBotLib;
import annotations.AdminOnly;
import annotations.AutoReply;
import annotations.BotCommand;
import annotations.ScheduledTask;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A service class for managing and processing bot annotations in a Telegram bot.
 * Handles annotations such as {@link BotCommand}, {@link AutoReply}, {@link AdminOnly}, and
 * {@link ScheduledTask}, providing a clean separation of annotation logic from the core bot functionality.
 *
 * <p>This class scans for annotated methods in the bot implementation, registers them, and
 * invokes them based on incoming updates or scheduled tasks. It supports command handling,
 * automatic replies, admin-only commands, and scheduled tasks.</p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class AnnotationService {
    private final JBotLib bot;
    private final MessageService messageService;
    private final KeyboardBuilder keyboardBuilder;
    private final ChatService chatService;
    private final EventLogger eventLogger;

    private final Map<String, Method> commandHandlers = new HashMap<>();
    private final Map<String, Method> autoReplyHandlers = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Set<Long> activeChats = new HashSet<>();

    /**
     * Constructs a new AnnotationService instance.
     *
     * @param bot The JBotLib instance containing the annotated methods.
     * @param messageService The MessageService instance for sending messages.
     * @param keyboardBuilder The KeyboardBuilder instance for creating keyboards.
     * @param chatService The ChatService instance for managing chat operations.
     * @param eventLogger The EventLogger instance for logging events.
     */
    public AnnotationService(JBotLib bot, MessageService messageService, KeyboardBuilder keyboardBuilder,
                             ChatService chatService, EventLogger eventLogger) {
        this.bot = bot;
        this.messageService = messageService;
        this.keyboardBuilder = keyboardBuilder;
        this.chatService = chatService;
        this.eventLogger = eventLogger;

        registerHandlers();
        scheduleTasks();
    }

    /**
     * Processes an incoming Telegram update and triggers appropriate annotated methods.
     * Handles {@link BotCommand} and {@link AutoReply} annotations based on the message text.
     *
     * @param update The Telegram update to process.
     *
     * @example
     * <pre>
     * AnnotationService annotationService = new AnnotationService(bot, messageService, keyboardBuilder, chatService, eventLogger);
     * Update update = // Received Telegram update
     * annotationService.processUpdate(update);
     * </pre>
     */
    public void processUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();
            String text = update.getMessage().getText();

            activeChats.add(chatId);

            if (commandHandlers.containsKey(text)) {
                invokeMethod(commandHandlers.get(text), chatId, userId);
            }

            for (Map.Entry<String, Method> entry : autoReplyHandlers.entrySet()) {
                if (text.toLowerCase().contains(entry.getKey().toLowerCase())) {
                    invokeMethod(entry.getValue(), chatId, userId);
                    break;
                }
            }
        }
    }

    /**
     * Registers methods annotated with {@link BotCommand} and {@link AutoReply}.
     * Scans the bot's declared methods and maps them to their respective triggers.
     *
     * @example
     * <pre>
     * // This method is called automatically during construction.
     * // See the constructor for usage.
     * </pre>
     */
    public void registerHandlers() {
        for (Method method : bot.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotCommand.class)) {
                String command = method.getAnnotation(BotCommand.class).value();
                commandHandlers.put(command, method);
            }
            if (method.isAnnotationPresent(AutoReply.class)) {
                String trigger = method.getAnnotation(AutoReply.class).value();
                autoReplyHandlers.put(trigger, method);
            }
        }
    }

    /**
     * Schedules tasks annotated with {@link ScheduledTask}.
     * Executes the annotated methods at fixed intervals in all active chats.
     *
     * @example
     * <pre>
     * // This method is called automatically during construction.
     * // See the constructor for usage.
     * </pre>
     */
    public void scheduleTasks() {
        for (Method method : bot.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ScheduledTask.class)) {
                int interval = method.getAnnotation(ScheduledTask.class).intervalSeconds();
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        for (Long chatId : activeChats) {
                            invokeMethod(method, chatId, null);
                        }
                    } catch (Exception e) {
                        eventLogger.logError(e, "scheduled_task_execution");
                    }
                }, 0, interval, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Invokes a method with the specified parameters, handling {@link AdminOnly} restrictions.
     * Dynamically prepares method arguments based on the method's parameter types.
     *
     * @param method The method to invoke.
     * @param chatId The ID of the chat where the method is invoked.
     * @param userId The ID of the user who triggered the method (can be null for scheduled tasks).
     * @throws Exception If the method invocation fails or the user lacks admin privileges for an {@link AdminOnly} method.
     *
     * @example
     * <pre>
     * // This method is private and called internally by processUpdate and scheduleTasks.
     * // See processUpdate for usage.
     * </pre>
     */
    @SneakyThrows
    private void invokeMethod(Method method, Long chatId, Long userId) {
        if (method.isAnnotationPresent(AdminOnly.class)) {
            if (!chatService.isUserAdmin(userId, String.valueOf(chatId))) {
                messageService.sendMessage(chatId, "This command is for admins only!");
                return;
            }
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i].equals(Long.class) && i == 0) {
                args[i] = chatId;
            } else if (paramTypes[i].equals(Long.class) && i == 1) {
                args[i] = userId;
            } else if (paramTypes[i].equals(MessageService.class)) {
                args[i] = messageService;
            } else if (paramTypes[i].equals(KeyboardBuilder.class)) {
                args[i] = keyboardBuilder;
            } else if (paramTypes[i].equals(ChatService.class)) {
                args[i] = chatService;
            } else if (paramTypes[i].equals(EventLogger.class)) {
                args[i] = eventLogger;
            } else {
                args[i] = null;
            }
        }

        method.invoke(bot, args);
    }
}
