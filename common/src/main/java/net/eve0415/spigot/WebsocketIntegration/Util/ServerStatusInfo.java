package net.eve0415.spigot.WebsocketIntegration.Util;

public interface ServerStatusInfo {
    int getOnlinePlayers();

    int getMaxPlayers();

    double getTPS();
}
