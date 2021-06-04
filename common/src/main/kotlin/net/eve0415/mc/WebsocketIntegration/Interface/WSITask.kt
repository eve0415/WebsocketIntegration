package net.eve0415.mc.WebsocketIntegration.Interface

public interface WSITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
