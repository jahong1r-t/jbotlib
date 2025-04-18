package bot;

import lombok.Getter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import service.*;

/**
 * Abstract base class for creating Telegram bots using the JBotLib library.
 * Extends TelegramLongPollingBot to provide additional functionality for bot development.
 */
@Getter
public abstract class JBotLib extends TelegramLongPollingBot {
    private final AnnotationService annotationService;
    private final ChatService chatService;
    private final EventLogger eventLogger;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageService messageService;


    private JBotLib(AnnotationService annotationService, EventLogger eventLogger) {
        this.annotationService = annotationService;
        this.eventLogger = eventLogger;
        this.keyboardBuilder = new KeyboardBuilder(this);
        this.chatService = new ChatService(this);
        this.messageService = new MessageService(this);
    }
}
