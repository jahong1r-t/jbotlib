package service;

import exceptions.BotNotAdminException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinChatMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import utils.Resolvers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class ChatService {
    private final TelegramLongPollingBot bot;

    public ChatService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public boolean isBotAdmin(String chat) {
        User botUser = bot.execute(new GetMe());
        Long botId = botUser.getId();

        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(chat));
        getChatMember.setUserId(botId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    @SneakyThrows
    public boolean isBotAdmin(List<String> chats) {
        User botUser = bot.execute(new GetMe());
        Long botId = botUser.getId();

        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setUserId(botId);

        for (String s : chats) {
            getChatMember.setChatId(Resolvers.linkResolver(s));
            ChatMember member = bot.execute(getChatMember);

            if (!(member instanceof ChatMemberAdministrator)) {
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    public boolean isChatMember(Long userId, String chat) {
        if (isBotAdmin(chat)) {
            ChatMember execute = bot.execute(GetChatMember.builder()
                    .chatId(Resolvers.linkResolver(chat))
                    .userId(userId)
                    .build());

            return execute.getStatus().equals("member");
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public boolean isChatMember(Long userId, List<String> chats) {
        for (String chat : chats) {
            if (isBotAdmin(chat)) {
                ChatMember execute = bot.execute(GetChatMember.builder()
                        .chatId(Resolvers.linkResolver(chat))
                        .userId(userId)
                        .build());

                if (execute.getStatus().equals("member")) {
                    return true;
                }
            } else {
                throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
            }
        }
        return false;
    }

    @SneakyThrows
    public boolean isUserAdmin(Long userId, String chat) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(chat));
        getChatMember.setUserId(userId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    @SneakyThrows
    public void restrictChatMember(Long chatId, String chat) {
        if (isBotAdmin(chat)) {
            bot.execute(new RestrictChatMember(chat, chatId, new ChatPermissions(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)));
            log.info("Banned user: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public void restrictChatMember(Long chatId, String chat, Integer time, ChronoUnit timeUnit) {
        if (isBotAdmin(chat)) {
            Integer untilDate = (int) Instant.now().plus(time, timeUnit).getEpochSecond();

            bot.execute(new RestrictChatMember(chat, chatId, new ChatPermissions(), untilDate, true));
            log.info("Banned user: {} in chat: {} for {} {}", chatId, chat, time, timeUnit);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }


    @SneakyThrows
    public void unrestrictChatMember(Long chatId, String chat) {
        if (isBotAdmin(chat)) {
            bot.execute(new UnbanChatMember(Resolvers.linkResolver(chat), chatId, true));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }


    @SneakyThrows
    public void setChatPhoto(String chat, InputFile photo) {
        if (isBotAdmin(chat)) {
            bot.execute(SetChatPhoto.builder()
                    .chatId(Resolvers.linkResolver(chat))
                    .photo(photo)
                    .build());
            log.info("Chat photo updated: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public void deleteChatPhoto(String chat) {
        if (isBotAdmin(chat)) {
            bot.execute(DeleteChatPhoto.builder()
                    .chatId(Resolvers.linkResolver(chat))
                    .build());
            log.info("Chat photo deleted: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public void setChatTitle(String chat, String title) {
        if (isBotAdmin(chat)) {
            bot.execute(new SetChatTitle(chat, title));
            log.info("Chat title updated: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public void setChatDescription(String chat, String description) {
        if (isBotAdmin(chat)) {
            bot.execute(new SetChatDescription(Resolvers.linkResolver(chat), description));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    @SneakyThrows
    public void pinMessage(String chatId, Integer messageId) {
        if (isBotAdmin(chatId)) {
            bot.execute(PinChatMessage.builder()
                    .chatId(Resolvers.linkResolver(chatId))
                    .messageId(messageId).build());
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chatId);
        }
    }

    @SneakyThrows
    public void unpinMessage(String chat, Integer messageId) {
        if (isBotAdmin(chat)) {
            bot.execute(new UnpinChatMessage(Resolvers.linkResolver(chat), messageId));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }
}
