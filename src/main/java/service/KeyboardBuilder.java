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

/**
 * A service class for building and managing keyboards for a Telegram bot.
 * Provides methods to create various types of keyboards, including reply keyboards,
 * inline keyboards, URL buttons, contact request buttons, location request buttons,
 * and pagination keyboards for displaying paginated content.
 *
 * <p>This class simplifies the creation of interactive keyboards, allowing developers to
 * easily integrate buttons and navigation into their Telegram bots. It supports both
 * static keyboards (ReplyKeyboardMarkup) and dynamic inline keyboards (InlineKeyboardMarkup).</p>
 *
 * @author [Your Name]
 * @version 1.0
 */
public class KeyboardBuilder {
    private final TelegramLongPollingBot bot;

    /**
     * Constructs a new KeyboardBuilder instance.
     *
     * @param bot The TelegramLongPollingBot instance used to execute API requests.
     * @throws Exception If the Telegram API initialization fails.
     */
    @SneakyThrows
    public KeyboardBuilder(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Creates a reply keyboard with the specified button layout.
     * The buttons are arranged in rows as defined by the 2D array.
     *
     * @param buttons A 2D array of strings representing the button labels, where each inner array represents a row.
     * @return A ReplyKeyboardMarkup object representing the reply keyboard.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * String[][] buttons = {{"Option 1", "Option 2"}, {"Option 3"}};
     * ReplyKeyboard keyboard = keyboardBuilder.keyboard(buttons);
     * // Use the keyboard with a message
     * </pre>
     */
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

    /**
     * Creates an inline keyboard with the specified button layout and callback data.
     * Each button is associated with a callback data string for handling user interactions.
     *
     * @param buttons A 2D array of strings representing the button labels.
     * @param data A 2D array of strings representing the callback data for each button.
     * @return An InlineKeyboardMarkup object representing the inline keyboard.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * String[][] buttons = {{"Yes", "No"}, {"Maybe"}};
     * String[][] data = {{"yes_data", "no_data"}, {"maybe_data"}};
     * ReplyKeyboard inlineKeyboard = keyboardBuilder.inlineKeyboard(buttons, data);
     * // Use the inline keyboard with a message
     * </pre>
     */
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

    /**
     * Creates an inline URL button that redirects the user to a specified URL when clicked.
     *
     * @param text The label of the button.
     * @param url The URL to redirect the user to when the button is clicked.
     * @return An InlineKeyboardMarkup object containing a single URL button.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * ReplyKeyboard urlButton = keyboardBuilder.inlineUrlButton("Visit Website", "https://example.com");
     * // Use the URL button with a message
     * </pre>
     */
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

    /**
     * Creates a reply keyboard with a button that requests the user's phone number.
     *
     * @param buttonText The label of the button.
     * @return A ReplyKeyboardMarkup object containing a single button to request the user's phone number.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * ReplyKeyboard phoneButton = keyboardBuilder.phoneNumberButton("Share Phone Number");
     * // Use the phone number button with a message
     * </pre>
     */
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

    /**
     * Creates a reply keyboard with a button that requests the user's location.
     * Note: The current implementation incorrectly uses requestContact(true) for location.
     * It should use requestLocation(true) instead.
     *
     * @param buttonText The label of the button.
     * @return A ReplyKeyboardMarkup object containing a single button to request the user's location.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * ReplyKeyboard locationButton = keyboardBuilder.locationButton("Share Location");
     * // Use the location button with a message
     * </pre>
     */
    @SneakyThrows
    public ReplyKeyboard locationButton(String buttonText) {
        KeyboardButton contactButton = KeyboardButton.builder()
                .text(buttonText)
                .requestLocation(true) // Fixed: Changed from requestContact to requestLocation
                .build();

        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(new KeyboardRow(List.of(contactButton)))
                .build();
    }

    /**
     * Sends or edits a message with a pagination keyboard for displaying paginated content.
     * The pagination keyboard includes numbered buttons for selecting items and navigation buttons
     * for moving between pages.
     *
     * @param chatId The ID of the chat where the message will be sent or edited.
     * @param messagePerPage A list of messages to display for each page.
     * @param data A list of callback data associated with each item in the pagination.
     * @param currentPage The current page number (1-based indexing).
     * @param messageId The ID of the message to edit; if null, a new message is sent.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * KeyboardBuilder keyboardBuilder = new KeyboardBuilder(bot);
     * ArrayList<String> messages = new ArrayList<>(List.of("Page 1 content", "Page 2 content"));
     * ArrayList<String> data = new ArrayList<>(List.of("item1", "item2", "item3"));
     * keyboardBuilder.sendPaginationKeyboard(123456L, messages, data, 1, null);
     * </pre>
     */
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

    /**
     * Builds a pagination keyboard with numbered buttons for items and navigation buttons.
     * The keyboard displays up to 10 items per page, with 5 items per row, and includes
     * "Previous" and "Next" buttons for navigation.
     *
     * @param currentPage The current page number (1-based indexing).
     * @param maxPage The total number of pages.
     * @param data The list of callback data for the items.
     * @return An InlineKeyboardMarkup object representing the pagination keyboard.
     * @throws Exception If the Telegram API request fails.
     *
     * @example
     * <pre>
     * // This method is private and called internally by sendPaginationKeyboard.
     * // See sendPaginationKeyboard for usage.
     * </pre>
     */
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
