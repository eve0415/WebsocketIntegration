package net.eve0415.spigot.WebsocketIntegration.Interface

import net.eve0415.spigot.WebsocketIntegration.Config.WSIConfigKey
import net.eve0415.spigot.WebsocketIntegration.Enum.WSIPlatformType
import net.eve0415.spigot.WebsocketIntegration.WebsocketManager

public interface WSIBootstrap {
  val platformType: WSIPlatformType

  var serverID: Int

  val config: WSIConfigKey

  val logger: WSILogger

  val websocketManager: WebsocketManager

  fun onEnable()

  fun onDisable()

  fun getServerPort(): Int?

  fun handleChatMessage(name: String, uuid: String, url: String, message: String)

  fun sendServerInfo()
}
