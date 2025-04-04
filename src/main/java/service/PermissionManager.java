package service;


import exceptions.BotNotAdminException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import utils.Resolvers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class PermissionManager {
    private final TelegramLongPollingBot bot;

    public PermissionManager(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public boolean isBotAdmin(String channel) {
        User botUser = bot.execute(new GetMe());
        Long botId = botUser.getId();

        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(channel));
        getChatMember.setUserId(botId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    @SneakyThrows
    public boolean isBotAdmin(List<String> channels) {
        User botUser = bot.execute(new GetMe());
        Long botId = botUser.getId();

        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setUserId(botId);

        for (String s : channels) {
            getChatMember.setChatId(Resolvers.linkResolver(s));
            ChatMember member = bot.execute(getChatMember);

            if (!(member instanceof ChatMemberAdministrator)) {
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    public boolean isJoinedChannel(Long userId, String channel) {
        if (isBotAdmin(channel)) {
            ChatMember execute = bot.execute(GetChatMember.builder()
                    .chatId(Resolvers.linkResolver(channel))
                    .userId(userId)
                    .build());

            return execute.getStatus().equals("member");
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }

    @SneakyThrows
    public boolean isJoinedChannel(Long userId, List<String> channels) {
        for (String channel : channels) {
            if (isBotAdmin(channel)) {
                ChatMember execute = bot.execute(GetChatMember.builder()
                        .chatId(Resolvers.linkResolver(channel))
                        .userId(userId)
                        .build());

                if (execute.getStatus().equals("member")) {
                    return true;
                }
            } else {
                throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
            }
        }
        return false;
    }

    @SneakyThrows
    public boolean isUserAdmin(Long userId, String channel) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(channel));
        getChatMember.setUserId(userId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    @SneakyThrows
    public void banUser(Long chatId, String channel) {
        if (isBotAdmin(channel)) {
            bot.execute(new RestrictChatMember(channel, chatId, new ChatPermissions(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)));
            log.info("Banned user: {}", channel);
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }

    @SneakyThrows
    public void banUser(Long chatId, String channel, Integer time, ChronoUnit timeUnit) {
        if (isBotAdmin(channel)) {
            Integer untilDate = (int) Instant.now().plus(time, timeUnit).getEpochSecond();

            bot.execute(new RestrictChatMember(channel, chatId, new ChatPermissions(), untilDate, true));
            log.info("Banned user: {} in channel: {} for {} {}", chatId, channel, time, timeUnit);

        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }

    @SneakyThrows
    public void unbanUser(Long chatId, String channel) {
        if (isBotAdmin(channel)) {
            bot.execute(new UnbanChatMember(Resolvers.linkResolver(channel), chatId, true));
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }
}
