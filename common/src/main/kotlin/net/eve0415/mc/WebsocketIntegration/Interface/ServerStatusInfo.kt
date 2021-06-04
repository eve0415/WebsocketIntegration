package net.eve0415.mc.WebsocketIntegration.Interface

public interface ServerStatusInfo {
  fun getOnlinePlayers(): Int

  fun getMaxPlayers(): Int

  fun getTPS(): Double
}
