package net.eve0415.mc.WebsocketIntegration

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Path
import net.eve0415.mc.WebsocketIntegration.Config.WIConfigFile
import net.eve0415.mc.WebsocketIntegration.Config.WIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WIBootstrap
import net.eve0415.mc.WebsocketIntegration.Interface.WILogger
import org.slf4j.Logger

@Plugin(
    id = "websocketintegration",
    name = "WebsocketIntegration-Velocity",
    version = "@project.version@",
    authors = ["eve0415"])
class WIVelocityPlugin : WIBootstrap {
  @Inject lateinit private var proxy: ProxyServer
  @Inject lateinit private var velocitylogger: Logger
  @Inject @DataDirectory lateinit private var configDir: Path

  override val platformType = WIPlatformType.Velocity

  override var serverID = 0

  override lateinit var config: WIConfigKey

  override lateinit var logger: WILogger

  override lateinit var websocketManager: WebsocketManager

  override fun onEnable() {
    if (!configDir.toFile().exists()) configDir.toFile().mkdirs()

    logger = WIVelocityLogger(velocitylogger)
    config = WIConfigFile.load(configDir.resolve("config.yml").toFile())
    websocketManager = WebsocketManager.start(this)
  }

  override fun onDisable() {}

  override fun getServerPort(): Int? {
    return 0
  }

  override fun sendServerInfo() {}

  // Proxy will not handle any messages to send to players.
  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {}

  @Subscribe(order = PostOrder.FIRST)
  fun onProxyInit(@Suppress("UNUSED_PARAMETER") e: ProxyInitializeEvent) {
    onEnable()
  }

  @Subscribe(order = PostOrder.LAST)
  fun onProxyShutdown(@Suppress("UNUSED_PARAMETER") e: ProxyShutdownEvent) {
    onDisable()
  }
}
