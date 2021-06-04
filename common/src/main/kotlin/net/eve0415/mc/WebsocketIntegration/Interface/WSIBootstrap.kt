package net.eve0415.mc.WebsocketIntegration.Interface

import net.eve0415.mc.WebsocketIntegration.Config.WSIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WSIPlatformType
import net.eve0415.mc.WebsocketIntegration.WebsocketManager

public interface WSIBootstrap {
  val platformType: WSIPlatformType

  val serverID: Int

  val config: WSIConfigKey

  val logger: WSILogger

  val websocketManager: WebsocketManager

  fun onEnable()

  fun onDisable()

  fun getServerPort(): Int?

  fun handleChatMessage(name: String, uuid: String, url: String, message: String)

  fun sendServerInfo()
}
