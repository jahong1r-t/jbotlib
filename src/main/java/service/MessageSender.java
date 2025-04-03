package service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Slf4j
public class MessageSender {
    private final TelegramLongPollingBot bot;

    public MessageSender(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .build());
        log.info("Sending a message to {}", chatId);
    }

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
