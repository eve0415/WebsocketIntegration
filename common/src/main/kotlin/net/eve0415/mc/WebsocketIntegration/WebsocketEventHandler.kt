package net.eve0415.spigot.WebsocketIntegration

import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType
import org.json.JSONObject

class WebsocketEventHandler constructor(val instance: WebsocketManager, val socket: Socket) {
  init {
    socket.once(
        Socket.EVENT_CONNECT,
        Emitter.Listener() {
          fun call() {
            instance.bootstrap.logger.info("Connected")
            instance.isConnected = true
            loadReconnectEvent()
            configureRoom()
            if (instance.isStarting)
                instance.send(WSIEventState.STARTING, instance.builder().basic().toJSON())
            if (instance.platformType == WSIPlatformType.Velocity)
                instance.bootstrap.sendServerInfo()
          }
        })

    socket.on(
        Socket.EVENT_DISCONNECT,
        Emitter.Listener() {
          fun call(reason: String) {
            instance.bootstrap.logger.info("Disconnected: " + reason)
            if (reason.equals("io server disconnect")) socket.connect()
            instance.isConnected = false
          }
        })

    socket.on(
        "chat",
        Emitter.Listener() {
          fun call(json: JSONObject) {
            instance.bootstrap.handleChatMessage(
                json.getString("name"),
                json.getString("UUID"),
                json.getString("URL"),
                json.getString("message"))
          }
        })
  }

  fun loadReconnectEvent() {
    socket
        .io()
        .on(
            Manager.EVENT_RECONNECT,
            Emitter.Listener() {
              fun call() {
                instance.bootstrap.logger.info("Reconnected")
                instance.isConnected = true
                configureRoom()
                if (instance.isStarting)
                    instance.send(WSIEventState.STARTING, instance.builder().basic().toJSON())
                if (instance.platformType == WSIPlatformType.Velocity)
                    instance.bootstrap.sendServerInfo()
              }
            })
  }

  fun configureRoom() {
    socket.emit("ROOM", instance.serverID)
  }
}
