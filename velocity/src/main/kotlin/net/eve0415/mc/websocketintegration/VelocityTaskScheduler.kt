package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WITask
import net.eve0415.mc.websocketintegration.type.WIEventState
import java.util.concurrent.TimeUnit

class VelocityTaskScheduler constructor(private val instance: VelocityPlugin) : WITask {
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
        instance
      ) {
        instance.websocketManager.isStarting = false
        updateStatus()
      }
      .delay(10, TimeUnit.SECONDS)
      .schedule()
  }

  override fun updateStatus() {
    instance
      .proxy
      .scheduler
      .buildTask(
        instance
      ) {
        instance.websocketManager.send(
          WIEventState.STATUS,
          instance
            .websocketManager
            .builder()
            .basic()
            .status(getOnlinePlayers(), getMaxPlayers(), getTPS())
            .toJSON()
        )
      }
      .repeat(20, TimeUnit.SECONDS)
      .schedule()
  }
}
