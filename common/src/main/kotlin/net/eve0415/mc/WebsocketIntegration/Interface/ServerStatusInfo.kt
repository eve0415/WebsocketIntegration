package net.eve0415.spigot.WebsocketIntegration.Interface

public interface ServerStatusInfo {
  fun getOnlinePlayers(): Int

  fun getMaxPlayers(): Int

  fun getTPS(): Double
}
