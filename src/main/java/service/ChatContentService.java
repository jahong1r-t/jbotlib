package service;

import exceptions.InvalidChannelLinkException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.DeleteChatPhoto;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Slf4j
public class ChatContentService {
    private final TelegramLongPollingBot bot;

    public ChatContentService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void changeChatPhoto(String chatUsername, InputFile photo) {
        if (chatUsername.startsWith("@") || chatUsername.matches("https?://t.me/.+")) {
            bot.execute(SetChatPhoto.builder()
                    .chatId(chatUsername.startsWith("@") ? chatUsername : "@" + chatUsername.substring("https://t.me/".length()))
                    .photo(photo)
                    .build());
            log.info("Chat photo updated: {}", chatUsername);
        } else {
            log.warn("Chat photo not changed: {}", chatUsername);
            throw new InvalidChannelLinkException("Invalid channel link: " + chatUsername);
        }
    }

    @SneakyThrows
    public void deleteChatPhoto(String chatUsername) {
        if (chatUsername.startsWith("@") || chatUsername.matches("https?://t.me/.+")) {
            bot.execute(DeleteChatPhoto.builder()
                    .chatId(chatUsername.startsWith("@") ? chatUsername : "@" + chatUsername.substring("https://t.me/".length()))
                    .build());

            log.info("Chat photo deleted: {}", chatUsername);
        } else {
            log.warn("Chat photo not deleted: {}", chatUsername);
            throw new InvalidChannelLinkException("Invalid channel link: " + chatUsername);
        }
    }
}
