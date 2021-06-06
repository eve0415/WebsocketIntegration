package net.eve0415.mc.websocketintegration

import com.google.inject.Inject
import net.eve0415.mc.websocketintegration.config.WIConfigFile
import net.eve0415.mc.websocketintegration.config.WIConfigKey
import net.eve0415.mc.websocketintegration.constructor.WIBootstrap
import net.eve0415.mc.websocketintegration.constructor.WILogger
import net.eve0415.mc.websocketintegration.type.WIEventState
import net.eve0415.mc.websocketintegration.type.WIPlatformType
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.*
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import java.nio.file.Path
import kotlin.properties.Delegates

@Plugin(
  id = "websocketintegration",
  name = "WebsocketIntegration-Sponge",
  description = "Send server status, log, chat via websocket",
  version = "@project.version@",
  authors = ["eve0415"]
)
class SpongePlugin : WIBootstrap {
  @Inject
  private lateinit var spongeLogger: Logger

  @Inject
  private lateinit var container: PluginContainer

  @Inject
  @DefaultConfig(sharedRoot = true)
  private lateinit var defaultConfig: Path

  private var initialized = false

  override val platformType = WIPlatformType.Sponge
  override var serverID: Int by Delegates.notNull()
  override lateinit var config: WIConfigKey
  override lateinit var logger: WILogger
  override lateinit var websocketManager: WebsocketManager
  private lateinit var chatHandler: SpongeChatHandler

  override fun onEnable() {
    logger = SpongeLogger(spongeLogger)
    config = WIConfigFile.load(defaultConfig.parent.resolve("WebsocketIntegration.yml").toFile())
    chatHandler = SpongeChatHandler()

    websocketManager = WebsocketManager.start(this)
  }

  override fun getServerPort(): Int {
    return Sponge.getServer().boundAddress.get().port
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {
    chatHandler.send(name, uuid, url, message)
  }

  // Do nothing
  override fun sendServerInfo() {}

  private fun isServerAvailable(): Boolean {
    return Sponge.getServer().boundAddress.isPresent
  }

  private fun checkStart() {
    if (initialized) return
    if (config.id != 0 || isServerAvailable()) {
      serverID = config.id
      websocketManager.initialize()
      initialized = true
    }
  }

  private fun updateStatus(state: WIEventState) {
    websocketManager.send(state, websocketManager.builder().basic().toJSON())
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GameConstructionEvent) {
    onEnable()
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.CONSTRUCTING)
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GamePreInitializationEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.PREINITIALIZATION)
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GameInitializationEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.INITIALIZATION)
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GamePostInitializationEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.POSTINITIALIZATION)
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GameLoadCompleteEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.LOADCOMPLETE)
  }

  @Listener(beforeModifications = true)
  fun onServerLoad(@Suppress("UNUSED_PARAMETER") e: GameAboutToStartServerEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.ABOUTTOSTART)
  }

  @Listener
  fun onServerStart(@Suppress("UNUSED_PARAMETER") e: GameStartedServerEvent) {
    checkStart()
    if (websocketManager.isConnected) updateStatus(WIEventState.GAMESTART)
    SpongeTaskScheduler(this)
    SpongeEventListener(this)
  }

  @Listener
  fun onServerStop(@Suppress("UNUSED_PARAMETER") e: GameStoppingServerEvent) {
    if (websocketManager.isConnected) updateStatus(WIEventState.STOPPING)
  }

  @Listener
  fun onServerStop(@Suppress("UNUSED_PARAMETER") e: GameStoppedServerEvent) {
    onDisable()
  }
}
