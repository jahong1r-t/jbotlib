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

/**
 * A utility class for managing permissions and membership status in Telegram channels or groups.
 * Provides methods to check bot/user admin status, user membership, and to ban/unban users.
 */
@Slf4j
public class PermissionManager {
    private final TelegramLongPollingBot bot;

    /**
     * Constructor for PermissionManager.
     *
     * @param bot The TelegramLongPollingBot instance used to interact with Telegram API.
     */
    public PermissionManager(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    /**
     * Checks if the bot is an admin in a specified channel or group.
     *
     * @param channel The channel or group identifier (e.g., username or chat ID).
     * @return {@code true} if the bot is an admin or owner, {@code false} otherwise.
     * @throws Exception If an error occurs while retrieving chat member information.
     */
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

    /**
     * Checks if the bot is an admin in all specified channels or groups.
     *
     * @param channels A list of channel or group identifiers.
     * @return {@code true} if the bot is an admin in all channels, {@code false} if not in at least one.
     * @throws Exception If an error occurs while retrieving chat member information.
     */
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

    /**
     * Checks if a user is a member of a specified channel or group.
     * Requires the bot to be an admin in the channel.
     *
     * @param userId The ID of the user to check.
     * @param channel The channel or group identifier.
     * @return {@code true} if the user is a member, {@code false} otherwise.
     * @throws BotNotAdminException If the bot is not an admin in the specified channel.
     * @throws Exception If an error occurs while retrieving chat member information.
     */
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

    /**
     * Checks if a user is a member of at least one of the specified channels or groups.
     * Requires the bot to be an admin in each checked channel.
     *
     * @param userId The ID of the user to check.
     * @param channels A list of channel or group identifiers.
     * @return {@code true} if the user is a member of at least one channel, {@code false} otherwise.
     * @throws BotNotAdminException If the bot is not an admin in any of the specified channels.
     * @throws Exception If an error occurs while retrieving chat member information.
     */
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

    /**
     * Checks if a user is an admin in a specified channel or group.
     *
     * @param userId The ID of the user to check.
     * @param channel The channel or group identifier.
     * @return {@code true} if the user is an admin or owner, {@code false} otherwise.
     * @throws Exception If an error occurs while retrieving chat member information.
     */
    @SneakyThrows
    public boolean isUserAdmin(Long userId, String channel) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(Resolvers.linkResolver(channel));
        getChatMember.setUserId(userId);

        ChatMember member = bot.execute(getChatMember);

        return (member instanceof ChatMemberAdministrator) || (member instanceof ChatMemberOwner);
    }

    /**
     * Bans a user from a specified channel or group permanently.
     * Requires the bot to be an admin in the channel.
     *
     * @param chatId The ID of the user to ban.
     * @param channel The channel or group identifier.
     * @throws BotNotAdminException If the bot is not an admin in the specified channel.
     * @throws Exception If an error occurs while restricting the user.
     */
    @SneakyThrows
    public void banUser(Long chatId, String channel) {
        if (isBotAdmin(channel)) {
            bot.execute(new RestrictChatMember(channel, chatId, new ChatPermissions(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)));
            log.info("Banned user: {}", channel);
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }

    /**
     * Bans a user from a specified channel or group for a specified duration.
     * Requires the bot to be an admin in the channel.
     *
     * @param chatId The ID of the user to ban.
     * @param channel The channel or group identifier.
     * @param time The duration of the ban.
     * @param timeUnit The unit of time for the ban duration (e.g., HOURS, DAYS).
     * @throws BotNotAdminException If the bot is not an admin in the specified channel.
     * @throws Exception If an error occurs while restricting the user.
     */
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

    /**
     * Unbans a user from a specified channel or group.
     * Requires the bot to be an admin in the channel.
     *
     * @param chatId The ID of the user to unban.
     * @param channel The channel or group identifier.
     * @throws BotNotAdminException If the bot is not an admin in the specified channel.
     * @throws Exception If an error occurs while unbanning the user.
     */
    @SneakyThrows
    public void unbanUser(Long chatId, String channel) {
        if (isBotAdmin(channel)) {
            bot.execute(new UnbanChatMember(Resolvers.linkResolver(channel), chatId, true));
        } else {
            throw new BotNotAdminException("Bot is not admin of this channel: " + channel);
        }
    }
}
