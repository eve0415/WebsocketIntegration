package net.eve0415.mc.WebsocketIntegration.Interface

import net.eve0415.mc.WebsocketIntegration.Config.WIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.WebsocketManager

public interface WIBootstrap {
  val platformType: WIPlatformType

  val serverID: Int

  val config: WIConfigKey

  val logger: WILogger

  val websocketManager: WebsocketManager

  fun onEnable()

  fun onDisable()

  fun getServerPort(): Int?

  fun handleChatMessage(name: String, uuid: String, url: String, message: String)

  fun sendServerInfo()
}
