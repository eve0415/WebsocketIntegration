package net.eve0415.spigot.WebsocketIntegration

import java.util.UUID
import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer
import org.json.JSONObject

public class WebsocketBuilder constructor(val instance: WebsocketManager) {
  val obj = JSONObject()
  val runtime = Runtime.getRuntime()

  fun basic(): WebsocketBuilder {
    val uptime = System.nanoTime() - instance.serverStartTime
    val tempSec = uptime / (1000 * 1000 * 1000)
    val days = (tempSec / (24 * 60 * 60)) % 24
    val hours = (tempSec / (60 * 60)) % 24
    val minutes = (tempSec / 60) % 60
    val seconds = tempSec % 60
    val time = days.toString() + ":" + hours + ":" + minutes + ":" + seconds

    obj.put("platform", instance.platformType.name)
    obj.put("serverId", instance.serverID)
    obj.put("serverName", instance.serverName)
    obj.put("totalMemory", (runtime.totalMemory() / 1048576L).toString() + "MB")
    obj.put(
        "usedMemory", ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L).toString() + "MB")
    obj.put("freeMemory", (runtime.freeMemory() / 1048576L).toString() + "MB")
    obj.put("uptime", time)
    return this
  }

  fun message(name: String, uuid: UUID, message: String): WebsocketBuilder {
    obj.put("serverId", instance.serverID)
    obj.put("name", name)
    obj.put("UUID", uuid)
    obj.put("message", message.replace("§.", ""))
    return this
  }

  fun message(name: String, uuid: UUID, message: Component): WebsocketBuilder {
    obj.put("serverId", instance.serverID)
    obj.put("name", name)
    obj.put("UUID", uuid)
    obj.put("message", PlainComponentSerializer.plain().serialize(message).replace("§.", ""))
    return this
  }

  fun status(onlinePlayers: Int, maxPlayers: Int, tps: Double): WebsocketBuilder {
    obj.put("onlinePlayers", onlinePlayers)
    obj.put("maxPlayers", maxPlayers)
    obj.put("tps", tps)
    return this
  }

  fun setServerName(port: Int, name: String): WebsocketBuilder {
    obj.put(port.toString(), name)
    return this
  }

  fun log(event: LogEventType, name: String, uuid: UUID, ip: String): WebsocketBuilder {
    obj.put("serverId", instance.serverID)
    obj.put("event", event.name)
    obj.put("name", name)
    obj.put("UUID", uuid)
    obj.put("ip", ip)
    return this
  }

  fun setAddress(address: String): WebsocketBuilder {
    obj.put("address", address)
    return this
  }

  fun clientType(type: String): WebsocketBuilder {
    obj.put("type", type)
    return this
  }

  fun clientVersion(version: String): WebsocketBuilder {
    obj.put("version", version)
    return this
  }

  fun mods(mods: Int): WebsocketBuilder {
    obj.put("mods", mods.toString())
    return this
  }

  fun connectingServer(server: String): WebsocketBuilder {
    obj.put("toServer", server)
    return this
  }

  fun previousServer(server: String): WebsocketBuilder {
    obj.put("fromServer", server)
    return this
  }

  fun connectedServer(server: String): WebsocketBuilder {
    obj.put("currentServer", server)
    return this
  }

  fun kick(reason: String): WebsocketBuilder {
    obj.put("reason", reason)
    return this
  }

  fun kick(reason: Component): WebsocketBuilder {
    obj.put("reason", PlainComponentSerializer.plain().serialize(reason))
    return this
  }

  fun fulfill(fulfill: String): WebsocketBuilder {
    obj.put("fulfill", fulfill)
    return this
  }

  fun toJSON(): JSONObject {
    return this.obj
  }
}
