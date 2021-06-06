package net.eve0415.mc.WebsocketIntegration.Interface

interface WITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
