package net.eve0415.spigot.WebsocketIntegration.Util;

import net.eve0415.spigot.WebsocketIntegration.WebsocketManager;

public interface WSIBootstrap {
    void onEnable();

    void onDisable();

    WSIConfiguration getWSIConfig();

    WSILogger getWSILogger();

    WebsocketManager getWebsocketManager();
}
