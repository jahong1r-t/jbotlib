package service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;

@Slf4j
public class MessageUpdater {
    private final TelegramLongPollingBot bot;

    public MessageUpdater(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void deleteMessage(Long chatId, Integer messageId) {
        bot.execute(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }

    @SneakyThrows
    public void editMessage(Long chatId, String message, Integer messageId) {
        bot.execute(EditMessageText.builder()
                .chatId(chatId)
                .text(message)
                .messageId(messageId)
                .parseMode("MarkdownV2")
                .build());

        log.info("chatId: {}, {}: message edited", chatId, messageId);
    }

    @SneakyThrows
    public void editCaption(Long chatId, String caption, Integer messageId) {
        bot.execute(EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .build());
        log.info("chatId: {}, {}: caption edited", chatId, messageId);
    }

    @SneakyThrows
    public void editMessagePhoto(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaPhoto(media.toString()))
                .build());
        log.info("chatId: {}, {}: photo edited", chatId, messageId);
    }

    @SneakyThrows
    public void editMessageVideo(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaVideo(media.toString()))
                .build());
        log.info("chatId: {}, {}: video edited", chatId, messageId);
    }

    @SneakyThrows
    public void editMessageDocument(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaDocument(media.toString()))
                .build());
        log.info("chatId: {}, {}: document edited", chatId, messageId);
    }
}
