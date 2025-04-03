package bot;

import lombok.Getter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import service.*;

@Getter
public abstract class JBotLib extends TelegramLongPollingBot {
    private final MessageSender messageSender;
    private final ButtonBuilder buttonBuilder;
    private final ChatContentService chatContentService;
    private final MessageUpdater messageUpdater;
    private final PermissionManager permissionManager;

    private JBotLib() {
        this.buttonBuilder = new ButtonBuilder(this);
        this.chatContentService = new ChatContentService(this);
        this.messageUpdater = new MessageUpdater(this);
        this.permissionManager = new PermissionManager(this);
        this.messageSender = new MessageSender(this);
    }
}
