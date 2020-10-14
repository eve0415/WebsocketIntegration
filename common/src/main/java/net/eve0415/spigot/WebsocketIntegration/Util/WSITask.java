package net.eve0415.spigot.WebsocketIntegration.Util;

public interface WSITask extends ServerStatusInfo {
    void serverIsReady();

    void updateStatus();
}
