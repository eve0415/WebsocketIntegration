package net.eve0415.mc.WebsocketIntegration

import java.util.concurrent.TimeUnit
import net.eve0415.mc.WebsocketIntegration.Enum.WIEventState
import net.eve0415.mc.WebsocketIntegration.Interface.WITask

public class WSIVelocityTaskScheduler constructor(private val instance: WIVelocityPlugin) : WITask {
  init {
    serverIsReady()
  }

  override fun getOnlinePlayers(): Int {
    return instance.proxy.allPlayers.size
  }

  override fun getMaxPlayers(): Int {
    return instance.proxy.configuration.showMaxPlayers
  }

  override fun getTPS(): Double {
    return 20.0
  }

  override fun serverIsReady() {
    instance
        .proxy
        .scheduler
        .buildTask(
            instance,
            Runnable() {
              fun run() {
                instance.websocketManager.isStarting = false
                updateStatus()
              }
            })
        .delay(5, TimeUnit.SECONDS)
        .schedule()
  }

  override fun updateStatus() {
    instance
        .proxy
        .scheduler
        .buildTask(
            instance,
            Runnable() {
              fun run() {
                instance.websocketManager.send(
                    WIEventState.STATUS,
                    instance
                        .websocketManager
                        .builder()
                        .basic()
                        .status(getOnlinePlayers(), getMaxPlayers(), getTPS())
                        .toJSON())
              }
            })
        .repeat(20, TimeUnit.SECONDS)
        .schedule()
  }
}
