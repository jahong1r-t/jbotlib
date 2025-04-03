package service;

import bot.JBotLib;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonBuilder extends JBotLib {

    @SneakyThrows
    public static ReplyKeyboard keyboard(String[][] buttons) {
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


}
