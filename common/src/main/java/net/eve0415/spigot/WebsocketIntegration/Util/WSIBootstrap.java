package net.eve0415.spigot.WebsocketIntegration.Util;

public interface WSIBootstrap {
    void onEnable();

    void onDisable();

    WSIConfiguration getWSIConfig();

    WSILogger getWSILogger();
}
