package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.type.LogEventType
import net.eve0415.mc.websocketintegration.type.WIEventState
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.advancement.AdvancementEvent
import org.spongepowered.api.event.entity.DestructEntityEvent
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent
import org.spongepowered.api.event.filter.IsCancelled
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.network.ClientConnectionEvent.*
import org.spongepowered.api.util.Tristate
import java.util.*

class SpongeEventListener constructor(instance: SpongePlugin) {
  private val manager: WebsocketManager

  init {
    Sponge.getEventManager().registerListeners(instance, this)
    manager = instance.websocketManager
  }

  private fun sendChatMessage(name: String, uuid: UUID, message: String) {
    manager.send(WIEventState.CHAT, manager.builder().message(name, uuid, message).toJSON())
  }

  @Listener
  fun onJoin(event: Join) {
    sendChatMessage(event.targetEntity.name, event.targetEntity.uniqueId, event.message.toPlain())
  }

  @Listener
  fun onQuit(event: Disconnect) {
    val player = event.targetEntity

    sendChatMessage(event.targetEntity.name, event.targetEntity.uniqueId, event.message.toPlain())

    manager.send(
      WIEventState.LOG,
      manager
        .builder()
        .log(
          LogEventType.DISCONNECT,
          player.name,
          player.uniqueId,
          player.connection.address.hostString
        )
        .toJSON()
    )
  }

  @Listener
  fun onDeath(event: DestructEntityEvent.Death) {
    val player = event.targetEntity
    if (player !is Player) return
    sendChatMessage(player.name, player.uniqueId, event.message.toPlain())
  }

  @Listener
  fun onMessage(event: MessageChannelEvent.Chat) {
    val player = event.cause.first(Player::class.java).get()
    val playerName = "<" + player.name + "> "
    sendChatMessage(
      player.name,
      player.uniqueId,
      event.message.toPlain().substring(playerName.length)
    )
  }

  @Listener
  fun onAdvancementDone(event: AdvancementEvent.Grant) {
    // We don't want to call out recipes unlock as advancement complete.
    if (event.advancement.id.contains("recipes_")) return
    val adv = event.advancement.id.replace(':', '_').replace('/', '_').uppercase()
    manager.send(
      WIEventState.ADVANCEMENT,
      manager
        .builder()
        .message(event.targetEntity.name, event.targetEntity.uniqueId, adv)
        .toJSON()
    )
  }

  @Listener(order = Order.PRE)
  fun onAuth(event: Auth) {
    val connection = event.connection
    val profile = event.profile
    manager.send(
      WIEventState.LOG,
      manager
        .builder()
        .log(
          LogEventType.AUTH,
          profile.name.get(),
          profile.uniqueId,
          connection.address.hostString
        )
        .setAddress(connection.virtualHost.toString())
        .toJSON()
    )
  }

  @Listener(order = Order.BEFORE_POST)
  @IsCancelled(Tristate.UNDEFINED)
  fun onLogin(event: Login) {
    val connection = event.connection
    val profile = event.profile
    if (event.isCancelled) {
      manager.send(
        WIEventState.LOG,
        manager
          .builder()
          .log(
            LogEventType.AUTH,
            profile.name.get(),
            profile.uniqueId,
            connection.address.hostString
          )
          .setAddress(connection.virtualHost.toString())
          .kick(event.message.toPlain())
          .toJSON()
      )
    } else {
      manager.send(
        WIEventState.LOG,
        manager
          .builder()
          .log(
            LogEventType.AUTH,
            profile.name.get(),
            profile.uniqueId,
            connection.address.hostString
          )
          .setAddress(connection.virtualHost.toString())
          .toJSON()
      )
    }
  }

  @Listener
  fun onKick(event: KickPlayerEvent) {
    val profile = event.targetEntity
    manager.send(
      WIEventState.LOG,
      manager
        .builder()
        .log(
          LogEventType.KICK,
          profile.name,
          profile.uniqueId,
          profile.connection.address.hostString
        )
        .kick(event.message.toPlain())
        .toJSON()
    )
  }
}
