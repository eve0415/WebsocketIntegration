package net.eve0415.spigot.WebsocketIntegration.Util

public interface WSITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
