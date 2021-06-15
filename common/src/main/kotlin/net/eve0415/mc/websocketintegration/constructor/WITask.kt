package net.eve0415.mc.websocketintegration.constructor

interface WITask : ServerStatusInfo {
  fun serverIsReady()

  fun updateStatus()
}
