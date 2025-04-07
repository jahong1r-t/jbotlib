package service;

import exceptions.BotNotAdminException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.DeleteChatPhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import utils.Resolvers;

/**
 * A service class for managing chat content in Telegram, such as chat photos.
 * Provides methods to change or delete the photo of a chat, requiring admin privileges.
 */
@Slf4j
public class ChatContentService {
    private final TelegramLongPollingBot bot;
    private final PermissionManager permissionManager;

    /**
     * Constructor for ChatContentService.
     *
     * @param bot The TelegramLongPollingBot instance used to interact with the Telegram API.
     */
    public ChatContentService(TelegramLongPollingBot bot) {
        this.bot = bot;
        this.permissionManager = new PermissionManager(bot);
    }

    /**
     * Changes the photo of a specified chat.
     * Requires the bot to have admin privileges in the chat.
     *
     * @param chatUsername The username or identifier of the chat (e.g., @channelname).
     * @param photo The new photo file to set for the chat.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     * @throws Exception If an error occurs while updating the chat photo.
     */
    @SneakyThrows
    public void changeChatPhoto(String chatUsername, InputFile photo) {
        if (permissionManager.isBotAdmin(chatUsername)) {
            bot.execute(SetChatPhoto.builder()
                    .chatId(Resolvers.linkResolver(chatUsername))
                    .photo(photo)
                    .build());
            log.info("Chat photo updated: {}", chatUsername);
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + chatUsername);
        }
    }

    /**
     * Deletes the photo of a specified chat.
     * Requires the bot to have admin privileges in the chat.
     *
     * @param chatUsername The username or identifier of the chat (e.g., @channelname).
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     * @throws Exception If an error occurs while deleting the chat photo.
     */
    @SneakyThrows
    public void deleteChatPhoto(String chatUsername) {
        if (permissionManager.isBotAdmin(chatUsername)) {
            bot.execute(DeleteChatPhoto.builder()
                    .chatId(Resolvers.linkResolver(chatUsername))
                    .build());
            log.info("Chat photo deleted: {}", chatUsername);
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + chatUsername);
        }
    }
}
