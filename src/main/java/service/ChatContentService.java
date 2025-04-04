package service;

import exceptions.BotNotAdminException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.DeleteChatPhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import utils.Resolvers;

@Slf4j
public class ChatContentService {
    private final TelegramLongPollingBot bot;
    private final PermissionManager permissionManager;

    public ChatContentService(TelegramLongPollingBot bot) {
        this.bot = bot;
        this.permissionManager = new PermissionManager(bot);
    }

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
