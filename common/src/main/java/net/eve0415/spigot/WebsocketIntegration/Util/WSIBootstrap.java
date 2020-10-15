package net.eve0415.spigot.WebsocketIntegration.Util;

import net.eve0415.spigot.WebsocketIntegration.WebsocketManager;

public interface WSIBootstrap {
    void onEnable();

    void onDisable();

    void handleChatMessage(String name, String uuid, String url, String message);

    WSIPlatformType getPlatformType();

    WSIConfiguration getWSIConfig();

    WSILogger getWSILogger();

    WebsocketManager getWebsocketManager();
}
