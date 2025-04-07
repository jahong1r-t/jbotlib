package service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * A utility class for sending various types of messages in Telegram chats.
 * Supports sending text messages, photos, documents, and videos, with optional keyboards.
 */
@Slf4j
public class MessageSender {
    private final TelegramLongPollingBot bot;

    /**
     * Constructor for MessageSender.
     *
     * @param bot The TelegramLongPollingBot instance used to send messages via the Telegram API.
     */
    public MessageSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Sends a text message to a specified chat.
     * The message is formatted using MarkdownV2.
     *
     * @param chatId  The ID of the chat where the message will be sent.
     * @param message The text content of the message.
     * @throws Exception If an error occurs while sending the message.
     */
    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .build());
        log.info("Sending a message to {}", chatId);
    }

    /**
     * Sends a text message with a reply keyboard to a specified chat.
     * The message is formatted using MarkdownV2.
     *
     * @param chatId        The ID of the chat where the message will be sent.
     * @param message       The text content of the message.
     * @param replyKeyboard The keyboard markup to include with the message.
     * @throws Exception If an error occurs while sending the message.
     */
    @SneakyThrows
    public void sendMessage(Long chatId, String message, ReplyKeyboard replyKeyboard) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyboard message to {}", chatId);
    }

    /**
     * Sends a photo with a caption to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId  The ID of the chat where the photo will be sent.
     * @param caption The caption for the photo.
     * @param photo   The photo file to send.
     * @throws Exception If an error occurs while sending the photo.
     */
    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo) {
        bot.execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .build());
        log.info("Sending a photo to {}", chatId);
    }

    /**
     * Sends a photo with a caption and a reply keyboard to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId        The ID of the chat where the photo will be sent.
     * @param caption       The caption for the photo.
     * @param photo         The photo file to send.
     * @param replyKeyboard The keyboard markup to include with the photo.
     * @throws Exception If an error occurs while sending the photo.
     */
    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo, ReplyKeyboard replyKeyboard) {
        bot.execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyboard photo to {}", chatId);
    }

    /**
     * Sends a document with a caption to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId   The ID of the chat where the document will be sent.
     * @param caption  The caption for the document.
     * @param document The document file to send.
     * @throws Exception If an error occurs while sending the document.
     */
    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document) {
        bot.execute(SendDocument.builder()
                .chatId(chatId)
                .document(document)
                .parseMode("MarkdownV2")
                .caption(caption)
                .build());
        log.info("Sending a document to {}", chatId);
    }

    /**
     * Sends a document with a caption and a reply keyboard to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId        The ID of the chat where the document will be sent.
     * @param caption       The caption for the document.
     * @param document      The document file to send.
     * @param replyKeyboard The keyboard markup to include with the document.
     * @throws Exception If an error occurs while sending the document.
     */
    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document, ReplyKeyboard replyKeyboard) {
        bot.execute(SendDocument.builder()
                .chatId(chatId)
                .document(document)
                .parseMode("MarkdownV2")
                .caption(caption)
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyboard document to {}", chatId);
    }

    /**
     * Sends a video with a caption to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId  The ID of the chat where the video will be sent.
     * @param caption The caption for the video.
     * @param video   The video file to send.
     * @throws Exception If an error occurs while sending the video.
     */
    @SneakyThrows
    public void sendVideo(Long chatId, String caption, InputFile video) {
        bot.execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .build());
        log.info("Sending a video to {}", chatId);
    }

    /**
     * Sends a video with a caption and a reply keyboard to a specified chat.
     * The caption is formatted using MarkdownV2.
     *
     * @param chatId        The ID of the chat where the video will be sent.
     * @param caption       The caption for the video.
     * @param video         The video file to send.
     * @param replyKeyboard The keyboard markup to include with the video.
     * @throws Exception If an error occurs while sending the video.
     */
    @SneakyThrows
    public void sendVideo(Long chatId, String caption, InputFile video, ReplyKeyboard replyKeyboard) {
        bot.execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyboard video to {}", chatId);
    }
}
