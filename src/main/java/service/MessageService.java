package service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Slf4j
public class MessageService {
    private final TelegramLongPollingBot bot;

    public MessageService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .build());
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message, ReplyKeyboard replyKeyboard) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .replyMarkup(replyKeyboard)
                .build());
    }

    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo) {
        bot.execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .build());
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
    }

    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document) {
        bot.execute(SendDocument.builder()
                .chatId(chatId)
                .document(document)
                .parseMode("MarkdownV2")
                .caption(caption)
                .build());
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
    }

    @SneakyThrows
    public void sendVideo(Long chatId, String caption, InputFile video) {
        bot.execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .build());
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
    }

    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .build());
    }

    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options, boolean allowMultipleAnswers) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .allowMultipleAnswers(allowMultipleAnswers)
                .build());
    }

    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options, String explanation) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .explanation(explanation)
                .build());
    }

    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options, Integer time, ChronoUnit timeUnit) {
        Integer untilDate = (int) Instant.now().plus(time, timeUnit).getEpochSecond();

        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .closeDate(untilDate)
                .build());
    }

    @SneakyThrows
    public void sendMediaGroup(Long chatId, String caption, List<InputMedia> mediaGroup) {
        if (!mediaGroup.isEmpty()) {
            mediaGroup.get(0).setCaption(caption);
            bot.execute(SendMediaGroup.builder()
                    .chatId(chatId)
                    .medias(mediaGroup)
                    .build());
        }
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
    }

    @SneakyThrows
    public void editMessageCaption(Long chatId, String caption, Integer messageId) {
        bot.execute(EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .build());
    }

    @SneakyThrows
    public void editMessagePhoto(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaPhoto(media.toString()))
                .build());
    }

    @SneakyThrows
    public void editMessageVideo(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaVideo(media.toString()))
                .build());
    }

    @SneakyThrows
    public void editMessageDocument(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaDocument(media.toString()))
                .build());
    }
}
