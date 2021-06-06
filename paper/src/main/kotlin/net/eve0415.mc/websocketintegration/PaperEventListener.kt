package net.eve0415.mc.websocketintegration

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent
import io.papermc.paper.event.player.AsyncChatEvent
import net.eve0415.mc.websocketintegration.type.LogEventType
import net.eve0415.mc.websocketintegration.type.WIEventState
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import java.util.*


class PaperEventListener constructor(private val instance: PaperPlugin) : Listener {
  private val manager: WebsocketManager

  init {
    instance.server.pluginManager.registerEvents(this, instance)
    manager = instance.websocketManager
  }

  private fun sendChatMessage(name: String, uuid: UUID, message: Component) {
    manager.send(
      WIEventState.CHAT,
      manager.builder().message(name, uuid, message).toJSON()
    )
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onJoin(event: PlayerJoinEvent) {
    event.joinMessage()?.let { sendChatMessage(event.player.name, event.player.uniqueId, it) }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onQuit(event: PlayerQuitEvent) {
    event.quitMessage()?.let { sendChatMessage(event.player.name, event.player.uniqueId, it) }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onDeath(event: PlayerDeathEvent) {
    event.deathMessage()?.let { sendChatMessage(event.entity.name, event.entity.uniqueId, it) }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onMessage(event: AsyncChatEvent) {
    sendChatMessage(event.player.name, event.player.uniqueId, event.message())
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onAdvancementDone(event: PlayerAdvancementDoneEvent) {
    // We don't want to call out recipes unlock as advancement complete.
    if (event.advancement.key.key.contains("recipes/")) return
    val adv = "${event.advancement.key.namespace.uppercase()}_${
      event.advancement.key.key.replace('/', '_').uppercase()
    }"

    manager.send(
      WIEventState.ADVANCEMENT, manager.builder()
        .message(event.player.name, event.player.uniqueId, adv).toJSON()
    )
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
    manager.send(
      WIEventState.LOG, manager.builder()
        .log(LogEventType.AUTH, event.name, event.uniqueId, event.address.hostAddress)
        .toJSON()
    )
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onPlayerLogin(event: PlayerLoginEvent) {
    if (event.result == PlayerLoginEvent.Result.ALLOWED) {
      manager.send(
        WIEventState.LOG,
        manager
          .builder().log(
            LogEventType.LOGIN, event.player.name,
            event.player.uniqueId, event.address.hostAddress
          )
          .setAddress(event.hostname).toJSON()
      )
    } else {
      manager.send(
        WIEventState.LOG,
        manager
          .builder().log(
            LogEventType.KICK, event.player.name,
            event.player.uniqueId, event.hostname
          )
          .kick(event.kickMessage()).toJSON()
      )
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onPlayerKick(event: PlayerKickEvent) {
    manager.send(
      WIEventState.LOG,
      manager
        .builder().log(
          LogEventType.KICK, event.player.name,
          event.player.uniqueId, event.player.address.hostName
        )
        .kick(event.reason()).toJSON()
    )
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun onPlayerConnectionClose(event: PlayerConnectionCloseEvent) {
    manager.send(
      WIEventState.LOG,
      manager.builder().log(
        LogEventType.DISCONNECT, event.playerName,
        event.playerUniqueId, event.ipAddress.hostName
      ).toJSON()
    )
  }
}
