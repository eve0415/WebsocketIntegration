package net.eve0415.mc.WebsocketIntegration

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.connection.PreLoginEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent
import com.velocitypowered.api.event.player.KickedFromServerEvent.*
import com.velocitypowered.api.event.player.ServerPostConnectEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import com.velocitypowered.api.proxy.Player
import java.util.Optional
import net.eve0415.mc.WebsocketIntegration.Enum.LogEventType
import net.eve0415.mc.WebsocketIntegration.Enum.WIEventState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer

public class VelocityEventListener constructor(private val instance: VelocityPlugin) {
  val manager: WebsocketManager

  init {
    instance.proxy.eventManager.register(instance, this)
    manager = instance.websocketManager
  }

  @Subscribe
  fun onPreLogin(event: PreLoginEvent) {
    val connection = event.connection
    manager.send(
        WIEventState.LOG,
        manager
            .builder()
            .log(LogEventType.AUTH, event.username, null, connection.remoteAddress.toString())
            .setAddress(connection.virtualHost.get().toString())
            .clientVersion(connection.protocolVersion.versionIntroducedIn)
            .toJSON())
  }

  @Subscribe
  fun onLogin(event: LoginEvent) {
    val player = event.player
    manager.send(
        WIEventState.LOG,
        manager
            .builder()
            .log(
                LogEventType.LOGIN,
                player.username,
                player.uniqueId,
                player.remoteAddress.toString())
            .setAddress(player.virtualHost.get().toString())
            .clientVersion(player.protocolVersion.versionIntroducedIn)
            .toJSON())
  }

  @Subscribe
  fun onPreConnect(event: ServerPreConnectEvent) {
    val player = event.player
    manager.send(
        WIEventState.LOG,
        manager
            .builder()
            .log(
                LogEventType.PRECONNECT,
                player.username,
                player.uniqueId,
                player.remoteAddress.toString())
            .setAddress(player.virtualHost.get().toString())
            .clientType(getClientType(player))
            .clientVersion(player.protocolVersion.versionIntroducedIn)
            .connectingServer(event.originalServer.serverInfo.name)
            .toJSON())
  }

  @Subscribe
  fun onPostConnect(event: ServerPostConnectEvent) {
    val player = event.player
    manager.send(
        WIEventState.LOG,
        manager
            .builder()
            .log(
                LogEventType.POSTCONNECT,
                player.username,
                player.uniqueId,
                player.remoteAddress.toString())
            .setAddress(player.virtualHost.get().toString())
            .clientType(getClientType(player))
            .clientVersion(player.protocolVersion.versionIntroducedIn)
            .connectedServer(player.currentServer.get().serverInfo.name)
            .previousServer(event.previousServer?.serverInfo?.name)
            .toJSON())
  }

  @Subscribe
  fun onDisconnect(event: DisconnectEvent) {
    val player = event.player
    manager.send(
        WIEventState.LOG,
        manager
            .builder()
            .log(
                LogEventType.DISCONNECT,
                player.username,
                player.uniqueId,
                player.remoteAddress.toString())
            .setAddress(player.virtualHost.get().toString())
            .toJSON())
  }

  @Subscribe
  fun onKicked(event: KickedFromServerEvent) {
    val player = event.player
    val result = event.result
    val message =
        manager
            .builder()
            .log(
                LogEventType.KICKEDFROM,
                player.username,
                player.uniqueId,
                player.remoteAddress.toString())
            .setAddress(player.virtualHost.get().toString())
            .previousServer(event.server.serverInfo.name)

    if (result is DisconnectPlayer) {
      message
          .kick(getReason(event.getServerKickReason(), result.reasonComponent))
          .fulfill("Disconnect Player")
    } else if (result is RedirectPlayer) {
      message
          .kick(getReason(event.getServerKickReason(), result.messageComponent))
          .fulfill("Redirect Player")
    } else if (result is Notify) {
      message
          .kick(getReason(event.getServerKickReason(), result.messageComponent))
          .fulfill("Notify Player")
    }

    manager.send(WIEventState.LOG, message.toJSON())
  }

  private fun getClientType(player: Player): String {
    return if (player.gameProfileProperties.size == 2) player.gameProfileProperties.get(1).name
    else "Vanilla"
  }

  private fun getReason(optional: Optional<Component>, event: Component?): Component {
    if (!optional.isPresent() && event == null)
        return PlainComponentSerializer.plain().deserialize("Unknown Reason")
    return event ?: optional.get()
  }
}
