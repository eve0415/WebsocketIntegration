package net.eve0415.spigot.WebsocketIntegration.Interface

public interface WSITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
