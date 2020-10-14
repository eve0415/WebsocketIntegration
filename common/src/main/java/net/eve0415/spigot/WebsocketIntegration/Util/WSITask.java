package net.eve0415.spigot.WebsocketIntegration.Util;

public interface WSITask {
    void serverIsReady();

    void updateStatus();

    void autoUpdateStatus();
}
