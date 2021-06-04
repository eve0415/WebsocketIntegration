package net.eve0415.mc.WebsocketIntegration

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Path
import net.eve0415.mc.WebsocketIntegration.Config.WSIConfigFile
import net.eve0415.mc.WebsocketIntegration.Config.WSIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WSIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WSIBootstrap
import net.eve0415.mc.WebsocketIntegration.Interface.WSILogger
import org.slf4j.Logger

@Plugin(
    id = "websocketintegration",
    name = "WebsocketIntegration-Velocity",
    version = "@project.version@",
    authors = ["eve0415"])
class WSIVelocityPlugin : WSIBootstrap {
  @Inject lateinit private var proxy: ProxyServer
  @Inject lateinit private var velocitylogger: Logger
  @Inject @DataDirectory lateinit private var configDir: Path

  override val platformType = WSIPlatformType.Velocity

  override var serverID = 0

  override lateinit var config: WSIConfigKey

  override lateinit var logger: WSILogger

  override lateinit var websocketManager: WebsocketManager

  override fun onEnable() {
    logger = WSIVelocityLogger(velocitylogger)
    config = WSIConfigFile.load(configDir.resolve("config.yml").toFile())
    websocketManager = WebsocketManager.start(this)
  }

  override fun onDisable() {}

  override fun getServerPort(): Int? {
    return 0
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {}

  override fun sendServerInfo() {}
}
