package net.eve0415.spigot.WebsocketIntegration

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Path
import net.eve0415.spigot.WebsocketIntegration.Config.WSIConfigFile
import net.eve0415.spigot.WebsocketIntegration.Config.WSIConfigKey
import net.eve0415.spigot.WebsocketIntegration.Enum.WSIPlatformType
import net.eve0415.spigot.WebsocketIntegration.Interface.WSIBootstrap
import net.eve0415.spigot.WebsocketIntegration.Interface.WSILogger
import org.slf4j.Logger

@Plugin(
    id = "websocketintegration",
    name = "WebsocketIntegration-Velocity",
    version = "@project.version@",
    authors = ["eve0415"])
class WSIVelocityPlugin
constructor(
    @Inject private val proxy: ProxyServer,
    @Inject private val velocitylogger: Logger,
    @Inject @DataDirectory private val configDir: Path
) : WSIBootstrap {
  // @Inject private val proxy: ProxyServer
  // @Inject private val velocitylogger: Logger
  // @Inject @DataDirectory private val configDir: Path

  override val platformType = WSIPlatformType.Velocity

  override var serverID = 0

  override val config: WSIConfigKey

  override val logger: WSILogger

  override val websocketManager: WebsocketManager

  init {
    logger = WSIVelocityLogger(velocitylogger)
    config = WSIConfigFile.load(configDir.resolve("config.yml").toFile())
    websocketManager = WebsocketManager.start(this)
  }

  override fun onEnable() {
    // this.config = WSIConfigFile.load(configDir.resolve("config.yml").toFile())
  }

  override fun onDisable() {}

  override fun getServerPort(): Int? {
    return 0
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {}

  override fun sendServerInfo() {}
}