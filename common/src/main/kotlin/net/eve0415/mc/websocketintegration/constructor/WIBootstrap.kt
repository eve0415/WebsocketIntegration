package net.eve0415.mc.websocketintegration.constructor

import net.eve0415.mc.websocketintegration.WebsocketManager
import net.eve0415.mc.websocketintegration.config.WIConfigKey
import net.eve0415.mc.websocketintegration.type.WIPlatformType

interface WIBootstrap {
  val platformType: WIPlatformType

  val serverID: Int

  val config: WIConfigKey

  val logger: WILogger

  val websocketManager: WebsocketManager

  fun onEnable()

  fun onDisable() {
    websocketManager.shutdown()
  }

  fun getServerPort(): Int?

  fun handleChatMessage(name: String, uuid: String, url: String, message: String)

  fun sendServerInfo()
}
