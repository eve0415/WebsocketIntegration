package net.eve0415.mc.WebsocketIntegration

import io.socket.client.IO
import io.socket.client.Socket
import net.eve0415.mc.WebsocketIntegration.Enum.WIEventState
import net.eve0415.mc.WebsocketIntegration.Enum.WIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WIBootstrap
import org.json.JSONObject

class WebsocketManager private constructor(val bootstrap: WIBootstrap) {
  val serverStartTime = System.nanoTime()

  private var socket: Socket? = null

  val platformType: WIPlatformType = bootstrap.platformType
  val serverID: Int = bootstrap.serverID
  val serverName: String =
      if (bootstrap.config.name.equals("auto")) platformType.name else bootstrap.config.name

  var isStarting = true
  var isConnected = false

  companion object {
    fun start(bootstrap: WIBootstrap): WebsocketManager {
      return WebsocketManager(bootstrap)
    }
  }

  fun initialize(): WebsocketManager {
    val logger = bootstrap.logger
    val configuration = bootstrap.config

    logger.info("Starting WebsocketIntegration on " + bootstrap.platformType + "...")

    if (!(0 <= configuration.port && configuration.port <= 65535)) {
      logger.error("Invalid port configured")
      shutdown()
    }

    val socket = IO.socket("http://" + configuration.address + ":" + configuration.port)
    WebsocketEventHandler(this, socket)
    socket.connect()

    this.socket = socket

    logger.info("Successfully enabled")
    return this
  }

  fun shutdown() {
    if (!isStarting) send(WIEventState.STOPPING, builder().basic().toJSON())

    socket?.close()
    socket = null

    bootstrap.logger.info("Shutting down WebsocketIntegration")
  }

  fun builder(): WebsocketBuilder {
    return WebsocketBuilder(this)
  }

  fun send(event: WIEventState, content: JSONObject) {
    if (isConnected) socket?.emit(event.name, content)
  }
}
