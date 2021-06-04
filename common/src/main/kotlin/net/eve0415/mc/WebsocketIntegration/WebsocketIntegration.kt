package net.eve0415.mc.WebsocketIntegration

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import net.eve0415.mc.WebsocketIntegration.Enum.WSIEventState
import net.eve0415.mc.WebsocketIntegration.Enum.WSIPlatformType
import net.eve0415.mc.WebsocketIntegration.Interface.WSIBootstrap
import org.json.JSONObject

class WebsocketManager private constructor(val bootstrap: WSIBootstrap) {
  val serverStartTime = System.nanoTime()

  var socket: Socket? = null

  val platformType: WSIPlatformType = bootstrap.platformType
  val serverID: Int = bootstrap.serverID
  val serverName: String =
      if (bootstrap.config.name.equals("auto")) platformType.name else bootstrap.config.name

  var isStarting = true
  var isConnected = false

  companion object {
    fun start(bootstrap: WSIBootstrap): WebsocketManager {
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

    try {
      val socket = IO.socket("http://" + configuration.address + ":" + configuration.port)
      WebsocketEventHandler(this, socket)
      this.socket = socket
    } catch (e: URISyntaxException) {
      logger.error("An error occurred while opening socket", e)
      shutdown()
    }

    socket?.connect()

    logger.info("Successfully enabled")
    return this
  }

  fun shutdown() {
    if (!isStarting) send(WSIEventState.STOPPING, builder().basic().toJSON())

    socket?.close()
    socket = null

    bootstrap.logger.info("Shutting down WebsocketIntegration")
  }

  fun builder(): WebsocketBuilder {
    return WebsocketBuilder(this)
  }

  fun send(event: WSIEventState, content: JSONObject) {
    if (isConnected) socket?.emit(event.name, content)
  }
}
