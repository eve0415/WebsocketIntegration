package net.eve0415.spigot.WebsocketIntegration.Util

import net.eve0415.spigot.WebsocketIntegration.WebsocketManager

public interface WSIBootstrap {
  val platformType: WSIPlatformType

  var serverID: Int

  val config: WSIConfiguration

  val logger: WSILogger

  val websocketManager: WebsocketManager

  fun onEnable()

  fun onDisable()

  fun getServerPort(): Int?

  fun handleChatMessage(name: String, uuid: String, url: String, message: String)

  fun sendServerInfo()
}
