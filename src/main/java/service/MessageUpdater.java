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

/**
 * A utility class for updating and deleting messages in Telegram chats.
 * Provides methods to delete messages, edit text, captions, and media (photos, videos, documents).
 */
@Slf4j
public class MessageUpdater {
    private final TelegramLongPollingBot bot;

    /**
     * Constructor for MessageUpdater.
     *
     * @param bot The TelegramLongPollingBot instance used to interact with Telegram API.
     */
    public MessageUpdater(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Deletes a message in a specified chat.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param messageId The ID of the message to delete.
     * @throws Exception If an error occurs while deleting the message.
     */
    @SneakyThrows
    public void deleteMessage(Long chatId, Integer messageId) {
        bot.execute(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }

    /**
     * Edits the text of an existing message in a chat.
     * The updated text is formatted using MarkdownV2.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param message The new text to replace the existing message content.
     * @param messageId The ID of the message to edit.
     * @throws Exception If an error occurs while editing the message.
     */
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

    /**
     * Edits the caption of an existing message in a chat.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param caption The new caption to replace the existing one.
     * @param messageId The ID of the message to edit.
     * @throws Exception If an error occurs while editing the caption.
     */
    @SneakyThrows
    public void editCaption(Long chatId, String caption, Integer messageId) {
        bot.execute(EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .build());
        log.info("chatId: {}, {}: caption edited", chatId, messageId);
    }

    /**
     * Edits the photo of an existing message in a chat.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param media The new photo file to replace the existing media.
     * @param messageId The ID of the message to edit.
     * @throws Exception If an error occurs while editing the photo.
     */
    @SneakyThrows
    public void editMessagePhoto(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaPhoto(media.toString()))
                .build());
        log.info("chatId: {}, {}: photo edited", chatId, messageId);
    }

    /**
     * Edits the video of an existing message in a chat.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param media The new video file to replace the existing media.
     * @param messageId The ID of the message to edit.
     * @throws Exception If an error occurs while editing the video.
     */
    @SneakyThrows
    public void editMessageVideo(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaVideo(media.toString()))
                .build());
        log.info("chatId: {}, {}: video edited", chatId, messageId);
    }

    /**
     * Edits the document of an existing message in a chat.
     *
     * @param chatId The ID of the chat where the message is located.
     * @param media The new document file to replace the existing media.
     * @param messageId The ID of the message to edit.
     * @throws Exception If an error occurs while editing the document.
     */
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
