package net.eve0415.mc.websocketintegration.type

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
