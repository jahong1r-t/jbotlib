package utils;

import exceptions.InvalidChannelLinkException;
import lombok.SneakyThrows;

public class Resolvers {
    @SneakyThrows
    public static String linkResolver(String link) {
        if (link == null || link.isBlank()) {
            throw new InvalidChannelLinkException("Channel identifier cannot be null or empty");
        }

        if (link.startsWith("@") || link.startsWith("-100")) {
            return link;
        }

        if (link.startsWith("https://t.me/")) {
            String username = link.substring("https://t.me/".length());
            if (username.isEmpty()) {
                throw new InvalidChannelLinkException("No username found in Telegram link");
            }
            return "@" + username;
        }

        throw new InvalidChannelLinkException("Invalid Telegram link format");
    }
}
