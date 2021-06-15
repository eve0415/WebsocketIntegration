package net.eve0415.mc.websocketintegration.constructor

interface ServerStatusInfo {
  fun getOnlinePlayers(): Int

  fun getMaxPlayers(): Int

  fun getTPS(): Double
}
