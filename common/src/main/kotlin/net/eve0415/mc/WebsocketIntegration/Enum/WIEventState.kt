package net.eve0415.mc.WebsocketIntegration.Enum

enum class WIEventState {
  STARTING,
  STOPPING,
  STATUS,
  CHAT,
  ADVANCEMENT,
  SERVERINFO,
  LOG,

  // For Forge Server
  CONSTRUCTING,
  PREINITIALIZATION,
  INITIALIZATION,
  POSTINITIALIZATION,
  LOADCOMPLETE,
  ABOUTTOSTART,
  GAMESTART
}
