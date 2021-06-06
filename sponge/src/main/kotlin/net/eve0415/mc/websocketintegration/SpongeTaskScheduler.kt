package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WITask
import net.eve0415.mc.websocketintegration.type.WIEventState
import org.spongepowered.api.Sponge
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

class SpongeTaskScheduler constructor(private val instance: SpongePlugin) : WITask {
  private val taskBuilder = Task.builder()

  init {
    serverIsReady()
  }

  override fun getOnlinePlayers(): Int {
    return Sponge.getServer().onlinePlayers.size
  }

  override fun getMaxPlayers(): Int {
    return Sponge.getServer().maxPlayers
  }

  override fun getTPS(): Double {
    return Sponge.getServer().ticksPerSecond
  }

  override fun serverIsReady() {
    taskBuilder
      .execute(
        Runnable {
          fun run() {
            instance.websocketManager.isStarting = false
            updateStatus()
          }
        })
      .async()
      .submit(instance)
  }

  override fun updateStatus() {
    taskBuilder
      .execute(
        Runnable {
          fun run() {
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
        })
      .async()
      .interval(20, TimeUnit.SECONDS)
      .submit(instance)
  }
}
