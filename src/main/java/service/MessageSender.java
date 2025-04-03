package service;

import bot.JBotLib;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Slf4j
public class MessageSender extends JBotLib {

    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .build());
        log.info("Sending a message to {}", chatId);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message, ReplyKeyboard replyKeyboard) {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending  a keyboard message to {}", chatId);
    }

    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo) {
        execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .build());
        log.info("Sending a photo to {}", chatId);
    }

    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo, ReplyKeyboard replyKeyboard) {
        execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyobard photo to {}", chatId);
    }

    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document) {
        execute(SendDocument.builder()
                .chatId(chatId)
                .document(document)
                .parseMode("MarkdownV2")
                .caption(caption)
                .build());
        log.info("Sending a document to {}", chatId);
    }

    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document, ReplyKeyboard replyKeyboard) {
        execute(SendDocument.builder()
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
        execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .build());
        log.info("Sending a video to {}", chatId);
    }

    @SneakyThrows
    public void sendVideo(Long chatId, String caption, InputFile video, ReplyKeyboard replyKeyboard) {
        execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .replyMarkup(replyKeyboard)
                .build());
        log.info("Sending a keyboard video to {}", chatId);
    }
}
