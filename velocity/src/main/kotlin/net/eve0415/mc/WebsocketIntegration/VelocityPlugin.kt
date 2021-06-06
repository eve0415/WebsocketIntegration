package net.eve0415.mc.WebsocketIntegration

import com.google.inject.Inject
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import net.eve0415.mc.WebsocketIntegration.Config.WIConfigFile
import net.eve0415.mc.WebsocketIntegration.Config.WIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WIEventState
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WIBootstrap
import net.eve0415.mc.WebsocketIntegration.Interface.WILogger
import org.slf4j.Logger
import java.nio.file.Path
import kotlin.properties.Delegates

@Plugin(
  id = "websocketintegration",
  name = "WebsocketIntegration-Velocity",
  version = "@project.version@",
  authors = ["eve0415"]
)
class VelocityPlugin : WIBootstrap {
  @Inject
  lateinit var proxy: ProxyServer

  @Inject
  private lateinit var velocitylogger: Logger

  @Inject
  @DataDirectory
  private lateinit var configDir: Path

  override val platformType = WIPlatformType.Velocity
  override var serverID: Int by Delegates.notNull()
  override lateinit var config: WIConfigKey
  override lateinit var logger: WILogger
  override lateinit var websocketManager: WebsocketManager

  override fun onEnable() {
    if (!configDir.toFile().exists()) configDir.toFile().mkdirs()

    logger = VelocityLogger(velocitylogger)
    config = WIConfigFile.load(configDir.resolve("config.yml").toFile())
    serverID = if (config.id == 0) getServerPort() else config.id
    websocketManager = WebsocketManager.start(this).initialize()

    VelocityTaskScheduler(this)
    VelocityEventListener(this)
  }

  override fun getServerPort(): Int {
    return proxy.boundAddress.port
  }

  override fun sendServerInfo() {
    val message = websocketManager.builder()
    proxy.allServers.forEach { server ->
      message.setServerName(server.serverInfo.address.port, server.serverInfo.name)
    }
    websocketManager.send(WIEventState.SERVERINFO, message.toJSON())
  }

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
