package net.eve0415.mc.WebsocketIntegration

import com.google.inject.Inject
import java.nio.file.Path
import kotlin.properties.Delegates
import net.eve0415.mc.WebsocketIntegration.Config.WIConfigKey
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WIBootstrap
import net.eve0415.mc.WebsocketIntegration.Interface.WILogger
import org.slf4j.Logger
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(
    id = "websocketintegration",
    name = "WebsocketIntegration-Sponge",
    version = "@project.version@",
    authors = ["eve0415"])
class SpongePlugin : WIBootstrap {
  @Inject lateinit private var spongeLogger: Logger
  @Inject lateinit private var container: PluginContainer
  @Inject @DefaultConfig(sharedRoot = true) lateinit private var defaultConfig: Path

  override val platformType = WIPlatformType.Sponge
  override var serverID: Int by Delegates.notNull()
  override lateinit var config: WIConfigKey
  override lateinit var logger: WILogger
  override lateinit var websocketManager: WebsocketManager

  override fun onEnable() {}

  override fun onDisable() {}

  override fun getServerPort(): Int {
    return 0
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {}

  // Do nothing
  override fun sendServerInfo() {}
}
