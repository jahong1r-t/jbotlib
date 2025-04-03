package service;

import bot.JBotLib;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

@Slf4j
public class MessageUpdater extends JBotLib {

    @SneakyThrows
    public void deleteMessage(Long chatId, Integer messageId) {
        execute(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }

    @SneakyThrows
    public void editMessage(Long chatId, String message, Integer messageId) {
        execute(EditMessageText.builder()
                .chatId(chatId)
                .text(message)
                .messageId(messageId)
                .parseMode("MarkdownV2")
                .build());

        log.info("chatId: {}, {}: message edited", chatId, messageId);
    }

    @SneakyThrows
    public void editMessagePhoto(Long chatId, InputFile media, Integer messageId) {
        execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaPhoto(media.toString()))
                .build());
    }

    @SneakyThrows
    public void editCaption(Long chatId, String caption, Integer messageId) {
        execute(EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .build());
    }

}
