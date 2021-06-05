package net.eve0415.mc.WebsocketIntegration

import com.google.inject.Inject
import java.nio.file.Path
import kotlin.properties.Delegates
import net.eve0415.mc.WebsocketIntegration.Config.*
import net.eve0415.mc.WebsocketIntegration.Enum.WIEventState
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WIBootstrap
import net.eve0415.mc.WebsocketIntegration.Interface.WILogger
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.*
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(
    id = "websocketintegration",
    name = "WebsocketIntegration-Sponge",
    description = "Send server status, log, chat via websocket",
    version = "@project.version@",
    authors = ["eve0415"])
class SpongePlugin : WIBootstrap {
  @Inject lateinit private var spongeLogger: Logger
  @Inject lateinit private var container: PluginContainer
  @Inject @DefaultConfig(sharedRoot = true) lateinit private var defaultConfig: Path

  private var initialized = false

  override val platformType = WIPlatformType.Sponge
  override var serverID: Int by Delegates.notNull()
  override lateinit var config: WIConfigKey
  override lateinit var logger: WILogger
  override lateinit var websocketManager: WebsocketManager
  lateinit var chatSender: SpongeChatHandler

  override fun onEnable() {
    logger = SpongeLogger(spongeLogger)
    config = WIConfigFile.load(defaultConfig.parent.resolve("WebsocketIntegration.yml").toFile())
    chatSender = SpongeChatHandler()

    websocketManager = WebsocketManager.start(this)
  }

  override fun getServerPort(): Int? {
    return Sponge.getServer().boundAddress.get().port
  }

  override fun handleChatMessage(name: String, uuid: String, url: String, message: String) {}

  // Do nothing
  override fun sendServerInfo() {}

  private fun isServerAvailable(): Boolean {
    return Sponge.getServer().getBoundAddress().isPresent()
  }

  private fun checkStart() {
    if (initialized == true) return
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
