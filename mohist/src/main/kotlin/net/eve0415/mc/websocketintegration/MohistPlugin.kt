package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.config.WIConfigFile
import net.eve0415.mc.websocketintegration.config.WIConfigKey
import net.eve0415.mc.websocketintegration.constructor.WIBootstrap
import net.eve0415.mc.websocketintegration.constructor.WILogger
import net.eve0415.mc.websocketintegration.type.WIPlatformType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.properties.Delegates

class MohistPlugin : JavaPlugin(), WIBootstrap {
  override val platformType = WIPlatformType.Paper
  override var serverID: Int by Delegates.notNull()
  override lateinit var config: WIConfigKey
  override lateinit var logger: WILogger
  override lateinit var websocketManager: WebsocketManager
  private lateinit var chatHandler: MohistChatHandler

  override fun onEnable() {
    saveDefaultConfig()
    logger = MohistLogger(getLogger())
    config = WIConfigFile.load(dataFolder.resolve("config.yml"))
    serverID = if (config.id == 0) getServerPort() else config.id
    websocketManager = WebsocketManager.start(this).initialize()
    chatHandler = MohistChatHandler(this)
    MohistTaskScheduler(this)
    MohistEventListener(this)
  }

  override fun onDisable() {
    super<WIBootstrap>.onDisable()
  }

  override fun getServerPort(): Int {
    return server.port
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {
    chatHandler.send(name, uuid, url, message)
  }

  // Do nothing
  override fun sendServerInfo() {}
}
