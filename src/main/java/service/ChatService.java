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

/**
 * A service class for managing chat-related operations in a Telegram bot.
 * Provides methods to check bot and user admin status, manage chat members (restrict/unrestrict),
 * and modify chat settings such as photo, title, description, and pinned messages.
 *
 * <p>This class is designed to handle administrative tasks in Telegram chats, ensuring that
 * the bot has the necessary permissions before performing actions. It throws a
 * {@link BotNotAdminException} if the bot lacks admin privileges in the specified chat.</p>
 *
 * @author [Jahongir]
 * @version 1.0.0
 */
@Slf4j
public class ChatService {
    private final TelegramLongPollingBot bot;

    /**
     * Constructs a new ChatService instance.
     *
     * @param bot The TelegramLongPollingBot instance used to execute API requests.
     */
    public ChatService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Checks if the bot is an admin in the specified chat.
     *
     * @param chat The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @return {@code true} if the bot is an admin (Administrator or Owner), {@code false} otherwise.
     */
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

    /**
     * Checks if the bot is an admin in all specified chats.
     *
     * @param chats A list of chat IDs or links (resolved using {@link Resolvers#linkResolver}).
     * @return {@code true} if the bot is an admin in all chats, {@code false} if it is not an admin in at least one chat.
     */
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

    /**
     * Checks if a user is a member of the specified chat.
     *
     * @param userId The ID of the user to check.
     * @param chat   The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @return {@code true} if the user is a member of the chat, {@code false} otherwise.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
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

    /**
     * Checks if a user is a member of at least one of the specified chats.
     *
     * @param userId The ID of the user to check.
     * @param chats  A list of chat IDs or links (resolved using {@link Resolvers#linkResolver}).
     * @return {@code true} if the user is a member of at least one chat, {@code false} otherwise.
     * @throws BotNotAdminException If the bot is not an admin in any of the specified chats.
     */
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

    /**
     * Checks if a user is an admin in the specified chat.
     *
     * @param userId The ID of the user to check.
     * @param chat   The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @return {@code true} if the user is an admin (Administrator or Owner), {@code false} otherwise.
     */
    @SneakyThrows
    public boolean isUserAdmin(Long userId, String chat) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(chat));
        getChatMember.setUserId(userId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    /**
     * Restricts a chat member by revoking all permissions (effectively banning them) in the specified chat.
     *
     * @param chatId The ID of the user to restrict.
     * @param chat   The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
    @SneakyThrows
    public void restrictChatMember(Long chatId, String chat) {
        if (isBotAdmin(chat)) {
            bot.execute(new RestrictChatMember(chat, chatId, new ChatPermissions(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)));
            log.info("Banned user: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    /**
     * Restricts a chat member for a specified duration by revoking all permissions (effectively banning them temporarily).
     *
     * @param chatId   The ID of the user to restrict.
     * @param chat     The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param time     The duration for which the user will be restricted.
     * @param timeUnit The unit of time for the duration (e.g., ChronoUnit.SECONDS, ChronoUnit.MINUTES).
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
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

    /**
     * Unrestricts a chat member, lifting any previous restrictions (effectively unbanning them).
     *
     * @param chatId The ID of the user to unrestrict.
     * @param chat   The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
    @SneakyThrows
    public void unrestrictChatMember(Long chatId, String chat) {
        if (isBotAdmin(chat)) {
            bot.execute(new UnbanChatMember(Resolvers.linkResolver(chat), chatId, true));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    /**
     * Sets a new photo for the specified chat.
     *
     * @param chat  The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param photo The InputFile containing the new photo to set.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
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

    /**
     * Deletes the photo of the specified chat.
     *
     * @param chat The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
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

    /**
     * Sets a new title for the specified chat.
     *
     * @param chat  The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param title The new title for the chat.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
    @SneakyThrows
    public void setChatTitle(String chat, String title) {
        if (isBotAdmin(chat)) {
            bot.execute(new SetChatTitle(chat, title));
            log.info("Chat title updated: {}", chat);
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    /**
     * Sets a new description for the specified chat.
     *
     * @param chat        The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param description The new description for the chat.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
    @SneakyThrows
    public void setChatDescription(String chat, String description) {
        if (isBotAdmin(chat)) {
            bot.execute(new SetChatDescription(Resolvers.linkResolver(chat), description));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }

    /**
     * Pins a message in the specified chat.
     *
     * @param chatId    The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param messageId The ID of the message to pin.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
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

    /**
     * Unpins a message in the specified chat.
     *
     * @param chat      The chat ID or link (resolved using {@link Resolvers#linkResolver}).
     * @param messageId The ID of the message to unpin.
     * @throws BotNotAdminException If the bot is not an admin in the specified chat.
     */
    @SneakyThrows
    public void unpinMessage(String chat, Integer messageId) {
        if (isBotAdmin(chat)) {
            bot.execute(new UnpinChatMessage(Resolvers.linkResolver(chat), messageId));
        } else {
            throw new BotNotAdminException("Bot is not admin of this chat: " + chat);
        }
    }
}
