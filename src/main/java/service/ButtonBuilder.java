package service;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ButtonBuilder {
    private final TelegramLongPollingBot bot;

    public ButtonBuilder(TelegramLongPollingBot bot) {
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
    public void paginationButton(Long chatId, ArrayList<String> messagePerPage, ArrayList<String> data, int currentPage, Integer messageId) {
        int maxPage = (int) Math.ceil((double) data.size() / 10);
        String messageText = messagePerPage.get(currentPage - 1);

        InlineKeyboardMarkup markup = getPaginationWithDataButtons(currentPage, maxPage, data);

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
    private InlineKeyboardMarkup getPaginationWithDataButtons(int currentPage, int maxPage, ArrayList<String> data) {
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
