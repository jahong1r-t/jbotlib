package utils;

import exceptions.InvalidChannelLinkException;
import lombok.SneakyThrows;

public class Resolvers {
    @SneakyThrows
    public static String linkResolver(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidChannelLinkException("Channel identifier cannot be null or empty");
        }

        if (raw.startsWith("@") || raw.startsWith("-100")) {
            return raw;
        }

        if (raw.startsWith("https://t.me/")) {
            return "@" + raw.substring("https://t.me/".length());
        }
        return "@" + raw;
    }

}
