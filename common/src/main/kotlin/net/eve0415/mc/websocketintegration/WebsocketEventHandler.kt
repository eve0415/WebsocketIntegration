package net.eve0415.mc.websocketintegration

import io.socket.client.Manager
import io.socket.client.Socket
import net.eve0415.mc.websocketintegration.type.WIEventState
import net.eve0415.mc.websocketintegration.type.WIPlatformType
import org.json.JSONObject

class WebsocketEventHandler
constructor(private val instance: WebsocketManager, private val socket: Socket) {
  init {
    socket.once(Socket.EVENT_CONNECT) {
      instance.bootstrap.logger.info("Connected")
      instance.isConnected = true
      loadReconnectEvent()
      configureRoom()
      if (instance.isStarting)
        instance.send(WIEventState.STARTING, instance.builder().basic().toJSON())
      if (instance.platformType == WIPlatformType.Velocity) instance.bootstrap.sendServerInfo()
    }

    socket.on(Socket.EVENT_DISCONNECT) {
      instance.bootstrap.logger.info("Disconnected: ${it[0].toString()}")
      if (it[0].toString() == "io server disconnect") socket.connect()
      instance.isConnected = false
    }

    socket.on("chat") {
      val json = it[0] as JSONObject
      instance.bootstrap.handleChatMessage(
        json.getString("name"),
        json.getString("UUID"),
        json.getString("URL"),
        json.getString("message")
      )
    }
  }

  private fun loadReconnectEvent() {
    socket.io().on(Manager.EVENT_RECONNECT) {
      instance.bootstrap.logger.info("Reconnected")
      instance.isConnected = true
      configureRoom()
      if (instance.platformType == WIPlatformType.Velocity) instance.bootstrap.sendServerInfo()
    }
  }

  private fun configureRoom() {
    socket.emit("ROOM", instance.serverID)
  }
}
