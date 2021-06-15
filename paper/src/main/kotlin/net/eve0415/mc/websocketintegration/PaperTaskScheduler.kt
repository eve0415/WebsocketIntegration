package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WITask
import net.eve0415.mc.websocketintegration.type.WIEventState
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.roundToInt


class PaperTaskScheduler constructor(private val instance: PaperPlugin) : WITask {
  init {
    serverIsReady()
  }

  override fun getOnlinePlayers(): Int {
    return instance.server.onlinePlayers.size
  }

  override fun getMaxPlayers(): Int {
    return instance.server.maxPlayers
  }

  override fun getTPS(): Double {
    return (instance.server.tps[0] * 100.0).roundToInt() / 100.0
  }

  // Task runs when the server is ready to serve the player which will send status
  // info after sever had done starting up.
  override fun serverIsReady() {
    object : BukkitRunnable() {
      override fun run() {
        instance.websocketManager.isStarting = false
        updateStatus()
      }
    }.runTaskAsynchronously(instance)
  }

  override fun updateStatus() {
    object : BukkitRunnable() {
      override fun run() {
        instance.websocketManager.send(
          WIEventState.STATUS,
          instance.websocketManager.builder().basic()
            .status(getOnlinePlayers(), getMaxPlayers(), getTPS())
            .toJSON()
        )
      }
    }.runTaskTimerAsynchronously(instance, 0, 400)
  }
}
