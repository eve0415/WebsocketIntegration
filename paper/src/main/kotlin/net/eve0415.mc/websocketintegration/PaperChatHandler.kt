package net.eve0415.mc.websocketintegration

import net.eve0415.mc.websocketintegration.constructor.WIChatHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import java.util.*


class PaperChatHandler constructor(private val instance: PaperPlugin) : WIChatHandler {
  override fun chatHandler(name: String, uuid: String, url: String, message: String) {
    val mes = messageFormatter(getName(name, uuid), message)
    val component = Component.text(mes);

    if (url != "null") {
      component
        .hoverEvent(
          HoverEvent.hoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            Component.text("Click to open the website!")
          )
        )
        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url))
    }

    instance.server.sendMessage(component)
    // Using TextComponent, it does not send to console so we do this way
    instance.server.consoleSender.sendMessage(mes)
  }

  override fun getName(name: String, uuid: String): String {
    if (uuid == "null") return name
    return if (instance.server.getPlayer(UUID.fromString(uuid)) == null)
      instance.server.getOfflinePlayer(UUID.fromString(uuid)).name!! else instance.server.getPlayer(
      UUID.fromString(uuid)
    )!!.name
  }
}
