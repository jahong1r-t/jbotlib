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

/**
 * A service class for handling message-related operations in a Telegram bot.
 * Provides methods to send various types of messages (text, photos, videos, documents, polls, media groups),
 * edit existing messages, and delete messages in a Telegram chat.
 * All messages are sent with MarkdownV2 parsing enabled by default for rich text formatting.
 *
 * <p>This class is designed to simplify interaction with the Telegram Bot API by encapsulating
 * common message-sending and editing operations, making it easier for developers to build
 * interactive Telegram bots.</p>
 *
 * @author [jahong1r-t]
 * @version 1.0
 */
@Slf4j
public class MessageService {
    private final TelegramLongPollingBot bot;

    /**
     * Constructs a new MessageService instance.
     *
     * @param bot The TelegramLongPollingBot instance used to execute API requests.
     */
    public MessageService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Sends a text message to a specified chat.
     *
     * @param chatId  The ID of the chat where the message will be sent.
     * @param message The text content of the message (supports MarkdownV2 formatting).
     *                If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .build());
    }

    /**
     * Sends a text message to a specified chat with a custom reply keyboard.
     *
     * @param chatId        The ID of the chat where the message will be sent.
     * @param message       The text content of the message (supports MarkdownV2 formatting).
     * @param replyKeyboard The custom reply keyboard to attach to the message.
     *                      If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendMessage(Long chatId, String message, ReplyKeyboard replyKeyboard) {
        bot.execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("MarkdownV2")
                .replyMarkup(replyKeyboard)
                .build());
    }

    /**
     * Sends a photo to a specified chat with a caption.
     *
     * @param chatId  The ID of the chat where the photo will be sent.
     * @param caption The caption for the photo (supports MarkdownV2 formatting).
     * @param photo   The InputFile containing the photo to send.
     *                If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendPhoto(Long chatId, String caption, InputFile photo) {
        bot.execute(SendPhoto.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .photo(photo)
                .build());
    }

    /**
     * Sends a photo to a specified chat with a caption and a custom reply keyboard.
     *
     * @param chatId        The ID of the chat where the photo will be sent.
     * @param caption       The caption for the photo (supports MarkdownV2 formatting).
     * @param photo         The InputFile containing the photo to send.
     * @param replyKeyboard The custom reply keyboard to attach to the message.
     *                      If the Telegram API request fails.
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
    }

    /**
     * Sends a document to a specified chat with a caption.
     *
     * @param chatId   The ID of the chat where the document will be sent.
     * @param caption  The caption for the document (supports MarkdownV2 formatting).
     * @param document The InputFile containing the document to send.
     *                 If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendDocument(Long chatId, String caption, InputFile document) {
        bot.execute(SendDocument.builder()
                .chatId(chatId)
                .document(document)
                .parseMode("MarkdownV2")
                .caption(caption)
                .build());
    }

    /**
     * Sends a document to a specified chat with a caption and a custom reply keyboard.
     *
     * @param chatId        The ID of the chat where the document will be sent.
     * @param caption       The caption for the document (supports MarkdownV2 formatting).
     * @param document      The InputFile containing the document to send.
     * @param replyKeyboard The custom reply keyboard to attach to the message.
     *                      If the Telegram API request fails.
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
    }

    /**
     * Sends a video to a specified chat with a caption.
     *
     * @param chatId  The ID of the chat where the video will be sent.
     * @param caption The caption for the video (supports MarkdownV2 formatting).
     * @param video   The InputFile containing the video to send.
     *                If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendVideo(Long chatId, String caption, InputFile video) {
        bot.execute(SendVideo.builder()
                .chatId(chatId)
                .caption(caption)
                .parseMode("MarkdownV2")
                .video(video)
                .build());
    }

    /**
     * Sends a video to a specified chat with a caption and a custom reply keyboard.
     *
     * @param chatId        The ID of the chat where the video will be sent.
     * @param caption       The caption for the video (supports MarkdownV2 formatting).
     * @param video         The InputFile containing the video to send.
     * @param replyKeyboard The custom reply keyboard to attach to the message.
     *                      If the Telegram API request fails.
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
    }

    /**
     * Sends a poll to a specified chat with a question and options.
     *
     * @param chatId   The ID of the chat where the poll will be sent.
     * @param question The question for the poll.
     * @param options  The list of options for the poll (at least 2 options required).
     *                 If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .build());
    }

    /**
     * Sends a poll to a specified chat with a question, options, and multiple answer support.
     *
     * @param chatId               The ID of the chat where the poll will be sent.
     * @param question             The question for the poll.
     * @param options              The list of options for the poll (at least 2 options required).
     * @param allowMultipleAnswers Whether users can select multiple answers.
     *                             If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options, boolean allowMultipleAnswers) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .allowMultipleAnswers(allowMultipleAnswers)
                .build());
    }

    /**
     * Sends a poll to a specified chat with a question, options, and an explanation for the correct answer.
     *
     * @param chatId      The ID of the chat where the poll will be sent.
     * @param question    The question for the poll.
     * @param options     The list of options for the poll (at least 2 options required).
     * @param explanation The explanation to show when the poll is answered (e.g., for quizzes).
     *                    If the Telegram API request fails.
     */
    @SneakyThrows
    public void sendPoll(Long chatId, String question, List<String> options, String explanation) {
        bot.execute(SendPoll.builder()
                .chatId(chatId)
                .question(question)
                .options(options)
                .explanation(explanation)
                .build());
    }

    /**
     * Sends a poll to a specified chat with a question, options, and a time limit for voting.
     *
     * @param chatId   The ID of the chat where the poll will be sent.
     * @param question The question for the poll.
     * @param options  The list of options for the poll (at least 2 options required).
     * @param time     The duration for which the poll will be open.
     * @param timeUnit The unit of time for the duration (e.g., ChronoUnit.SECONDS, ChronoUnit.MINUTES).
     *                 If the Telegram API request fails.
     **/
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

    /**
     * Sends a media group (e.g., multiple photos or videos) to a specified chat with a caption.
     * The caption is applied to the first media item in the group.
     *
     * @param chatId     The ID of the chat where the media group will be sent.
     * @param caption    The caption for the media group (applied to the first item).
     * @param mediaGroup The list of InputMedia objects (e.g., photos, videos) to send.
     *                   If the Telegram API request fails or the media group is empty.
     */
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

    /**
     * Deletes a message from a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param messageId The ID of the message to delete.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void deleteMessage(Long chatId, Integer messageId) {
        bot.execute(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build());
    }

    /**
     * Edits the text of an existing message in a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param message   The new text content of the message (supports MarkdownV2 formatting).
     * @param messageId The ID of the message to edit.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void editMessage(Long chatId, String message, Integer messageId) {
        bot.execute(EditMessageText.builder()
                .chatId(chatId)
                .text(message)
                .messageId(messageId)
                .parseMode("MarkdownV2")
                .build());
    }

    /**
     * Edits the caption of an existing media message in a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param caption   The new caption for the media message.
     * @param messageId The ID of the message to edit.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void editMessageCaption(Long chatId, String caption, Integer messageId) {
        bot.execute(EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption(caption)
                .build());
    }

    /**
     * Edits the photo of an existing media message in a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param media     The new InputFile containing the photo to replace the existing media.
     * @param messageId The ID of the message to edit.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void editMessagePhoto(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaPhoto(media.toString()))
                .build());
    }

    /**
     * Edits the video of an existing media message in a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param media     The new InputFile containing the video to replace the existing media.
     * @param messageId The ID of the message to edit.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void editMessageVideo(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaVideo(media.toString()))
                .build());
    }

    /**
     * Edits the document of an existing media message in a specified chat.
     *
     * @param chatId    The ID of the chat where the message is located.
     * @param media     The new InputFile containing the document to replace the existing media.
     * @param messageId The ID of the message to edit.
     *                  If the Telegram API request fails.
     */
    @SneakyThrows
    public void editMessageDocument(Long chatId, InputFile media, Integer messageId) {
        bot.execute(EditMessageMedia.builder()
                .chatId(chatId)
                .messageId(messageId)
                .media(new InputMediaDocument(media.toString()))
                .build());
    }
}
