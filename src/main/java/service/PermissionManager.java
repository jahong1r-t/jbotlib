package service;


import exceptions.InvalidChannelLinkException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.List;

@Slf4j
public class PermissionManager {
    private final TelegramLongPollingBot bot;

    public PermissionManager(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public boolean isJoinedChannel(Long userId, List<String> channels) {
        for (String channel : channels) {
            if (channel.startsWith("@")) {
                ChatMember execute = bot.execute(GetChatMember.builder()
                        .chatId(channel)
                        .userId(userId)
                        .build());

                if ("member".equals(execute.getStatus())) {
                    return true;
                }
            } else if (channel.matches("https?://t.me/.+")) {
                ChatMember execute = bot.execute(GetChatMember.builder()
                        .chatId(channel)
                        .userId(userId)
                        .build());

                if ("member".equals(execute.getStatus())) {
                    return true;
                }
            } else {
                log.warn("Invalid channel link format: {}", channel);
                throw new InvalidChannelLinkException("Invalid channel link format: " + channel);
            }
        }
        return false;
    }

}
