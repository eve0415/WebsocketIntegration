package net.eve0415.spigot.WebsocketIntegration.Util;

public interface WSIChatHandler {
    void chatHandler(String name, String uuid, String url, String message);

    String getName(String name, String uuid);

    static String messageFormatter(final String name, final String message) {
        return String.format("<" + name + "> " + message);
    }
}
