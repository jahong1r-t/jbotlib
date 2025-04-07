package service;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KeyboardBuilder {
    private final TelegramLongPollingBot bot;

    @SneakyThrows
    public KeyboardBuilder(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public ReplyKeyboard keyboard(String[][] buttons) {
        List<KeyboardRow> rows = Arrays.stream(buttons)
                .map(row -> {
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardRow.addAll(Arrays.asList(row));
                    return keyboardRow;
                })
                .collect(Collectors.toList());

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .build();
    }

    @SneakyThrows
    public ReplyKeyboard inlineKeyboard(String[][] buttons, String[][] data) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows =
                IntStream.range(0, buttons.length)
                        .mapToObj(i -> IntStream.range(0, buttons[i].length)
                                .mapToObj(j -> InlineKeyboardButton.builder()
                                        .callbackData(data[i][j])
                                        .text(buttons[i][j])
                                        .build())
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());

        markup.setKeyboard(rows);

        return markup;
    }

    @SneakyThrows
    public ReplyKeyboard inlineUrlButton(String text, String url) {
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text(text)
                .url(url)
                .build();
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(button)))
                .build();
    }

    @SneakyThrows
    public ReplyKeyboard phoneNumberButton(String buttonText) {
        KeyboardButton contactButton = KeyboardButton.builder()
                .text(buttonText)
                .requestContact(true)
                .build();

        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(new KeyboardRow(List.of(contactButton)))
                .build();
    }

    @SneakyThrows
    public ReplyKeyboard locationButton(String buttonText) {
        KeyboardButton contactButton = KeyboardButton.builder()
                .text(buttonText)
                .requestContact(true)
                .build();

        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(new KeyboardRow(List.of(contactButton)))
                .build();
    }

    @SneakyThrows
    public void sendPaginationKeyboard(Long chatId, ArrayList<String> messagePerPage, ArrayList<String> data, int currentPage, Integer messageId) {
        int maxPage = (int) Math.ceil((double) data.size() / 10);
        String messageText = messagePerPage.get(currentPage - 1);

        InlineKeyboardMarkup markup = buildPaginationKeyboard(currentPage, maxPage, data);

        if (messageId == null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(messageText);
            sendMessage.setReplyMarkup(markup);
            bot.execute(sendMessage);
        } else {
            EditMessageText editMessage = new EditMessageText();
            editMessage.setChatId(chatId);
            editMessage.setMessageId(messageId);
            editMessage.setText(messageText);
            editMessage.setReplyMarkup(markup);
            bot.execute(editMessage);
        }
    }

    @SneakyThrows
    private InlineKeyboardMarkup buildPaginationKeyboard(int currentPage, int maxPage, ArrayList<String> data) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int itemsPerPage = 10;
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, data.size());

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            int buttonNumber = (i % 10) + 1;
            row.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(buttonNumber))
                    .callbackData(data.get(i))
                    .build());

            if (row.size() == 5) {
                keyboard.add(new ArrayList<>(row));
                row.clear();
            }
        }

        if (!row.isEmpty()) {
            keyboard.add(new ArrayList<>(row));
        }

        List<InlineKeyboardButton> navigationButtons = new ArrayList<>();

        int prevPage = (currentPage > 1) ? currentPage - 1 : currentPage;
        int nextPage = (currentPage < maxPage) ? currentPage + 1 : currentPage;

        navigationButtons.add(InlineKeyboardButton.builder().text("⬅️").callbackData("page:" + prevPage).build());
        navigationButtons.add(InlineKeyboardButton.builder().text("➡️").callbackData("page:" + nextPage).build());
        keyboard.add(navigationButtons);

        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }
}
