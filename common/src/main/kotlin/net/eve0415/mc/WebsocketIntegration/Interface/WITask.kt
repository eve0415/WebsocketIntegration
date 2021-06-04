package net.eve0415.mc.WebsocketIntegration.Interface

public interface WITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
